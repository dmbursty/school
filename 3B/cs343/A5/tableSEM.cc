#include <uC++.h>
#include <uSemaphore.h>
#include "table.h"
#include "printer.h"

Table::Table(unsigned int noOfPhil, Printer& prt)
  : noOfPhil(noOfPhil), prt(prt) {
  // Make fork locks
  philosophers = new uSemaphore*[noOfPhil];
  available = new bool[noOfPhil];
  for (unsigned int i = 0; i < noOfPhil; i++) {
    philosophers[i] = new uSemaphore(0);
  }
}

Table::~Table() {
  for (unsigned int i = 0; i < noOfPhil; i++) {
    delete philosophers[i];
  }
  delete philosophers;
  delete available;
}

void Table::pickup(unsigned int id) {
  tableLock.P();
  unsigned int idb = (id+1) % noOfPhil;
  // If either fork is unavailable, block
  if (!available[id] || !available[idb]) {
    prt.print(id, Philosopher::WAITING);
    tableLock.V();
    philosophers[id]->P();
    // We will only get signalled when another philosopher
    // has given us both forks
    return;
  } else {
    // If both are available, pick them up
    available[id] = false;
    available[idb] = false;
  }
  tableLock.V();
}

void Table::putdown(unsigned int id) {
  tableLock.P();
  // Far left and right forks (the other fork of the adjacent philosopher)
  int farLeft = (id == 0 ? noOfPhil - 1 : id - 1);
  int farRight = (id + 2) % noOfPhil;
  // My adjacent philosophers
  int pLeft = farLeft;
  int pRight = (id + 1) % noOfPhil;

  // If either adjacent philosopher would have both forks available at this
  // time, then pick up the other fork, and unblock that philosopher instead of
  // putting down my fork on that side
  if (available[farLeft] && philosophers[pLeft]->counter() < 0) {
    available[farLeft] = false;
    philosophers[pLeft]->V();
  } else {
    available[id] = true;
  }
  if (available[farRight] && philosophers[pRight]->counter() < 0) {
    available[farRight] = false;
    philosophers[pRight]->V();
  } else {
    available[pRight] = true;
  }
  tableLock.V();
}
