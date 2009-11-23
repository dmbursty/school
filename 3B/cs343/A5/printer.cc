#include "printer.h"
#include <iostream>

using std::cout;
using std::endl;

Printer::Printer(unsigned int noOfPhil) : noOfPhil(noOfPhil) {
  buffer = new BufferSlot[noOfPhil];
  for (int i = 0; i < noOfPhil; i++) {
    cout << "Phil" << i << "\t";
  }
  cout << endl;
  for (int i = 0; i < noOfPhil; i++) {
    cout << "******" << "\t";
  }
  cout << endl;
}

Printer::~Printer() {
  delete buffer;
  cout << "***********************" << endl;
  cout << "Philosophers terminated" << endl;
}

void Printer::print(unsigned int id, Philosopher::States state) {
  print(id, state, 0, 0);
}

void Printer::print(unsigned int id, Philosopher::States state,
                    unsigned int bite, unsigned int noodles) {
  static int last_flush = -1;
  static int last_id = -1;
  static int soloing = -1;
  // Special print on a finish
  if (state == Philosopher::FINISHED) {
    finish(id);
    buffer[id].set = false;
    return;
  }

  if (buffer[id].set) {
    if (id == last_flush && id == last_id) {
      if (id != soloing) {
        printOnlyMe(id, buffer[id].state, buffer[id].bite, buffer[id].noodles);
        soloing = id;
      }
      printOnlyMe(id, state, bite, noodles);
    } else {
      flush(id);
      soloing = -1;
      last_flush = id;
    }
  }
  buffer[id].set = true;
  buffer[id].state = state;
  buffer[id].bite = bite;
  buffer[id].noodles = noodles;
  last_id = id;
}

void Printer::flush(int id) {
  for (int i = 0; i < noOfPhil; i++) {
    if (!buffer[i].set) {
      cout << "\t";
      continue;
    }
    // Print State
    switch (buffer[i].state) {
      case Philosopher::HUNGRY:
        cout << "H"; break;
      case Philosopher::THINKING:
        cout << "T"; break;
      case Philosopher::EATING:
        cout << "E" << buffer[i].bite << "," << buffer[i].noodles; break;
      case Philosopher::WAITING:
        cout << "W" << i << "," << ((i + 1) % noOfPhil); break;
    }
    if (i == id) {
      cout << "*";
    }
    cout << "\t";
  }
  cout << endl;
}

void Printer::finish(int id) {
  for (int i = 0; i < noOfPhil; i++) {
    cout << (i == id ? "F\t" : "...\t");
  }
  cout << endl;
}

void Printer::printOnlyMe(unsigned int id, Philosopher::States state,
                          unsigned int bite, unsigned int noodles) {
  for (int i = 0; i < noOfPhil; i++) {
    if (i == id) {
      switch (state) {
        case Philosopher::HUNGRY:
          cout << "H"; break;
        case Philosopher::THINKING:
          cout << "T"; break;
        case Philosopher::EATING:
          cout << "E" << bite << "," << noodles; break;
        case Philosopher::WAITING:
          cout << "W" << i << "," << ((i + 1) % noOfPhil); break;
      }
      cout << "\t";
    } else {
      cout << "\t";
    }
  }
  cout << endl;
}
