#ifndef _CLIENT_STUB_H_
#define _CLIENT_STUB_H_

#include "Binder.h"

int rpcCall(char* name, int argTypes[], void* args[]);
int rpcTerminate( void );

#endif
