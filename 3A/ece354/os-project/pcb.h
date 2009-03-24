#ifndef _PCB_H
#define _PCB_H

#define ST_EXECUTING 0
#define ST_READY 1
#define ST_BLOCKED_ON_UART 2
#define ST_BLOCKED_ON_MESSAGE 3
#define ST_BLOCKED_ON_MESSAGE_HEADER 4
#define ST_BLOCKED_ON_MEMORY 5
#define ST_ZOMBIE -1

#include "rtx_inc.h"

typedef struct msg_header_list_t msg_list;

typedef struct PCB_t
{
	SINT32 pid;
	SINT32 priority;
	SINT32 state;
	SINT32 is_i_process;
	msg_list* msg_queue;
	SINT32 data_reg[8];
	SINT32 add_reg[8];
	SINT32 pc;
	SINT32 sr;
	SINT32 stack_size[2];
} PCB;

typedef struct PCB_list_t
{
	PCB* pcb;
	struct PCB_list_t* next;
} PCB_list;

typedef PCB_list PCB_node;

extern PCB* current_process;

extern PCB pcb_map[];
extern PCB_list* message_blocked_processes;
extern PCB_list* msg_header_blocked_processes[5];
extern PCB_list* uart_blocked_processes;
extern PCB_list* memory_blocked_processes[5];
extern PCB_list* ready_processes[5];

VOID* pcb_init ();

PCB* pid_to_pcb (SINT32 process_id);
SINT32 pcb_to_pid (PCB* pcb);

SINT32 move_pcb (PCB_list** from, PCB_list** to, SINT32 pid);
int add_pcb (PCB_list** list, PCB_node* pcb);

#endif
