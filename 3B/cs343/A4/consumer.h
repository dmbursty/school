#ifndef __consumer_h_
#define __consumer_h_ 1

#include <uC++.h>
#include "q2buffer.h"

_Task Consumer {
    int id;
    BoundedBuffer<int>& buffer;
    int count;
    int delay;
    int& sum;
    unsigned int seed;
  protected:
    virtual void main();
  public:
    Consumer( int id, BoundedBuffer<int>& buffer, int count, int delay, int& sum, unsigned int seed = 0 );
};

#endif /* __consumer_h_ */
