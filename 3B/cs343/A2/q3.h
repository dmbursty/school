#ifndef _Q3_H
#define _Q3_H

#include <uC++.h>

_Coroutine merge {
 public:
  merge() {}
  void start(merge* partner, int sortValues[], int mergedValues[]);
  void mergeTo(int limit, int nextPos);
 private:
  void main();

  merge* partner;
  int* list;
  int* out;

  int limit;
  int nextPos;
};

#endif
