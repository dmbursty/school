#include <cstdlib>          // atoi
#include <iostream>
using namespace std;

int main( int argc, char *argv[] ) {
  unsigned int times = 100000;
  switch ( argc ) {
    case 2:
     times = atoi( argv[1] );
  } // switch

  string s( "abcdefghijklmnopqrstuvwxyz" );
  for ( unsigned int i = 0; i < times; i += 1 ) {
#ifdef SS
    cout << "s:"  + s  + "s:"  + s  + " s:"  + s  + "\n"; // Java style
#else
    cout << "s:" << s << "s:" << s << " s:" << s << endl; // C++ style
#endif
  }
}
