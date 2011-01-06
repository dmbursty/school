#include <errno.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>

#include <iostream>
#include "ClientStub.h"

static ServerMap cache;

// Contacts the binder and gets a set of servers that serves the given method
int getServer(char* name, LocList& locs) {
  location binder(getenv("BINDER_ADDRESS"), getenv("BINDER_PORT"));

  int sockfd;

  // communicate with binder
  if (clientConnection(binder, &sockfd) == 0) {
    sendLocReq(name, sockfd);
    int result = recvLocs(locs, sockfd);
    close(sockfd);
    return result;
  } else {
    // die
    fprintf(stderr, "Could not contact binder for server request.\n");
    return -1;
  }
}

int rpcCall(char* name, int argTypes[], void* args[]) {
  // check cache
  std::string check(name);
  ServerMap::iterator it = cache.find(check);

  bool connected = false;
  int sockfd;
  LocList server_l;
  LocList::iterator lit;

  if (it != cache.end()) {
    printf("cache hit!\n");

    // cache hit
    server_l = (*it).second;

    // try each server in the list and try to connect to it
    for (lit=server_l.begin() ; lit != server_l.end(); lit++ ) {
      if (clientConnection(*lit, &sockfd) == 0) {
        // got a connection!
        connected = true;
        break;
      }
    }
  } 
  
  // if cache miss or no connections are formed, try binder again
  if (!connected){

    // cache miss; go to binder to get location of servers
    if (getServer(name, server_l) < 0) {
      // Could not get servers
      return -1;
    } else if (server_l.size() == 0) {
      fprintf(stderr, "No servers found supporting method '%s'\n", name);
      return -1;
    }

    // cache servers
    cache[check] = server_l;

    // try each server in the list and try to connect to it
    for (lit=server_l.begin() ; lit != server_l.end(); lit++ ) {
      if (clientConnection(*lit, &sockfd) == 0) {
        // got a connection!
        connected = true;
        break;
      }
    }
    
    // if still no connections are formed, exit
    if (!connected) {
      fprintf(stderr, "Could not connect to any servers.\n");
      return -1;
    }
  }

  // send the arguments
  sendArgs(name, argTypes, args, sockfd);

  // receive data back
  char * r_name;
  int * r_argTypes;
  void ** r_args;

  int result = recvArgs(&r_name, &r_argTypes, &r_args, sockfd);

  if (result < 0) {
    printf("Could not recieve return arguments.\n");
    return -1;
  }

  // sanity check; the function names should be the same
  if (strcmp(name, r_name) != 0) {
    fprintf(stderr, "The function names aren't the same! (%s, %s)\n", name, r_name);
  }

  for (int i = 0; r_argTypes[i] != 0; i++) {
    // sanity check; return argTypes should be identical
    if (argTypes[i] != r_argTypes[i]) { 
      fprintf(stderr, "argTypes unequal\n"); 
    }
  }

  // Copy output args into local vars
  for (int i = 0; r_argTypes[i] != 0; i++) {
    int type, len;
    getType(r_argTypes[i], &type, &len);
    if (len == 0 && type != ARG_STRING) len = 1;
    if ((r_argTypes[i] & ARG_OUTPUT) == ARG_OUTPUT) {
      // TODO: Handle ugly memory problems if some args were on heap (?)
      switch(type) {
        case ARG_INT:
          for (int j = 0; j < len; j++) {
            ((int**)args)[i][j] = ((int**)r_args)[i][j];
          }
          break;
        case ARG_CHAR:
          for (int j = 0; j < len; j++) {
            ((char*)args[i])[j] = ((char*)r_args[i])[j];
          }
          break;
        case ARG_STRING:
          if (len == 0) {
            args[i] = r_args[i];
          } else {
            for (int j = 0; j < len; j++) {
              ((char**)args[i])[j] = ((char**)r_args[i])[j];
            }
          }
          break;
      }
    } else {
      // Delete the memory we don't need (for non output args)
      switch(type) {
        case ARG_INT:
          delete (int*)r_args[i]; break;
        case ARG_CHAR:
          delete (char*)r_args[i]; break;
        case ARG_STRING:
          if (len == 0) {
            delete (char*)r_args[i]; break;
          } else {
            for (int j = 0; j < len; j++) delete ((char**)r_args[i])[j];
            delete (char**)r_args[i];
          }
          break;
      }
    }
  }

  delete r_args;
  delete r_name;
  delete r_argTypes;

  close(sockfd);

  return 0;
}

int rpcTerminate ( void ) {
  location binder(getenv("BINDER_ADDRESS"), getenv("BINDER_PORT"));
  int sockfd;
  unsigned char buf[4];
  int size = 0;

  // communicate with binder
  if (clientConnection(binder, &sockfd) == 0) {
    size += packint(buf, TERM_REQ);
    sendall(sockfd, buf, &size);
    close(sockfd);
  } else {
    // die
    fprintf(stderr, "Could not contact binder for terminate.\n");
    return -1;
  }

  return 0;
}
