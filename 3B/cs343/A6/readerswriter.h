#ifndef _READERS_WRITER_H_
#define _READERS_WRITER_H_

#include <uC++.h>

// Taken from course notes pg 116
_Monitor ReadersWriter {
 public:
  ReadersWriter();
  void endRead();
  void endWrite();
  void startRead();
  void startWrite();

 private:
  int rcnt, wcnt;
};

#endif
