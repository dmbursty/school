#include <malloc.h>
#include <iostream>
#include <string.h>
#include "../src/ServerStub.h"

#define PARAMETER_COUNT 255

////////////////////////////////////////////////////////////////////////////////////////////
//
// This server implements the following procedures:
//

int displayParms (int *argTypes, void **args);
int copyChar     (int *argTypes, void **args);


////////////////////////////////////////////////////////////////////////////////////////////
//
// This server procedure simply displays parameters that are sent to it by the client
//

int displayParms (int *argTypes, void **args) {

  std::cerr << "You have entered the server displayParms" << std::endl;
  
  for (int i = 0; argTypes[i] != 0; ++i) {

    int arrayLength = argTypes[i] & 0xFFFF;

    switch ((argTypes[i] & 0xFF0000)) {
    case ARG_CHAR :
      std::cerr << "Argument " << i << " is char with value: " << *(char *)args[i] << std::endl;
      if (arrayLength) {
        std::cerr << "Argument is an array of length " << arrayLength << std::endl;
        std::cerr << "Last value of array is " << ((char *)args[i])[arrayLength - 1] << std::endl;
        for (int j = 0; j < arrayLength; j++) ((char*)args[i])[j] = 'x';
      } else {
        *(char *)args[i] = 'y';
      }
      break;
      
    case ARG_INT :
      std::cerr << "Argument " << i << " is int with value: " << *(int *)args[i] << std::endl;
      if (arrayLength) {
        std::cerr << "Argument is an array of length " << arrayLength << std::endl;
        std::cerr << "Last value of array is " << ((int *)args[i])[arrayLength - 1] << std::endl;
        for (int j = 0; j < arrayLength; j++) ((int*)args[i])[j] = 42;
      } else {
        *(int*)args[i] = 1337;
      }
      break;
    case ARG_STRING :
	if (!arrayLength) {
	    std::cerr << "Argument " << i << " is string with value: " << (char *)args[i] << std::endl;
            args[i] = (void*)"beans!";
	}
	else {
	    std::cerr << "Argument is an array of length " << arrayLength << std::endl;
	    std::cerr << "First value of array is " << ((char**)args[i])[0] << std::endl;
	    std::cerr << "Last value of array is " << ((char**)args[i])[arrayLength - 1] << std::endl;
            for (int j = 0; j < arrayLength; j++) {
              void*** v_args = (void***)args;
              v_args[i][j] = (void*)"stringy";
            }
	}
      break;
      
    default :
      std::cerr << "Argument " << i << " is of unknown type" << std::endl;
      break;
    }
    
  }
  return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////
//
//  Procedure copyChar expects the argument types to be two char's.
//  Failure to do so will result in it returning a -1.
//  It copies the second char to the first char.
//

int copyChar (int *argTypes, void **args) {

  if ((argTypes[0] != (ARG_CHAR | ARG_OUTPUT))  ||
      (argTypes[1] != (ARG_CHAR | ARG_INPUT))) {
    return -1;
  }

  *(char *)args[0] = *(char *)args[1];   // dest = src;
  return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////
//
//  Main simply registers the procedures and then enters the
//  stub execute mode.
//

int main ( void ) {

  int argTypes[PARAMETER_COUNT+1];
  int result = 0;

  void **args = (void **)malloc(PARAMETER_COUNT  * sizeof(void *));

  // copyChar(dest, src);

  argTypes[0] = ARG_CHAR | ARG_OUTPUT;       // dest
  argTypes[1] = ARG_CHAR | ARG_INPUT;        // src
  argTypes[2] = 0;                         // Terminator

  result = rpcRegister("copyChar", argTypes, &copyChar);

  if (result < 0) 
    std::cerr << "rpcRegister generated an error: " << result << std::endl;
  else if (result > 0)
    std::cerr << "rpcRegister generated a warning: " << result << std::endl;


  // displayParms(...)
  argTypes[0] = 0;

  result = rpcRegister("displayParms", argTypes, &displayParms);

  if (result < 0) 
    std::cerr << "rpcRegister generated an error: " << result << std::endl;
  else if (result > 0)
    std::cerr << "rpcRegister generated a warning: " << result << std::endl;


  // And now hand control over to the the server stub

  result = rpcExecute();

  // Someone terminated the server stub.  Let's print the result and exit

  std::cerr << "Server was terminated with result: " << result << std::endl;

  return result;
}
