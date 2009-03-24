/****************************************************************
 *
 *                           E&CE 354 RTX
 *                   (c)1998, All Rights Reserved
 *
 *                  Chris McKillop and  Craig Stout
 *                  [cdmckill,castout]@uwaterloo.ca
 *
 ****************************************************************/

/*  $Id: rtx_inc_5307.h,v 1.1.1.1 2007/11/12 22:00:24 yqhuang Exp $ */


#if !defined( RTX_BASE_H__ )
#define RTX_BASE_H__

/******************************************************************
 *                      CONSTANTS
 ******************************************************************/

/*
 * Data Types by Size
 */
#define SINT32  signed long int
#define UINT32  unsigned long int
#define SINT16  signed short int
#define UINT16  unsigned short int
#define SINT8   signed char
#define UINT8   unsigned char
#define CHAR    signed char
#define BYTE    unsigned char
#define VOID    void
#define BOOLEAN signed long int


#define ESC                 0x1B
#define BKSP                '\b'
#define CR                  '\r'
#define LF                  '\n'


#if !defined( TRUE )
#define TRUE 1
#endif

#if !defined( FALSE )
#define FALSE 0
#endif

#if !defined( NULL )
#define NULL 0
#endif


/*
 * Coldfire system defines
 */
#define RTX_COLDFIRE_MBAR    (BYTE *)(0xF0000000)
#define SIM_IMR              *( (UINT32 *)( RTX_COLDFIRE_MBAR + 0x044 ) )


/*
 * Coldfire Timer Defines
 */
#define TIMER0_TMR  *( (UINT16 *)( RTX_COLDFIRE_MBAR + 0x140 ) )
#define TIMER0_TRR  *( (UINT16 *)( RTX_COLDFIRE_MBAR + 0x144 ) )
#define TIMER0_TCN  *( (UINT16 *)( RTX_COLDFIRE_MBAR + 0x14C ) )
#define TIMER0_ICR  *( RTX_COLDFIRE_MBAR + 0x04D )
#define TIMER0_TER  *( RTX_COLDFIRE_MBAR + 0x151 ) 

#define TIMER1_TMR  *( (UINT16 *)( RTX_COLDFIRE_MBAR + 0x180 ) )
#define TIMER1_TRR  *( (UINT16 *)( RTX_COLDFIRE_MBAR + 0x184 ) )
#define TIMER1_TCN  *( (UINT16 *)( RTX_COLDFIRE_MBAR + 0x18C ) )
#define TIMER1_ICR  *( RTX_COLDFIRE_MBAR + 0x04E )
#define TIMER1_TER  *( RTX_COLDFIRE_MBAR + 0x191 ) 

/*
 * Coldfire Serial Defines
 */
#define SERIAL0_UCR     *( RTX_COLDFIRE_MBAR + 0x1C8 )
#define SERIAL0_UBG1    *( RTX_COLDFIRE_MBAR + 0x1D8 )
#define SERIAL0_UBG2    *( RTX_COLDFIRE_MBAR + 0x1DC )
#define SERIAL0_UDU	SERIAL0_UBG1
#define SERIAL0_UDL	SERIAL0_UBG2
#define SERIAL0_UCSR    *( RTX_COLDFIRE_MBAR + 0x1C4 )
#define SERIAL0_UMR     *( RTX_COLDFIRE_MBAR + 0x1C0 )
#define SERIAL0_ICR     *( RTX_COLDFIRE_MBAR + 0x050 )
#define SERIAL0_IVR     *( RTX_COLDFIRE_MBAR + 0x1F0 )
#define SERIAL0_ISR     *( RTX_COLDFIRE_MBAR + 0x1D4 )
#define SERIAL0_IMR     *( RTX_COLDFIRE_MBAR + 0x1D4 )
#define SERIAL0_RD      *( RTX_COLDFIRE_MBAR + 0x1CC )
#define SERIAL0_WD      *( RTX_COLDFIRE_MBAR + 0x1CC )

#define SERIAL1_UCR     *( RTX_COLDFIRE_MBAR + 0x208 )
#define SERIAL1_UBG1    *( RTX_COLDFIRE_MBAR + 0x218 )
#define SERIAL1_UBG2    *( RTX_COLDFIRE_MBAR + 0x21C )
#define SERIAL1_UDU	SERIAL1_UBG1
#define SERIAL1_UDL	SERIAL1_UBG2
#define SERIAL1_UCSR    *( RTX_COLDFIRE_MBAR + 0x204 )
#define SERIAL1_UMR     *( RTX_COLDFIRE_MBAR + 0x200 )
#define SERIAL1_ICR     *( RTX_COLDFIRE_MBAR + 0x051 )
#define SERIAL1_IVR     *( RTX_COLDFIRE_MBAR + 0x230 )
#define SERIAL1_ISR     *( RTX_COLDFIRE_MBAR + 0x214 )
#define SERIAL1_IMR     *( RTX_COLDFIRE_MBAR + 0x214 )
#define SERIAL1_RD      *( RTX_COLDFIRE_MBAR + 0x20C )
#define SERIAL1_WD      *( RTX_COLDFIRE_MBAR + 0x20C )

/*
 * RTX Error Codes
 */
#define RTX_SUCCESS 0
#define RTX_ERROR   -1

/* added by HYQ */
#define    size_t          unsigned long

void atomic (BOOLEAN on);

#endif

