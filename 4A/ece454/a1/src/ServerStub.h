#ifndef _SERVER_STUB_H_
#define _SERVER_STUB_H_

#include "Binder.h"

typedef int (*function)(int *, void **);

int rpcRegister(char* name, int argTypes[], function f);
int rpcExecute( void );

#endif
