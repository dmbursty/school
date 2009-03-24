.globl asm_timer_entry
.globl save_pcb
.globl restore_pcb

********************************************************
** asm_timer_entry - Entry point for timer interrupt  **
********************************************************
asm_timer_entry:
	jsr save_pcb
	jsr asm_timer_isr	;**Defined in pcb_hw_save_test.c**
	jsr restore_pcb
	rte

************************************************
** save_pcb - Saves all registers, including  **
**            SR and PC, into current_process **
** REQUIRED:  Processor must be starting to   **
**            deal with some exception        **
************************************************
save_pcb:
	move.l %a0, -(%a7)
	move.l current_process, %a0
	move.l %d0, 20(%a0)
	move.l %d1, 24(%a0)
	move.l %d2, 28(%a0)
	move.l %d3, 32(%a0)
	move.l %d4, 36(%a0)
	move.l %d5, 40(%a0)
	move.l %d6, 44(%a0)
	move.l %d7, 48(%a0)
	move.l (%a7), 52(%a0)
	move.l %a1, 56(%a0)
	move.l %a2, 60(%a0)
	move.l %a3, 64(%a0)
	move.l %a4, 68(%a0)
	move.l %a5, 72(%a0)
	move.l %a6, 76(%a0)
	move.l 12(%sp), 84(%a0)
	move.w 10(%sp), 88(%a0)
	move.l %a7, 80(%a0)
	move.l (%a7)+, %a0
	rts
	
**************************************************
** restore_pcb - Restore all registers, incl.   **
**              SR and PC, from current_process **
** REQUIRED:    Processor must be finished      **
**              dealing with some exception     **
**************************************************
restore_pcb:
	move.l %a0, -(%a7)
	move.l current_process, %a0
	move.l 80(%a0), %a7
	move.w 88(%a0), 10(%sp)
	move.l 84(%a0), 12(%sp)
	move.l 76(%a0), %a6
	move.l 72(%a0), %a5
	move.l 68(%a0), %a4
	move.l 64(%a0), %a3
	move.l 60(%a0), %a2
	move.l 56(%a0), %a1
	move.l 52(%a0), (%a7)
	move.l 48(%a0), %d7
	move.l 44(%a0), %d6
	move.l 40(%a0), %d5
	move.l 36(%a0), %d4
	move.l 32(%a0), %d3
	move.l 28(%a0), %d2
	move.l 24(%a0), %d1
	move.l 20(%a0), %d0
	move.l (%a7)+, %a0
	rts
