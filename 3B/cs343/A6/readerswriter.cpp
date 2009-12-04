#include "readerswriter.h"

ReadersWriter::ReadersWriter() : rcnt(0), wcnt(0) {}

void ReadersWriter::endRead() {
  rcnt--;
}

void ReadersWriter::endWrite() {
  wcnt = 0;
}

void ReadersWriter::startRead() {
  if (wcnt > 0) _Accept(endWrite);
  rcnt++;
}

void ReadersWriter::startWrite() {
  if (wcnt > 0) { _Accept(endWrite); }
  else while (rcnt > 0) _Accept(endRead);
  wcnt = 1;
}
