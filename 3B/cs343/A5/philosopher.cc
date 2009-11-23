#include "philosopher.h"
#include "table.h"
#include "printer.h"

Philosopher::Philosopher(unsigned int id, unsigned int noodles,
                         Table& table, Printer& prt)
  : id(id), noodles(noodles), table(table), prt(prt) {}

void Philosopher::main() {
  while (true) {
    // Start Hungry
    prt.print(id, HUNGRY);
    yield(rand() % 5);

    // Acquire forks
    table.pickup(id);

    // Eat
    unsigned int bite = (rand() % 5) + 1;
    if (bite > noodles) bite = noodles;
    noodles -= bite;
    prt.print(id, EATING, bite, noodles);
    yield(rand() % 5);

    // Put down forks
    table.putdown(id);

    // Check if done noodles
    if (noodles <= 0) break;

    // Think
    prt.print(id, THINKING);
    yield(rand() % 20);
  }

  // Terminate
  prt.print(id, FINISHED);
}
