#include <signal.h>
#include <stdlib.h>
#include <vector>

#include "Common.h"

//************** sender/receiver methods **************
// send a location request to the given socket, assume that the request will
// fit into the one MAXDATASIZE buffer. I'm sure it will.
void sendLocReq(const char* name, int sockfd) {
  unsigned char buf[MAXDATASIZE];
  int size = 0;
  size += packint(buf+size, LOC_REQ);
  size += packstring(buf+size, name);
  sendall(sockfd, buf, &size);
}

void sendLocs(LocList& locs, int sockfd) {
  unsigned char buf[MAXDATASIZE];
  int size = 0;
  size += packint(buf, LOC_REQ);
  size += packloclist(buf+size, locs);
  sendall(sockfd, buf, &size);
}

int recvLocs(LocList& locs, int sockfd) {
  unsigned char buf[MAXDATASIZE];
  unsigned char* curr = buf;
  int numbytes;

  // Receive data
  if ((numbytes = recv(sockfd, buf, MAXDATASIZE, 0)) == -1) {
    perror("recv");
    return -1;
  }

  unsigned int req_type;
  curr += unpackint(curr, &req_type);
  if ((int)req_type != LOC_REQ) {
    printf("Unexpected type returned from location request: %d\n", req_type);
    return -1;
  }
  curr += unpackloclist(curr, locs);

  return 0;
}

void sendRegister(const char* name, const location& loc, int sockfd) {
  unsigned char buf[MAXDATASIZE];
  int size = 0;
  size += packint(buf+size, REG_REQ);
  size += packstring(buf+size, name);
  size += packlocation(buf+size, loc);
  sendall(sockfd, buf, &size);
}

void sendRegisterAck(int retcode, int sockfd) {
  unsigned char buf[MAXDATASIZE];
  int size = 0;
  size += packint(buf+size, REG_REQ);
  size += packint(buf+size, retcode);
  sendall(sockfd, buf, &size);
}

int recvRegisterAck(int sockfd) {
  unsigned char buf[MAXDATASIZE];
  unsigned char* curr = buf;
  int numbytes;

  // Receive data
  if ((numbytes = recv(sockfd, buf, MAXDATASIZE, 0)) == -1) {
    perror("recv");
    return -1;
  }

  unsigned int req_type;
  curr += unpackint(curr, &req_type);
  if ((int)req_type != REG_REQ) {
    printf("Unexpected type returned from registration request: %d\n",req_type);
    return -1;
  }
  unsigned int ret;
  curr += unpackint(curr, &ret);
  return (int)ret;
}

void sendDeregister(const location& loc, int sockfd) {
  unsigned char buf[MAXDATASIZE];
  int size = 0;
  size += packint(buf, DEREG_REQ);
  size += packlocation(buf+size, loc);
  sendall(sockfd, buf, &size);
}

void sendTerminate(int sockfd) {
  unsigned char buf[MAXDATASIZE];
  int size = 0;
  size += packint(buf, TERM_REQ);
  sendall(sockfd, buf, &size);
}

// a send method for sending argTypes/args array set
void sendArgs(const char* name, int argTypes[], void** args, int sockfd) {
  // begin client connection-y stuff
  unsigned char buf[MAXDATASIZE];
  int size = packint(buf, CALL_REQ);

  size += packstring(buf+size, name);
 
  // buffer/send type info
  for (int i = 0; argTypes[i] != 0; i++) {
    size += packint(buf+size, argTypes[i]);
    if (size + 4 > MAXDATASIZE) {
      // send
      sendall(sockfd, buf, &size);
      size = 0;
    }
  }
  size += packint(buf+size, 0);

  int type, len;
  // buffer/send arguments
  for (int i = 0; argTypes[i] != 0; i++) {
    getType(argTypes[i], &type, &len);

    if (type == ARG_INT) {
      if (len == 0) { len = 1; }
      int * intargs = (int*)args[i];
      for (int j = 0; j < len; j++) {
        if (size + 4 > MAXDATASIZE) {
          // send
          sendall(sockfd, buf, &size);
          size = 0;
        }
        size += packint(buf+size, (unsigned int) intargs[j]);
      }
    } else if (type == ARG_CHAR || (type == ARG_STRING && len == 0)) {
      char * charargs = (char*)args[i];
      if (type == ARG_STRING) { 
        len = strlen(charargs) + 1;
      } else if (len == 0) {
        len = 1;
      }
      for (int j = 0; j < len; j++) {
        if (size + 1 > MAXDATASIZE) {
          // send
          sendall(sockfd, buf, &size);
          size = 0;
        }
        size += packchar(buf+size, charargs[j]);
      }
    } else if (type == ARG_STRING) {
      char ** charargs = (char**)args[i];
      for (int j = 0; j < len; j++) {
        unsigned int str_len = strlen(charargs[j]) + 1;
        if (size + str_len > MAXDATASIZE) {
          // send
          sendall(sockfd, buf, &size);
          size = 0;
        }
        size += packstring(buf+size, charargs[j]);
      }
    } else {
      // wut
    }

    /*
    switch(type) {
      case ARG_INT:
      {
     }
      break;
      case ARG_CHAR:
      {
        char * charargs = (char*)args[i];
        for (int j = 0; j < len; j++) {
          if (size + 1 > MAXDATASIZE) {
            // send
            sendall(sockfd, buf, &size);
            size = 0;
          }
          size += packchar(buf+size, charargs[j]);
        }
      }
      break;
      case ARG_STRING:
      {
        char ** charargs = (char**)args[i];
        for (int j = 0; j < len; j++) {
          unsigned int str_len = strlen(charargs[j]) + 1;
          if (size + str_len > MAXDATASIZE) {
            // send
            sendall(sockfd, buf, &size);
            size = 0;
          }
          size += packstring(buf+size, charargs[j]);
        }
      }
      break;
    }
    */
  }

  // final send
  sendall(sockfd, buf, &size);
  size = 0;
}

int recvArgs(char** name, int** argTypes, void*** args, int sockfd) {
  unsigned char buf[MAXDATASIZE];
  unsigned char* curr = buf;
  int numbytes;

  // Receive data
  if ((numbytes = recv(sockfd, buf, MAXDATASIZE, 0)) == -1) {
    perror("recv");
    return -1;
  }

  // Print marshalled data
  /*
  printf("%d\n", numbytes);
  for (int i = 0; i < numbytes; i++) {
    if (i%urn 4 == 0) {
      printf("\n");
    }
    printf("%02x", buf[i]);
  }
  printf("\n");
  */

  // Demarshall data
  unsigned int req_type;
  curr += unpackint(curr, &req_type);
  if ((int)req_type == TERM_REQ) {
    // Got termination request from binder
    return 1;
  } else if ((int)req_type != CALL_REQ) {
    printf("Server got unexpected reqest type.  Ignoring");
  }

  // unpack name
  curr += unpackstring(curr, name);

  // unpack argtypes
  std::vector<int> v_argTypes;
  unsigned int argType;

  curr += unpackint(curr, &argType);
  while(argType > 0) {
    v_argTypes.push_back(argType);
    // TODO: Make sure we don't go past the end of buf
    curr += unpackint(curr, &argType);
  }

  // Now that we know the length, copy argTypes to the return array
  int num_args = v_argTypes.size();
  *argTypes = new int[num_args + 1];
  (*argTypes)[num_args] = 0;
  for (int i = 0; i < num_args; i++) {
    (*argTypes)[i] = v_argTypes[i];
  }

  //unpack args
  int type, len;
  *args = new void*[num_args];
  for (int i = 0; i < num_args; i++) {
    getType((*argTypes)[i], &type, &len);

    if (type == ARG_INT) {
      if (len == 0) len = 1;
      (*args)[i] = new int[len];
      for (int j = 0; j < len; j++) {
        curr += unpackint(curr, (unsigned int*)(*args)[i] + j);
      }
    } else if (type == ARG_CHAR) {
      if (len == 0) len = 1;
      (*args)[i] = new char[len];
      for (int j = 0; j < len; j++) {
        curr += unpackchar(curr, (char*)(*args)[i] + j);
      }
    } else if (type == ARG_STRING && len == 0) {
      curr += unpackstring(curr, (char**)(*args) + i);
    } else if (type == ARG_STRING) {
      (*args)[i] = new char*[len];
      for (int j = 0; j < len; j++) {
        curr += unpackstring(curr, (char**)(*args)[i] + j);
      }
    }
  }
  return 0;
}

//************** methods for setting up connections ***************
// gets a client connection using the given server's information and returns
// the socket description in the sockfd field
int clientConnection(location &server, int * sockfd) {

  // execute request to server
  struct addrinfo hints, *servinfo, *p;
  int rv;
  char s[INET6_ADDRSTRLEN];

  memset(&hints, 0, sizeof hints);
  hints.ai_family = AF_UNSPEC;
  hints.ai_socktype = SOCK_STREAM;

  if ((rv = getaddrinfo(server.host, server.port, &hints, &servinfo)) != 0) {
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
    return 1;
  }

  // loop through all the results and connect to the first we can
  for(p = servinfo; p != NULL; p = p->ai_next) {
    if ((*sockfd = socket(p->ai_family, p->ai_socktype,
            p->ai_protocol)) == -1) {
      perror("socket");
      continue;
    }

    if (connect(*sockfd, p->ai_addr, p->ai_addrlen) == -1) {
      close(*sockfd);
      perror("connect");
      continue;
    }

    break;
  }

  if (p == NULL) {
    return 2;
  }

  inet_ntop(p->ai_family, get_in_addr((struct sockaddr *)p->ai_addr),
          s, sizeof s);

  freeaddrinfo(servinfo); // all done with this structure

  return 0;
}

// Sets up server to recieve a connection.
// Returns socket description, and randomly bound port
int serverConnection(int * sockfd, int* port) {
  struct addrinfo hints, *servinfo, *p;
  struct sigaction sa;
  int yes=1;
  int rv;

  memset(&hints, 0, sizeof hints);
  hints.ai_family = AF_UNSPEC;
  hints.ai_socktype = SOCK_STREAM;
  hints.ai_flags = AI_PASSIVE; // use my IP

  if ((rv = getaddrinfo(NULL, PORT, &hints, &servinfo)) != 0) {
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
    return 1;
  }

  // loop through all the results and bind to the first we can
  for(p = servinfo; p != NULL; p = p->ai_next) {
    if ((*sockfd = socket(p->ai_family, p->ai_socktype,
            p->ai_protocol)) == -1) {
      perror("server: socket");
      continue;
    }

    if (setsockopt(*sockfd, SOL_SOCKET, SO_REUSEADDR, &yes,
            sizeof(int)) == -1) {
      perror("setsockopt");
      return -1;
    }

    if (bind(*sockfd, p->ai_addr, p->ai_addrlen) == -1) {
      close(*sockfd);
      perror("server: bind");
      continue;
    }

    break;
  }

  if (p == NULL)  {
    fprintf(stderr, "server: failed to bind\n");
    return 2;
  }

  // print the randomly bound port
  struct sockaddr addr;
  socklen_t addr_size = sizeof addr;
  getsockname(*sockfd, &addr, &addr_size);
  *port = get_port(&addr);

  freeaddrinfo(servinfo); // all done with this structure

  if (listen(*sockfd, BACKLOG) == -1) {
    perror("listen");
    return -1;
  }

  sa.sa_handler = sigchld_handler; // reap all dead processes
  sigemptyset(&sa.sa_mask);
  sa.sa_flags = SA_RESTART;
  if (sigaction(SIGCHLD, &sa, NULL) == -1) {
    perror("sigaction");
    return -1;
  }

  return 0;
}

//****** serialization methods *********
// pack ints
int packint(unsigned char *buf, unsigned int i) {
  unsigned int mask = 0x000000ff;
  *buf = i >> 24;
  buf++;
  *buf = mask & (i >> 16);
  buf++;
  *buf = mask & (i >> 8);
  buf++;
  *buf = mask & i;
  buf++;
  return 4;
}

// pack chars
int packchar(unsigned char *buf, char c) {
  *buf = c;
  buf++;
  return 1;
}

// pack strings
int packstring(unsigned char *buf, const char* s) {
  int bytes = 0;
  for(int i = 0; s[i] != '\0'; i++) {
    bytes += packchar(buf+bytes, s[i]);
  }
  bytes += packchar(buf+bytes, '\0');
  return bytes;
}

int packlocation(unsigned char* buf, const location& loc) {
  int bytes = 0;
  bytes += packstring(buf, loc.host);
  bytes += packstring(buf+bytes, loc.port);
  return bytes;
}

int packloclist(unsigned char* buf, const LocList& locs) {
  int bytes = 0;
  bytes += packint(buf, locs.size());
  for (unsigned int i = 0; i < locs.size(); i++) {
    bytes += packlocation(buf+bytes, locs[i]);
  }
  return bytes;
}

//****** deserialization methods *********
int unpackint(unsigned char* buf, unsigned int* i) {
  *i = (buf[0]<<24) | (buf[1]<<16) | (buf[2]<<8) | (buf[3]);
  return 4;
}

int unpackchar(unsigned char* buf, char* c) {
  *c = *buf;
  return 1;
}

int unpackstring(unsigned char* buf, char** s) {
  size_t len = strlen((char*)buf) + 1;
  *s = new char[len];
  strcpy(*s, (char*)buf);
  return len;
}

int unpacklocation(unsigned char* buf, location* loc) {
  int len = 0;
  char* host;
  char* port;
  len += unpackstring(buf, &host);
  len += unpackstring(buf+len, &port);
  loc->host = host;
  loc->port = port;
  return len;
}

int unpackloclist(unsigned char* buf, LocList& locs) {
  int len = 0;
  unsigned int size;
  location l;

  len += unpackint(buf, &size);
  for (unsigned int i = 0; i < size; i++) {
    len += unpacklocation(buf+len, &l);
    locs.push_back(l);
  }
  return len;
}

//************* utility methods *************
// a routine for sending the entirety of a buffer
// provided by Beej's Guide
int sendall(int sockfd, unsigned char *buf, int *len) {
  int total = 0;        // how many bytes we've sent
  int bytesleft = *len; // how many we have left to send
  int n;

  while(total < *len) {
    n = send(sockfd, buf+total, bytesleft, 0);
    if (n == -1) { break; }
    total += n;
    bytesleft -= n;
  }

  *len = total; // return number actually sent here

  if (n == -1) {
    fprintf(stderr, "Sendall: send routine failed unexpectedly.");
  }

  return n==-1?-1:0; // return -1 on failure, 0 on success
} 

// get sockaddr, IPv4 or IPv6:
// provided by Beej's Guide
void *get_in_addr(struct sockaddr *sa)
{
  if (sa->sa_family == AF_INET) {
    return &(((struct sockaddr_in*)sa)->sin_addr);
  }

  return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

unsigned short get_port(struct sockaddr *sa)
{
  if (sa->sa_family == AF_INET) {
    return ntohs(((struct sockaddr_in*)sa)->sin_port);
  }

  return ntohs(((struct sockaddr_in6*)sa)->sin6_port);
}

// using the an argType integer, return the type and length of the item
void getType(int argType, int* type, int* len) {
  unsigned int typemask = 0x00ff0000;
  unsigned int lenmask = 0x0000ffff;
  *type = typemask & argType;
  *len = lenmask & argType;
}

void sigchld_handler(int s)
{
  while(waitpid(-1, NULL, WNOHANG) > 0);
}
