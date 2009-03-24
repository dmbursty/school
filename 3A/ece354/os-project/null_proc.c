#include "rtx_inc.h"
#include "null_proc.h"

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

void null_proc ()
{
	//while (1) rtx_dbug_out_char('.');
	rtx_dbug_out_char('W');
	rtx_dbug_out_char('E');
	rtx_dbug_out_char(' ');
	rtx_dbug_out_char('A');
	rtx_dbug_out_char('R');
	rtx_dbug_out_char('E');
	rtx_dbug_out_char(' ');
	rtx_dbug_out_char('W');
	rtx_dbug_out_char('O');
	rtx_dbug_out_char('R');
	rtx_dbug_out_char('K');
	rtx_dbug_out_char('I');
	rtx_dbug_out_char('N');
	rtx_dbug_out_char('G');
	rtx_dbug_out_char('!');
	rtx_dbug_out_char('!');
	rtx_dbug_out_char('!');
	rtx_dbug_out_char('\r');
	rtx_dbug_out_char('\n');
	asm ("clr.l %d0");
	asm ("trap #15");
}