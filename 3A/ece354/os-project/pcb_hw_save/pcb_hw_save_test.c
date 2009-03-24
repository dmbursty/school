#include "pcb.h"
#include "rtx_inc.h"

PCB* current_process;
PCB proc;
PCB end_proc;
UINT32 counter;

void asm_timer_entry (void); //defined in pcb_hw_save.s

int __main (void) { return 0; }

VOID rtx_dbug_out_char( CHAR c )
{
     /* Store registers */
    asm( "move.l %d0, -(%a7)" );
    asm( "move.l %d1, -(%a7)" );

    /* Load CHAR c into d1 */
    asm( "move.l 8(%a6), %d1" );

    /* Setup trap function */
    asm( "move.l #0x13, %d0" );
    asm( "trap #15" );

     /* Restore registers  */
    asm(" move.l %d1, (%a7)+" );
    asm(" move.l %d0, (%a7)+" );
}

void shutdown (void)
{
	rtx_dbug_out_char('\r');
	rtx_dbug_out_char('\n');
	rtx_dbug_out_char('S');
	rtx_dbug_out_char('h');
	rtx_dbug_out_char('u');
	rtx_dbug_out_char('t');
	rtx_dbug_out_char(' ');
	rtx_dbug_out_char('d');
	rtx_dbug_out_char('o');
	rtx_dbug_out_char('w');
	rtx_dbug_out_char('n');
	rtx_dbug_out_char('\r');
	rtx_dbug_out_char('\n');
	asm ("move.l #0, %d0");
	asm ("trap #15");
}

int main (void)
{
	UINT32 mask;
	int i;
	
	counter = 0;
	
	//Disable all interrupts
	asm ("move.w #0x2700,%sr");

	//Move VBR into real memory
	asm( "move.l %a0, -(%a7)" );
    asm( "move.l #0x10000000, %a0 " );
    asm( "movec.l %a0, %vbr" );
    asm( "move.l (%a7)+, %a0" );

	//Store timer ISR at auto-vector #6
	asm ("move.l #asm_timer_entry,%d0");
	asm ("move.l %d0, 0x10000078");

	//Setup to use auto-vectored interrupt level 6, priority 3
	TIMER0_ICR = 0x9b;

	//Set the reference counts, 1.001 ms
	TIMER0_TRR = 176;

	//Setup the timer prescalar and stuff
	TIMER0_TMR = 0xff1b;

	//Set the interrupt mask
	mask = SIM_IMR;
	SIM_IMR &= 0x0003fdff;
	SIM_IMR = mask;

	rtx_dbug_out_char('-');
	rtx_dbug_out_char('-');
	rtx_dbug_out_char('-');
	rtx_dbug_out_char('\r');
	rtx_dbug_out_char('\n');
	
	//set up PCB
	current_process = &proc;
	
	end_proc.pc = (int)&shutdown;
	end_proc.sp = 0x0000;
	
	//Re-enable interrupts
	asm ("move.w #0x2000,%sr");
	
	//Run infinite loop
	while (1)
	{
		rtx_dbug_out_char('.');
		for (i = 7; i < 8; i++)
		{
			//end_proc.data_reg[i] = proc.data_reg[i];
			end_proc.add_reg[i] = proc.add_reg[i];
		}
		//end_proc.sp = proc.sp;
		end_proc.stack_size[0] = proc.stack_size[0];
		end_proc.stack_size[1] = proc.stack_size[1];
	}
}

void asm_timer_isr (void)
{
	int print = (counter % 1000 == 0);
	if (print) rtx_dbug_out_char('X');
	if (print) rtx_dbug_out_char((current_process->pc)%10+0x30);
	counter++;
	if (print) rtx_dbug_out_char((current_process->pc)%10+0x30);
	if (print) rtx_dbug_out_char('X');
	if (print) rtx_dbug_out_char('\r');
	if (print) rtx_dbug_out_char('\n');
	if (counter > 5000) current_process = &end_proc;
	TIMER0_TER = 2;
}