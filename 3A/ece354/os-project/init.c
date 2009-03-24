#define _RTX_EVAL_
#include "rtx.h"

RTXEVAL_PROC RTXEVAL_TEST_PROCS[NUM_TEST_PROCS];

void setTestProcs ()
{
	
}

int __main () { return 0; }

int main ()
{
	setTestProcs();
	pcb_init();
	asm ("jsr restore_pcb");
	asm ("move.w %d0, -(%sp)");
	asm ("move.w 4(%sp), %d0");
	asm ("move.w %d0, (%sp)+");
	asm ("add.l #4, %sp");
	asm ("rts");
	return 0;
}