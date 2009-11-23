#include "table.h"
#include "printer.h"

Table::Table(unsigned int noOfPhil, Printer& prt)
  : noOfPhil(noOfPhil), prt(prt) {
  // Make fork locks
  forks = new uSemaphore*[noOfPhil];
  for (unsigned int i = 0; i < noOfPhil; i++) {
    forks[i] = new uSemaphore();
  }
}

Table::~Table() {
  for (unsigned int i = 0; i < noOfPhil; i++) {
    delete forks[i];
  }
  delete forks;
}

void Table::pickup(unsigned int id) {
  // The first philosopher is left handed!
  // This avoids deadlock
  if (id == 0) {
    bool gotLFork = forks[0]->TryP();
    bool gotRFork = forks[1]->TryP();
    if (!gotLFork || !gotRFork) {
      prt.print(id, Philosopher::WAITING);
      if (!gotLFork) {
        forks[0]->P();
      }
      if (!gotRFork) {
        forks[1]->P();
      }
    }
  } else {
    // The rest of the philosophers are right handed
    bool gotRFork = forks[id]->TryP();
    bool gotLFork = forks[(id+1) % noOfPhil]->TryP();
    if (!gotLFork || !gotRFork) {
      prt.print(id, Philosopher::WAITING);
      if (!gotRFork) {
        forks[id]->P();
      }
      if (!gotLFork) {
        forks[(id+1) % noOfPhil]->P();
      }
    }
  }
}

void Table::putdown(unsigned int id) {
  forks[(id+1) % noOfPhil]->V();
  forks[id]->V();
}
