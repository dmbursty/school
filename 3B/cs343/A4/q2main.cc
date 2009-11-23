#include <uC++.h>
#include "producer.h"
#include "consumer.h"
#include <iostream>
using namespace std;

void uMain::main() {
    BoundedBuffer<int> buffer(5);
    Producer* prod[5];
    Consumer* cons[5];
    int sums[5];
    for ( int i = 0; i < 5; i += 1 ) {
	sums[i] = 0;
	cons[i] = new Consumer(i, buffer, 20, 10000, sums[i], i);
	prod[i] = new Producer(i, buffer, 20, 100, 10000, i);
    }
    int sum = 0;
    for ( int i = 0; i < 5; i += 1 ) {
	delete prod[i];
	delete cons[i];
	sum += sums[i];
    }
    cout << "total production: " << sum << endl;
}
