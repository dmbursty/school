#include <arpa/inet.h>
#include <errno.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>

#include <iostream>
#include <map>

#include "ServerStub.h"

bool init = false;
bool serving = false;
bool do_shutdown = false;
std::map<std::string, function> function_map;
int sockfd;
location my_loc;

void terminate(int param) {
  printf("Caught SIGINT.  Shutting down...\n");
  close(sockfd);

  int term_sockfd;
  location binder(getenv("BINDER_ADDRESS"), getenv("BINDER_PORT"));

  // communicate with binder
  if (clientConnection(binder, &term_sockfd) == 0) {
    sendDeregister(my_loc, term_sockfd);
    close(term_sockfd);
  } else {
    // die
    fprintf(stderr, "Could not contact binder for terminate.\n");
  }

  do_shutdown = true;
}

static int serverInit() {
  // Catch SIGINT
  signal(SIGINT, terminate);

  int port;
  int retcode = serverConnection(&sockfd, &port);

  if (retcode < 0) {
    printf("An error occured setting up server connection: %d", retcode);
    return retcode;
  }

  char* host_str = new char[HOST_SIZE];
  char* port_str = new char[PORT_SIZE];
  gethostname(host_str, HOST_SIZE);
  sprintf(port_str, "%d", port);
  my_loc.host = host_str;
  my_loc.port = port_str;

  init = true;
  return 0;
}

int rpcRegister(char* name, int argTypes[], function f) {
  if (!init) {
    if (serverInit() < 0) {
      return -1;
    }
  }

  int reg_sockfd;
  location binder(getenv("BINDER_ADDRESS"), getenv("BINDER_PORT"));

  int result;
  // communicate with binder
  if (clientConnection(binder, &reg_sockfd) == 0) {
    sendRegister(name, my_loc, reg_sockfd);
    result = recvRegisterAck(reg_sockfd);
    close(reg_sockfd);
  } else {
    // die
    fprintf(stderr, "Could not contact binder for register.\n");
    return -1;
  }

  if (result == 0) {
    serving = true;
    printf("Registered %s\n", name);
    function_map[name] = f;
  }
  return result;
}

int rpcExecute( void ) {
  if (!serving) {
    printf("Trying to rpcExecute before any successful registrations\n");
    return -1;
  }

  int new_fd;  // listen on sock_fd, new connection on new_fd
  socklen_t sin_size;
  struct sockaddr_storage their_addr; // connector's address information
  char s[INET6_ADDRSTRLEN];

  printf("server: waiting for connections...\n");

  // main accept() loop
  while(1) {
    sin_size = sizeof their_addr;
    new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
    if (new_fd == -1) {
      if (do_shutdown) return 0;
      perror("accept");
      continue;
    }

    inet_ntop(their_addr.ss_family,
        get_in_addr((struct sockaddr *)&their_addr),
        s, sizeof s);

    char* name;
    int* argTypes;
    void** args;

    // Get argtypes/args from socket
    int result = recvArgs(&name, &argTypes, &args, new_fd);

    if (result < 0) {
      printf("Could not recieve arguments for call\n");
      close(new_fd);
      continue;
    } else if (result == 1) {
      // Got termination request from binder
      printf("Recieved terminate from binder.  Shutting down...\n");
      return 0;
    }

    // this is the child process
    if (!fork()) {
      // child doesn't need the listener
      close(sockfd);

      printf("Got '%s' request from %s\n", name, s);
      function_map[name](argTypes, args);

      // Return args to the client for output
      sendArgs(name, argTypes, args, new_fd);

      // Delete memory for the function call
      delete name;

      for (int i = 0; argTypes[i] != 0; i++) {
        int type, len;
        getType(argTypes[i], &type, &len);
        switch(type) {
          case ARG_INT:
            delete (int*)args[i]; break;
          case ARG_CHAR:
            delete (char*)args[i]; break;
          case ARG_STRING:
            // We can't do this memory cleanup because these may be static!
            //for (int j = 0; j < len; j++) delete ((char**)args[i])[j];
            //delete (char**)args[i]; break;
            break;
        }
      }

      delete args;
      delete argTypes;

      close(new_fd);
      exit(0);
    }
    close(new_fd);  // parent doesn't need this
  }

  return 0;
}
