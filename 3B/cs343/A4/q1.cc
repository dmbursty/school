#include <uC++.h>

#if defined( V1 )
class
#elif defined( V2 )
_Coroutine
#elif defined( V3 )
_Task
#endif
CallCxtSw {
	unsigned int times;
	volatile unsigned int i;		// ignore, force call to rtn

	void rtn();
	void main();
  public:
	CallCxtSw( int times ) : times( times ), i( 0 ) {
#if defined( V1 )
		main();
#elif defined( V2 )
		resume();
#endif
	}
};

void CallCxtSw::main() {
	for ( int i = 1; i <= times; i += 1 ) {
#if defined( V1 )
		rtn();
#elif defined( V2 )
		resume();
#elif defined( V3 )
		uYieldNoPoll();
#endif
	} // for
}
void CallCxtSw::rtn() { i += 1; }

void uMain::main() {
	unsigned int times = 10000000;
	switch ( argc ) {
	  case 2:
		times = atoi( argv[1] );
	} // switch

	CallCxtSw obj( times );
}
