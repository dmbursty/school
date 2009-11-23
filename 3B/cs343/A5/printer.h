#ifndef __PRINTER_H
#define __PRINTER_H

#include <uC++.h>
#include "philosopher.h"

_Monitor Printer {
 public:
  Printer(unsigned int noOfPhil);
  ~Printer();
  void print(unsigned int id, Philosopher::States state);
  void print(unsigned int id, Philosopher::States state,
             unsigned int bite, unsigned int noodles);

 private:
  struct BufferSlot {
    BufferSlot() : set(false) {}
    Philosopher::States state;
    int bite;
    int noodles;
    bool set;
  };

  unsigned int noOfPhil;
  BufferSlot* buffer;

  void flush(int id);
  void finish(int id);
  void printOnlyMe(unsigned int id, Philosopher::States state,
                   unsigned int bite, unsigned int noodles);
};

#endif
