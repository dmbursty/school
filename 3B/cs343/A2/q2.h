#ifndef _Q2_H
#define _Q2_H

#include <uC++.h>

_Coroutine StringLiteral {
 public:
  enum status { CONT, MATCH, ERROR };
 private:
  status stat;
  char ch;
  void main();
 public:
  status next(char c) {
    ch = c;
    resume();
    return stat;
  }
};

#endif
