#ifndef __producer_h_
#define __producer_h_ 1

#include <uC++.h>
#include "q2buffer.h"

_Task Producer {
    int id;
    BoundedBuffer<int>& buffer;
    int count;
    int range;
    int delay;
    unsigned int seed;
  protected:
    virtual void main();
  public:
    Producer( int id, BoundedBuffer<int>& buffer, int count, int range, int delay, unsigned int seed = 0 );
};

#endif /* __producer_h_ */
