#include "q3smoker.h"
#include "q3printer.h"
#include "q3table.h"
#include <cstdlib>

Smoker::Smoker(Table<NoOfKinds> &table, const unsigned int id, Printer& printer)
 : table(table), id(id), printer(printer) {}

void Smoker::main() {
  unsigned int seed = (time(NULL) ^ id) * 12345;

  int kind = rand_r(&seed) % 3;
  int numCig = rand_r(&seed) % 20;

  for (int i = 0; i < numCig; i++) {
    // Pick up from table
    printer.print(id, Needs, kind);
    table.acquire(id, kind);
    yield(rand_r(&seed) % 3);

    // Put back and smoke
    table.release(id, kind);
    printer.print(id, Smoking, kind);
    yield(rand_r(&seed) % 10);
  }
}
