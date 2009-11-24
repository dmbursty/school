#include "printer.h"
#include <iostream>

using std::cout;
using std::endl;

Printer::Printer(unsigned int noOfPhil) : noOfPhil(noOfPhil) {
  buffer = new BufferSlot[noOfPhil];
  for (unsigned int i = 0; i < noOfPhil; i++) {
    cout << "Phil" << i << "\t";
  }
  cout << endl;
  for (unsigned int i = 0; i < noOfPhil; i++) {
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
  // Validate that current state is valid
  errorCheck();

  // Special print on a finish
  if (state == Philosopher::FINISHED) {
    if (last_flush != id || last_id != id) {
      flush(id);
    }
    finish(id);
    buffer[id].state = state;
    buffer[id].set = false;
    last_flush = id;
    last_id = id;
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

void Printer::flush(unsigned int id) {
  for (unsigned int i = 0; i < noOfPhil; i++) {
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
      case Philosopher::FINISHED:
        break;  // Should never happen
    }
    if (i == id) {
      cout << "*";
    }
    cout << "\t";
  }
  cout << endl;
}

void Printer::finish(unsigned int id) {
  for (unsigned int i = 0; i < noOfPhil; i++) {
    cout << (i == id ? "F\t" : "...\t");
  }
  cout << endl;
}

void Printer::printOnlyMe(unsigned int id, Philosopher::States state,
                          unsigned int bite, unsigned int noodles) {
  for (unsigned int i = 0; i < noOfPhil; i++) {
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
        case Philosopher::FINISHED:
          break;  // Should never happen
      }
      cout << "\t";
    } else {
      cout << "\t";
    }
  }
  cout << endl;
}

void Printer::errorCheck() {
  for (unsigned int i = 0; i < noOfPhil; i++) {
    unsigned int left = (i == 0 ? noOfPhil - 1 : i - 1);
    unsigned int right = (i+1) % noOfPhil;
    switch (buffer[i].state) {
      case Philosopher::HUNGRY:
      case Philosopher::THINKING:
        break;  // No restrictions
      case Philosopher::EATING:
        assert(buffer[left].state != Philosopher::EATING);
        assert(buffer[right].state != Philosopher::EATING);
        break;
      case Philosopher::WAITING:
        break;
      case Philosopher::FINISHED:
        assert(!buffer[i].set);
        break;
    }
  }
}
