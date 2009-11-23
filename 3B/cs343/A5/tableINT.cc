#include "table.h"
#include "printer.h"

Table::Table(unsigned int noOfPhil, Printer& prt)
  : noOfPhil(noOfPhil), prt(prt) {
  // Make fork conditions
  forks = new uCondition*[noOfPhil];
  available = new bool[noOfPhil];
  for (unsigned int i = 0; i < noOfPhil; i++) {
    forks[i] = new uCondition();
    available[i] = true;
  }
}

Table::~Table() {
  for (unsigned int i = 0; i < noOfPhil; i++) {
    delete forks[i];
  }
  delete forks;
  delete available;
}

void Table::pickup(unsigned int id) {
  // The first philosopher is left handed!
  // This avoids deadlock
  if (id == 0) {
    bool printed = false;
    if (!available[0]) {
      prt.print(id, Philosopher::WAITING);
      printed = true;
      forks[0]->wait();
    }
    available[0] = false;

    if (!available[1]) {
      if (!printed) {
        prt.print(id, Philosopher::WAITING);
      }
      forks[1]->wait();
    }
    available[1] = false;
  } else {
    // The rest of the philosophers are right handed
    bool printed = false;
    if (!available[id]) {
      prt.print(id, Philosopher::WAITING);
      forks[id]->wait();
    }
    available[id] = false;

    if (!available[(id+1) % noOfPhil]) {
      if (!printed) {
        prt.print(id, Philosopher::WAITING);
      }
      forks[(id+1) % noOfPhil]->wait();
    }
    available[(id+1) % noOfPhil] = false;
  }
}

void Table::putdown(unsigned int id) {
  available[id] = true;
  available[(id+1) % noOfPhil] = true;
  forks[id]->signal();
  forks[(id+1) % noOfPhil]->signal();
}
