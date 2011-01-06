#include <unistd.h>
#include <malloc.h>
#include <iostream>
#include <string.h>
#include "../src/ClientStub.h"

#define PARAMETER_COUNT 255

int main ( void ) {

  int argTypes[PARAMETER_COUNT+1];
  void **args = (void **)malloc(PARAMETER_COUNT  * sizeof(void *));

  // void copyChar(dest, src);

  int result = 23;                         // Why 23?  Why not 23?
  char dest = 'd';
  char src  = 's';
  argTypes[0] = ARG_CHAR | ARG_OUTPUT;       // dest
  argTypes[1] = ARG_CHAR | ARG_INPUT;        // src
  argTypes[2] = 0;                         // Terminator
  
  args[0] = (void *)&dest;
  args[1] = (void *)&src;

  std::cerr << "Before: " << dest << ", " << src << std::endl;
  result = rpcCall("copyChar", argTypes, args); 
  std::cerr << "After: " << dest << ", " << src << " with result: " << result << std::endl;

  // The displayParms() test case will accept 
  // arbitrary arguments and will simply display 
  // their value.  This allows testing of the 
  // various parameter passing mechanisms.

  // displayParms( ... ) 

  int* vector  = (int*)malloc(4*sizeof(int));
  vector[0] = 12;
  vector[1] = 13;
  vector[2] = 1;
  vector[3] = 99;
  char x = 'h';
  char *y = "fishies";
  char *z[3] = {"fish","and","chips"};

  argTypes[0] = ARG_INT    |     ARG_INPUT | ARG_OUTPUT;   // Result
  argTypes[1] = ARG_INT    | 4 | ARG_INPUT | ARG_OUTPUT;   // Vector
  argTypes[2] = ARG_CHAR   |     ARG_INPUT | ARG_OUTPUT;   // x
  argTypes[3] = ARG_STRING |     ARG_INPUT | ARG_OUTPUT;   // y
  argTypes[4] = ARG_STRING | 3 | ARG_INPUT | ARG_OUTPUT;   // z
  argTypes[5] = 0;                                            // Terminator

  args[0] = (void *)&result;
  args[1] = (void *) vector;
  args[2] = (void *)&x;
  args[3] = (void *) y;
  args[4] = (void *) z;

  std::cerr << "Y is " << y << std::endl
	    << "&Y is " << &y << std::endl
	    << "Z is " << z << std::endl
	    << "&Z is " << &z << std::endl
	    << z[0] << std::endl
	    << z[1] << std::endl
	    << z[2] << std::endl;

  rpcCall("displayParms", argTypes, args);

  std::cerr << "result: " << result << std::endl
            << "vector: " << vector << std::endl
            << vector[0] << std::endl
            << vector[1] << std::endl
            << vector[2] << std::endl
            << vector[3] << std::endl
            << "x is " << x << std::endl
            << "Y is " << y << std::endl
	    << "&Y is " << &y << std::endl
            << "new y is " << (char*)args[3] << std::endl
	    << "Z is " << z << std::endl
	    << "&Z is " << &z << std::endl
	    << z[0] << std::endl
	    << z[1] << std::endl
	    << z[2] << std::endl;

  return 0;
}
