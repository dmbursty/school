#ifndef __TABLE_H
#define __TABLE_H

#include <uC++.h>
#include <uSemaphore.h>
#include "philosopher.h"

class Printer;

#if defined(TABLETYPE_SEM)
class Table {
 private:
  uSemaphore** forks;

#elif defined(TABLETYPE_INT)
_Monitor Table {
 private:
  uCondition** forks;
  bool* available;

#elif defined(TABLETYPE_AUTO)

#include "AutomaticSignal.h"

_Monitor Table {
 private:
  AUTOMATIC_SIGNAL
  bool* available;

#else
  #error unsupported table
#endif

 public:
  Table(unsigned int noOfPhil, Printer& prt);
  ~Table();
  void pickup(unsigned int id);
  void putdown(unsigned int id);

 private:
  unsigned int noOfPhil;
  Printer& prt;
};

#endif
