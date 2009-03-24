#define _MESSAGES_C
#include "messages.h"
#include "pcb.h"

extern UINT32 current_time;

#define NUM_HEADERS 32

PCB_list* message_blocked_processes;
PCB_list* msg_header_blocked_processes[5];

typedef struct msg_header_t
{
	UINT32 sender_id;
	UINT32 dest_id;
	UINT32 msg_type;
	VOID* message;
	UINT32 send_time;
} msg_header;

struct msg_header_list_t
{
	msg_header head;
	struct msg_header_list_t* next;
};

static msg_list* free_headers;
static msg_list headers[NUM_HEADERS];

VOID k_message_init (VOID)
{
	UINT32 i = 0;
	while (i < NUM_HEADERS-1)
	{
		headers[i].next = &(headers[i+1]);
		i++;
	}
	headers[NUM_HEADERS-1].next = NULL;
	free_headers = &(headers[0]);
	
	message_blocked_processes = NULL;
	i = 0;
	while (i < 5)
	{
		msg_header_blocked_processes[i] = NULL;
		i++;
	}
}

typedef msg_list msg_header_node;

UINT32 process_switch();

static msg_header_node* get_msg_header ()
{
	atomic(TRUE);
	while (free_headers == NULL)
	{
		atomic (FALSE);
		if (current_process->is_i_process) return NULL;
		current_process->state = ST_BLOCKED_ON_MESSAGE_HEADER;
		process_switch();
		atomic (TRUE);
	}
	msg_list* header = free_headers;
	free_headers = header->next;
	header->next = NULL;
	atomic (FALSE);
	return header;
}

static VOID free_msg_header (msg_header_node* header)
{
	atomic (TRUE);
	header->next = free_headers;
	free_headers = header;

	UINT16 level = 0;
	UINT16 need_switch = FALSE;
	while (level < 5)
	{
		if (msg_header_blocked_processes[level] != NULL)
		{
			PCB* pcb = msg_header_blocked_processes[level]->pcb;
			pcb->state = ST_READY;
			move_pcb (&(msg_header_blocked_processes[level]), &(ready_processes[level]), pcb->pid);
			if (current_process->priority > pcb->priority) need_switch = TRUE;
			break;
		}
		level++;
	}

	atomic (FALSE);
	if (need_switch) process_switch();
}

SINT32 k_delayed_send (UINT32 process_id, UINT32 msg_type, VOID* Message, UINT32 delay)
{
	PCB* receiver = pid_to_pcb (process_id);
	if (receiver == NULL) return RTX_ERROR;

	msg_header_node* msg = get_msg_header ();
	if (msg == NULL) return RTX_ERROR;
	msg->head.sender_id = current_process->pid;
	msg->head.dest_id = process_id;
	msg->head.msg_type = msg_type;
	msg->head.message = Message;
	msg->head.send_time = current_time + delay;

	msg_list* head = receiver->msg_queue;
	if (head == NULL || msg->head.send_time < head->head.send_time)
	{
		msg->next = receiver->msg_queue;
		receiver->msg_queue = msg;
	}
	else
	{
		while (head->next != NULL && head->next->head.send_time < msg->head.send_time) head = head->next;
		msg->next = head->next;
		head->next = msg;
	}

	if (delay == 0 && receiver->state == ST_BLOCKED_ON_MESSAGE)
	{
		receiver->state = ST_READY;
		move_pcb (&message_blocked_processes, &(ready_processes[receiver->priority]), receiver->pid);
		if (current_process->priority > receiver->priority) process_switch();
	}

	return RTX_SUCCESS;
}

VOID* k_receive_message (UINT32* sender_ID, UINT32* msg_type)
{
	msg_list* head = current_process->msg_queue;
	while (head == NULL || head->head.send_time > current_time)
	{
		if (current_process->is_i_process) return NULL;
		current_process->state = ST_BLOCKED_ON_MESSAGE;
		process_switch();
		head = current_process->msg_queue;
	}
	current_process->msg_queue = head->next;

	if (sender_ID != NULL) *sender_ID = head->head.sender_id;
	if (msg_type != NULL) *msg_type = head->head.msg_type;

	VOID* msg = head->head.message;
	free_msg_header (head);
	return msg;
}
