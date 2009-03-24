	.file	"pcb.c"
gcc2_compiled.:
.text
.globl pcb_init
	.type	 pcb_init,@function
pcb_init:
	link.w %a6,#-4
	move.l %d3,-(%sp)
	move.l %d2,-(%sp)
	clr.l -4(%a6)
#APP
	move.l %d0, -(%a7)
	move.l #proc_stack, %d0
	move.l %d0, mem_counter
	move.l (%a7)+, %d0
#NO_APP
.L3:
	moveq.l #5,%d0
	cmp.l -4(%a6),%d0
	jbge .L5
	jbra .L4
.L5:
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	add.l %d0,%d1
	move.l %d1,%d2
	lsl.l #5,%d2
	add.l %d2,%d1
	add.l %d1,%d0
	lea pcb_map,%a0
	move.l -4(%a6),%d1
	move.l %d1,%d3
	move.l %d3,%d2
	lsl.l #3,%d2
	sub.l %d1,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	lea RTXEVAL_TEST_PROCS,%a1
	move.l (%a1,%d1.l),%d2
	move.l %d2,(%a0,%d0.l)
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	add.l %d0,%d1
	move.l %d1,%d2
	lsl.l #5,%d2
	add.l %d2,%d1
	add.l %d1,%d0
	lea pcb_map+4,%a0
	move.l -4(%a6),%d1
	move.l %d1,%d3
	move.l %d3,%d2
	lsl.l #3,%d2
	sub.l %d1,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	lea RTXEVAL_TEST_PROCS,%a1
	clr.l %d2
	move.b 4(%a1,%d1.l),%d2
	move.l %d2,(%a0,%d0.l)
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	add.l %d0,%d1
	move.l %d1,%d2
	lsl.l #5,%d2
	add.l %d2,%d1
	add.l %d1,%d0
	lea pcb_map+88,%a0
	clr.l (%a0,%d0.l)
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	add.l %d0,%d1
	move.l %d1,%d2
	lsl.l #5,%d2
	add.l %d2,%d1
	add.l %d1,%d0
	lea pcb_map+84,%a0
	move.l -4(%a6),%d1
	move.l %d1,%d3
	move.l %d3,%d2
	lsl.l #3,%d2
	sub.l %d1,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	lea RTXEVAL_TEST_PROCS,%a1
	move.l 10(%a1,%d1.l),%d2
	move.l %d2,(%a0,%d0.l)
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	add.l %d0,%d1
	move.l %d1,%d2
	lsl.l #5,%d2
	add.l %d2,%d1
	add.l %d1,%d0
	lea pcb_map+92,%a0
	move.l mem_counter,%d1
	move.l %d1,(%a0,%d0.l)
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	lsl.l #3,%d1
	sub.l %d0,%d1
	move.l %d1,%d0
	add.l %d0,%d0
	lea RTXEVAL_TEST_PROCS,%a0
	move.l 6(%a0,%d0.l),%d2
	add.l %d2,mem_counter
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	add.l %d0,%d1
	move.l %d1,%d2
	lsl.l #5,%d2
	add.l %d2,%d1
	add.l %d1,%d0
	lea pcb_map+96,%a0
	move.l mem_counter,%d1
	move.l %d1,(%a0,%d0.l)
	move.l -4(%a6),%d0
	move.l %d0,%d2
	move.l %d2,%d1
	add.l %d1,%d1
	add.l %d0,%d1
	move.l %d1,%d2
	lsl.l #5,%d2
	add.l %d2,%d1
	add.l %d1,%d0
	lea pcb_map+80,%a0
	moveq.l #-12,%d2
	add.l mem_counter,%d2
	move.l %d2,(%a0,%d0.l)
	moveq.l #1,%d0
	add.l %d0,-4(%a6)
	jbra .L3
.L4:
	move.l mem_counter,%d1
	move.l %d1,%d0
	jbra .L2
.L2:
	move.l (%sp)+,%d2
	move.l (%sp)+,%d3
	unlk %a6
	rts
.Lfe1:
	.size	 pcb_init,.Lfe1-pcb_init
	.comm	current_process,4,2
	.comm	pcb_map,600,2
	.comm	message_blocked_processes,4,2
	.comm	msg_header_blocked_processes,20,2
	.comm	uart_blocked_processes,4,2
	.comm	memory_blocked_processes,20,2
	.comm	ready_processes,20,2
	.comm	mem_counter,4,2
	.ident	"GCC: (GNU) 2.95.3 20010315 (release)(ColdFire patches - 20010318 from http://fiddes.net/coldfire/)"
