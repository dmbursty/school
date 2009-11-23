#ifndef __TABLE_H
#define __TABLE_H

#include <uSemaphore.h>
#include <iostream>
#include "q3smoker.h"
#include "q3printer.h"

template<unsigned int kinds> class Table {
 public:
  Table(Printer& printer) : printer(printer) {}

  void acquire(unsigned int id, unsigned int kind) {
    // Validate kind
    if (kind < 0 || kind >= kinds) {
      std::cerr << "Invalid kind specified in acquire" << std::endl;
      exit(1);
    }

    if (!locks[kind].TryP()) {
      printer.print(id, Smoker::Blocking, kind);
      locks[kind].P();
    }
  }

  void release(unsigned int id, unsigned int kind) {
    // Validate kind
    if (kind < 0 || kind >= kinds) {
      std::cerr << "Invalid kind specified in release" << std::endl;
      exit(1);
    }

    locks[kind].V();
  }

 private:
  Printer& printer;
  uSemaphore locks[kinds];
};

#endif
