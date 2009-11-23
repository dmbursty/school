#include <uC++.h>
#include <iostream>
using namespace std;

const int MAX = 800000;
int shared = MAX;

_Task decrement {
    void main() {
	for ( int i = 1; i <= MAX; i += 1 ) {
	    shared -= 1;
	} // for
    } // decrement::main
  public:
    decrement() {}
}; // decrement

void uMain::main() {
    {
	decrement t[2];
    } // wait for tasks to finish
    cout << "shared:" << shared << endl;
} // uMain::main
