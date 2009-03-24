#define _RTX_EVAL_
#include "rtx.h"
#include "pcb.h"
#include "null_proc.h"

#define NUM_PROCS NUM_TEST_PROCS+1

PCB pcb_map[NUM_PROCS];
PCB_node pcb_nodes[NUM_PROCS-1]; //don't need node for currently executing process
PCB* current_process;

PCB_list* message_blocked_processes;
PCB_list* msg_header_blocked_processes[5];
PCB_list* uart_blocked_processes;
PCB_list* memory_blocked_processes[5];
PCB_list* ready_processes[5];

static UINT32 mem_counter;

VOID* pcb_init ()
{
	int i = 0;
	int priority, pid;
	
	while (i < 5)
	{
		msg_header_blocked_processes[i] = NULL;
		memory_blocked_processes[i] = NULL;
		ready_processes[i] = NULL;
		i++;
	}
	uart_blocked_processes = NULL;
	message_blocked_processes = NULL;
	rtx_dbug_out_char('.');
	
	asm ("move.l %d0, -(%a7)");
	asm ("move.l #proc_stack, %d0");
	asm ("move.l %d0, mem_counter");
	asm ("move.l (%a7)+, %d0");
	
	//null process
	pcb_map[0].pid = 0;
	pcb_map[0].priority = 4;
	pcb_map[0].sr = 0;
	pcb_map[0].pc = (UINT32)(&null_proc);
	pcb_map[0].is_i_process = 0;
	pcb_map[0].msg_queue = NULL;
	pcb_map[0].state = ST_EXECUTING;
	
	pcb_map[0].stack_size[0] = mem_counter;
	mem_counter += 16;
	pcb_map[0].stack_size[1] = mem_counter;
	pcb_map[0].add_reg[7] = mem_counter - 16;
	rtx_dbug_out_char('.');
	
	i=0;
	while (i < NUM_TEST_PROCS)
	{
		pid = RTXEVAL_TEST_PROCS[i].pid;
		pcb_map[pid].pid = RTXEVAL_TEST_PROCS[i].pid;
		pcb_map[pid].priority = RTXEVAL_TEST_PROCS[i].priority;
		pcb_map[pid].sr = 0;
		pcb_map[pid].pc = (UINT32)(RTXEVAL_TEST_PROCS[i].rtxeval_proc_entry);
		pcb_map[pid].is_i_process = 0;
		pcb_map[pid].msg_queue = NULL;
		pcb_map[pid].state = ST_READY;
		
		pcb_map[pid].stack_size[0] = mem_counter;
		mem_counter += (RTXEVAL_TEST_PROCS[i].stacksize + 16);
		pcb_map[pid].stack_size[1] = mem_counter;
		pcb_map[pid].add_reg[7] = mem_counter - 16;
		
		pcb_nodes[i].pcb = &(pcb_map[pid]);
		priority = pcb_map[i+1].priority;
		pcb_nodes[i].next = ready_processes[priority];
		ready_processes[priority] = &(pcb_nodes[i]);
		
		i++;
		rtx_dbug_out_char('.');
	}
	
	current_process = &(pcb_map[0]);
	process_switch();
	rtx_dbug_out_char('\r');
	rtx_dbug_out_char('\n');
	
	return (VOID*)mem_counter; //first free block of heap memory
}

VOID process_switch ()
{
}