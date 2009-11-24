#include <uC++.h>
#include <cstdlib>
#include <iostream>
#include "philosopher.h"
#include "table.h"
#include "printer.h"

void uMain::main() {
  const char* usage = "Usage: ./phil [ P [ N ] ], P = # philosophers >= 2, N = # noodles >= 1";
  // Parameters
  int noOfPhil = 5;
  int noodles = 30;

  // Read in arguments
  if (argc > 3) {
    std::cerr << usage << std::endl;
    exit(1);
  } else if (argc > 2) {
    noOfPhil = atoi(argv[1]);
    noodles = atoi(argv[2]);
  } else if (argc > 1) {
    noOfPhil = atoi(argv[1]);
  }

  if (noOfPhil <= 1 || noodles <= 0) {
    std::cerr << usage << std::endl;
    exit(1);
  }

  // Set random seed
  srand(time(NULL));

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
