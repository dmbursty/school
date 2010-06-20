#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include "../src/ClientStub.h"

int main() {
  std::cout << "Client" << std::endl;

  // result = sum(vector);
  const int PARAMETER_COUNT = 3;      // Number of RPC arguments
  const int LENGTH = 23;              // Vector length
  int result = 0;
  int vector[LENGTH];

  for (int i = 0; i < LENGTH; i++) {
    vector[i] = i;
  }

  char * strings[10];
  for (int i = 0; i < 10; i++) {
    strings[i] = new char[10];
    sprintf(strings[i], "string %d", i);
  }

  const char * word = "0123456";

  int argTypes[PARAMETER_COUNT+1];
  void **args = (void **) malloc(PARAMETER_COUNT  * sizeof(void *));

  argTypes[0] = ARG_OUTPUT | ARG_INT ;    // result
  argTypes[1] = ARG_INPUT | ARG_INT | LENGTH;  // vector
  argTypes[2] = ARG_INPUT | ARG_OUTPUT | ARG_STRING | 10;
  argTypes[3] = 0;                             // Terminator

  args[0] = (void *) &result;
  args[1] = (void *) vector;      // Note that "vector" is the address
  args[2] = (void *) strings;      // Note that "vector" is the address
  
  int ec = rpcCall((char*)"sum", argTypes, args);
  if (ec == 0) { // Successful execution
     printf("Sum is %d\n", result);
  } else {
     printf("Error: %d\n", ec);
  }

  for (int i = 0; i < 10; i++) {
    delete[] strings[i];
  }

  rpcTerminate();
  return 0;
}
