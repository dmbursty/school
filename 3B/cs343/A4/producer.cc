#include "producer.h"
#include <cstdlib>

Producer::Producer( int id, BoundedBuffer<int>& buffer, int count, int range,
	int delay, unsigned int seed )
    : id(id), buffer(buffer), count(count), range(range), delay(delay),
	seed(seed) {}

void Producer::main() {
    for ( int i = 0; i < count; i += 1 ) {
	buffer.insert( rand_r( &seed ) % range, id );
	yield( rand_r( &seed ) % delay );
    }
}
