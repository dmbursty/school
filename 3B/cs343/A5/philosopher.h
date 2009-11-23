#ifndef __PHILOSOPHER_H
#define __PHILOSOPHER_H

#include <uC++.h>

class Table;
class Printer;

_Task Philosopher {
 public:
  enum States {THINKING, HUNGRY, EATING, WAITING, FINISHED};
  Philosopher(unsigned int id, unsigned int noodles,
              Table& table, Printer& prt);

 private:
  void main();

  // Members
  unsigned int id;
  unsigned int noodles;
  Table& table;
  Printer& prt;
};

#endif
