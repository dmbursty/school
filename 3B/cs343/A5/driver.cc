#include <uC++.h>
#include <cstdlib>
#include "philosopher.h"
#include "table.h"
#include "printer.h"

void uMain::main() {
  // Set random seed
  srand(time(NULL));
  srand(0);

  // Constants
  unsigned int noOfPhil = 5;
  unsigned int noodles = 10;

  // Make Printer, Table
  Printer prt(noOfPhil);
  Table table(noOfPhil, prt);

  // Make Philosophers
  Philosopher* philosophers[noOfPhil];

  for (unsigned int i = 0; i < noOfPhil; i++) {
    philosophers[i] = new Philosopher(i, noodles, table, prt);
  }

  // Wait on philosophers
  for (unsigned int i = 0; i < noOfPhil; i++) {
    delete philosophers[i];
  }
}
