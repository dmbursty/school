#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <stdio.h>

#include <iostream>
#include "Binder.h"
#include "Common.h"

ServerMap servers;

void terminate (int param) {
  printf("Caught SIGINT. Shutting down...\n");
  printf("Goodnight\n");
  exit(0);
}

void handle_connection(int sockfd) {
  unsigned char buf[MAXDATASIZE];
  unsigned char* curr = buf;
  int numbytes;

  // Receive data
  if ((numbytes = recv(sockfd, buf, MAXDATASIZE, 0)) == -1) {
    perror("recv");
    return;
  }

  unsigned int req_type;
  curr += unpackint(curr, &req_type);

  switch(req_type) {
    case LOC_REQ:
    {
      char* name;
      curr += unpackstring(curr, &name);
      printf("Location request for '%s'\n", name);
      sendLocs(servers[name], sockfd);
    }
    break;
    case REG_REQ:
    {
      char* name;
      location loc;
      curr += unpackstring(curr, &name);
      curr += unpacklocation(curr, &loc);
      LocList& locs = servers[name];
      for (unsigned int i = 0; i < locs.size(); i++) {
        if (loc.equals(locs[i])) {
          // Duplicate rpcRegister
          printf("Duplicate registration of '%s' from %s:%s\n",
                 name, loc.host, loc.port);
          sendRegisterAck(1, sockfd);
          return;
        }
      }
      printf("Registration of '%s' from %s:%s\n", name, loc.host, loc.port);
      sendRegisterAck(0, sockfd);
      locs.push_back(loc);
    }
    break;
    case DEREG_REQ:
    {
      location loc;
      curr += unpacklocation(curr, &loc);
      printf("Deregistration from %s:%s\n", loc.host, loc.port);
      for(ServerMap::iterator it = servers.begin(); it != servers.end(); it++) {
        for(LocList::iterator rit = it->second.begin();
            rit != it->second.end();) {
          if (rit->equals(loc)) {
            rit = it->second.erase(rit);
            printf("-Deregistered '%s'\n", it->first.c_str());
          } else {
            rit++;
          }
        }
      }
    }
    break;
    case TERM_REQ:
    {
      printf("Recieved terminate signal.  Shutting down...\n");
      for(ServerMap::iterator i = servers.begin(); i != servers.end(); i++) {
        for(LocList::iterator it = i->second.begin();
            it != i->second.end(); it++) {
          int sockfd;
          if (clientConnection(*it, &sockfd) == 0) {
            sendTerminate(sockfd);
          }
        }
      }
      printf("Goodnight\n");
      exit(0);
    }
    break;
  }
}

int main() {
  // catch SIGINT, and let terminate do proper termination
  signal (SIGINT, terminate);

  int sockfd, port;
  int new_fd;  // listen on sock_fd, new connection on new_fd
  socklen_t sin_size;
  struct sockaddr_storage their_addr; // connector's address information

  int retcode = serverConnection(&sockfd, &port);

  if (retcode != 0) {
    printf("Could not start binder: %d\n", retcode);
    exit(retcode);
  }

  char name[HOST_SIZE];
  gethostname(name, sizeof(name));
  printf("BINDER_ADDRESS %s\n", name);
  printf("BINDER_PORT %d\n", port);

  // main accept() loop
  while(1) {
    sin_size = sizeof their_addr;
    new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
    if (new_fd == -1) {
      perror("accept");
      continue;
    }

    // Handle the connection
    handle_connection(new_fd);
    close(new_fd);
  }

  return 0;
}
