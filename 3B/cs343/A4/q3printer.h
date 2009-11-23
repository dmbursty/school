#ifndef __PRINTER_H
#define __PRINTER_H

#include <uC++.h>
#include "q3smoker.h"

_Monitor Printer {
 public:
  Printer(unsigned int NoOfTasks);
  ~Printer();
  void print(unsigned int id, Smoker::States state, unsigned int kind);

 private:
  void flush(int id = -1);

  unsigned int numTasks;
  int last_flush;
  int last;

  struct BufferSlot {
    BufferSlot() : set(false) {}
    unsigned int kind;
    Smoker::States state;
    bool set;
  };

  BufferSlot* buffers;
};

#endif
