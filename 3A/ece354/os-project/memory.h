#ifndef MEMORY_H
#define MEMORY_H

extern int memory_block_size;

typedef struct memory_list_t
{
	void* block;
	struct memory_list_t* next;
} memory_list;

void memory_init( void* first_free_memory );

#endif
