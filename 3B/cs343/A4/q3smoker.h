#ifndef __SMOKER_H
#define __SMOKER_H

#include <uC++.h>

#define NUM_KINDS 3
const unsigned int NoOfKinds = NUM_KINDS;

class Printer;
template<unsigned int kinds> class Table;

_Task Smoker {
 public:
  enum States {Needs, Blocking, Smoking};
  Smoker(Table<NoOfKinds> &table, const unsigned int id, Printer& printer);

 protected:
  void main();

 private:
  Table<NoOfKinds>& table;
  const unsigned int id;
  Printer& printer;
};

#endif
