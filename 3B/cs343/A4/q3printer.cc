#include "q3printer.h"
#include "q3smoker.h"
#include <iostream>

using std::cout;
using std::endl;

Printer::Printer(unsigned int NoOfTasks)
 : numTasks(NoOfTasks), last_flush(-1), last(-1) {
  buffers = new BufferSlot[numTasks];

  for (int i = 0; i < numTasks; i++) {
    cout << "S" << i << "\t";
  }
  cout << endl;
}

Printer::~Printer() {
  flush();
  delete buffers;
}

void Printer::print(unsigned int id, Smoker::States state, unsigned int kind) {
  if (buffers[id].set) {
    if (last_flush == id && last == id) {
      flush();
    } else {
      flush(id);
      last_flush = id;
    }
  }
  last = id;
  buffers[id].set = true;
  buffers[id].state = state;
  buffers[id].kind = kind;
}

void Printer::flush(int id) {
  for (int i = 0; i < numTasks; i++) {
    if (!buffers[i].set) {
      cout << "\t";
      continue;
    }
    // Print State
    if (buffers[i].state == Smoker::Blocking) {
      cout << "B";
    } else {
      switch (buffers[i].state) {
        case Smoker::Needs:
          cout << "N "; break;
        case Smoker::Smoking:
          cout << "S "; break;
      }
      // Print kind
      switch (buffers[i].kind) {
        case 0:
          cout << "P"; break;
        case 1:
          cout << "T"; break;
        case 2:
          cout << "M"; break;
      }
    }
    if (i == id) {
      cout << "*";
    }
    cout << "\t";

    // Clear the buffer
    buffers[i].set = false;
  }
  cout << endl;
}
