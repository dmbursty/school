#include "consumer.h"
#include <cstdlib>

Consumer::Consumer( int id, BoundedBuffer<int>& buffer, int count, int delay,
	int& sum, unsigned int seed )
    : id(id), buffer(buffer), count(count), delay(delay), sum(sum),
	seed(seed) {}
  
void Consumer::main() {
    for ( int i = 0; i < count; i += 1 ) {
	sum += buffer.remove( id );
	yield( rand_r( &seed ) % delay );
    }
}
