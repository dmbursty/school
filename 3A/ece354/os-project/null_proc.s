	.file	"null_proc.c"
gcc2_compiled.:
.text
.globl null_proc
	.type	 null_proc,@function
null_proc:
	link.w %a6,#0
	nop
.L3:
	jbra .L5
	jbra .L4
.L5:
	jbra .L3
.L4:
.L2:
	unlk %a6
	rts
.Lfe1:
	.size	 null_proc,.Lfe1-null_proc
	.ident	"GCC: (GNU) 2.95.3 20010315 (release)(ColdFire patches - 20010318 from http://fiddes.net/coldfire/)"
