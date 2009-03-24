#include "rtx.h"
#include "pcb.h"
#include "memory.h"

#define NUM_MEMORY_BLOCKS 32

static memory_list memory_blocks[NUM_MEMORY_BLOCKS];
static memory_list* free_memory_blocks;

PCB_list* memory_blocked_processes[5];

void* request_memory_block( void )
{
  atomic( TRUE );
  while( free_memory_blocks == NULL ) {
    atomic( FALSE );
    if( current_process->is_i_process ) return NULL;
    current_process->state = ST_BLOCKED_ON_MEMORY;
    process_switch();
    atomic( TRUE );
  }

  void* retn = free_memory_blocks->block;
  free_memory_blocks = free_memory_blocks->next;
  atomic( FALSE );
  return retn;
}

int release_memory_block( void* MemoryBlock )
{
  atomic( TRUE );

  unsigned int i = 0;
  memory_list* memory_block;
  int need_switch;

  for ( i; i < NUM_MEMORY_BLOCKS; ++i )
  {
    if( MemoryBlock == memory_blocks[i].block ) {
      memory_block = &memory_blocks[i];
      break;
    }
  }

  if( free_memory_blocks == NULL ) {
    free_memory_blocks = memory_block;
  }
  else {
    memory_block->next = free_memory_blocks;
    free_memory_blocks = memory_block;
  }

  for( i = 0; i < 5; i++ ) {

    if( memory_blocked_processes != NULL ) {
      PCB* pcb = memory_blocked_processes[i]->pcb;
      pcb->state = ST_READY;
      move_pcb ( &(msg_header_blocked_processes[i]), &(ready_processes[i]), pcb->pid );
      if( current_process->priority > pcb->priority ) need_switch = TRUE;
      break;
    }
  }

  atomic( FALSE );
  if( need_switch ) process_switch();
  return 1;
}

void memory_init( void* first_free_memory )
{
  unsigned int i = 0;
  while( i < NUM_MEMORY_BLOCKS - 1 )
  {
    memory_blocks[i].block = first_free_memory + ( memory_block_size *  i);
    memory_blocks[i].next = &(free_memory_blocks[i+1]);
    i++;
  }

  memory_blocks[NUM_MEMORY_BLOCKS - 1].next = NULL;
  free_memory_blocks = &(memory_blocks[0]);

  i = 0;
  while( i < 5 )
  {
    memory_blocked_processes[i] = NULL;
    i++;
  }

}
