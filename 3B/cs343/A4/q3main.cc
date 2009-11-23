#include <uC++.h>
#include <iostream>
#include "q3smoker.h"
#include "q3table.h"
#include "q3printer.h"

void uMain::main() {
  const char* usage = "Usage: ./q3_test [ no-of-smokers (1-10) ]";
  int numSmokers = 5;
  if (argc > 2) {
    std::cerr << usage << std::endl;
    exit(1);
  } else if (argc == 2) {
    numSmokers = atoi(argv[1]);
    if (numSmokers < 1 || numSmokers > 10) {
      std::cerr << usage << std::endl;
      exit(1);
    }
  }

  Printer printer(numSmokers);
  Table<NoOfKinds> table(printer);

  Smoker* smokers[numSmokers];

  for (int i = 0; i < numSmokers; i++) {
    smokers[i] = new Smoker(table, i, printer);
  }

  for (int i = 0; i < numSmokers; i++) {
    delete smokers[i];
  }
}
