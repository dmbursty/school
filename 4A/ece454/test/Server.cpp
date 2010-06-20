#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include "../src/ServerStub.h"

int sum(int* argTypes, void** args) {
  int len = argTypes[1] & 0xffff;
  int* vector = (int*)(args[1]);

  int sum = 0;
  for (int i = 0; i < len; i++) {
    sum += vector[i];
  }

  *((int*)args[0]) = sum;
  return 0;
}

int main()
{
  std::cout << "Server" << std::endl;

  int argTypes[] = {
      ARG_INPUT | ARG_INT << 16, // result
      ARG_INPUT | ARG_INT | 20, // input
     0, // terminates the array
  };

  // Register the function with the binder
  rpcRegister((char*) "sum", argTypes, sum );
  // TODO: Check error code!

  // Listen for requests
  rpcExecute();
  // TODO: Check error code!

  return 0;
}

