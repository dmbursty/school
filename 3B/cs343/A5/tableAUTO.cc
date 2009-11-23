#include "table.h"
#include "printer.h"

Table::Table(unsigned int noOfPhil, Printer& prt)
  : noOfPhil(noOfPhil), prt(prt) {
  // Make fork conditions
  available = new bool[noOfPhil];
  for (unsigned int i = 0; i < noOfPhil; i++) {
    available[i] = true;
  }
}

Table::~Table() {
  delete available;
}

void Table::pickup(unsigned int id) {
  WAITUNTIL(available[id] && available[(id+1) % noOfPhil],
            prt.print(id, Philosopher::WAITING), )
  available[id] = false;
  available[(id+1) % noOfPhil] = false;
  RETURN()
}

void Table::putdown(unsigned int id) {
  available[id] = true;
  available[(id+1) % noOfPhil] = true;
  RETURN()
}
