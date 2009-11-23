#include <uC++.h>

enum Intent { WantIn, DontWantIn };

uBaseTask *CurrTid;					// current task id

void CriticalSection() {
    CurrTid = &uThisTask();
    for ( int i = 1; i <= 100; i += 1 ) {		// delay
	if ( CurrTid != &uThisTask() ) {
	    uAbort( "interference" );
	} // if
    } // for
}

_Task Kessels {
    int who;
    Intent &me, &you;
    int &Last1, &Last2;
    void main() {
	for ( int i = 1; i <= 10000; i += 1 ) {
	    yield();
	    me = WantIn;				// entry protocol
	    Last1 = ( Last2 + who ) % 2;		// race
	    while ( you == WantIn && Last1 == ( Last2 + who ) % 2 ) {}
	    CriticalSection();				// critical section
	    me = DontWantIn;				// exit protocol
	}
    }
  public:
    Kessels( int who, Intent &me, Intent &you, int &Last1, int &Last2 ) :
	who(who), me(me), you(you), Last1(Last1), Last2(Last2) {}
};

void uMain::main() {
    Intent me = DontWantIn, you = DontWantIn;		// shared
    int Last1 = 1, Last2 = 1;
    srand( time( NULL ) );
    Kessels t0( 0, me, you, Last1, Last2), t1(1, you, me, Last2, Last1 );
}
