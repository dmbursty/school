#ifndef _COMMON_H_
#define _COMMON_H_

#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <map>
#include <vector>
#include <string>

// max number of bytes we can get at once 
// happens to be the max array size
#define MAXDATASIZE 65535 

// use 0 for random port
#define PORT "0"
#define BACKLOG 10
#define HOST_SIZE 1024
#define PORT_SIZE 8

struct location {
  const char * host;
  const char * port;

  location() {}
  location(const char* host, const char* port) : host(host), port(port) {}

  // Copy constructor
  location(const location& other) {
    char* host_t;
    char* port_t;
    host_t = new char[strlen(other.host)+1];
    port_t = new char[strlen(other.port)+1];
    strcpy(host_t, other.host);
    strcpy(port_t, other.port);
    host = host_t;
    port = port_t;
  }

  bool equals(const location& other) {
    return (strcmp(host, other.host) == 0) && (strcmp(port, other.port) == 0);
  }
};

typedef std::vector<location> LocList;
typedef std::map<std::string, LocList> ServerMap;

//************** sender/receiver methods **************
void sendLocReq(const char* name, int sockfd);
void sendLocs(LocList& locs, int sockfd);
int  recvLocs(LocList& locs, int sockfd);
void sendRegister(const char* name, const location& loc, int sockfd);
void sendRegisterAck(int retcode, int sockfd);
int  recvRegisterAck(int sockfd);
void sendDeregister(const location& loc, int sockfd);
void sendTerminate(int sockfd);
void sendArgs(const char* name, int argTypes[], void** args, int sockfd);
int  recvArgs(char** name, int** argTypes, void*** args, int sockfd);

//************** methods for setting up connections ***************
int clientConnection(location &server, int * sockfd);
int serverConnection(int* sockfd, int* port);

//****** serialization methods *********
int packint(unsigned char *buf, unsigned int i);
int packchar(unsigned char *buf, char c);
int packstring(unsigned char *buf, const char* s);
int packlocation(unsigned char* buf, const location& loc);
int packloclist(unsigned char* buf, const LocList& locs);

//****** deserialization methods *********
int unpackint(unsigned char* buf, unsigned int* i);
int unpackchar(unsigned char* buf, char* c);
int unpackstring(unsigned char* buf, char** s);
int unpacklocation(unsigned char* buf, location* loc);
int unpackloclist(unsigned char* buf, LocList& locs);


//************* utility methods *************
int sendall(int sockfd, unsigned char *buf, int *len);
void *get_in_addr(struct sockaddr *sa);
unsigned short get_port(struct sockaddr *sa);
void getType(int argType, int* type, int* len);
void sigchld_handler(int s);


const int ARG_CHAR   = (1 << 16);
const int ARG_INT    = (2 << 16);
const int ARG_STRING = (3 << 16);
const int ARG_INPUT  = (1 << 31);
const int ARG_OUTPUT = (1 << 30);

const int LOC_REQ = 0;
const int REG_REQ = 1;
const int DEREG_REQ = 2;
const int TERM_REQ = 3;
const int CALL_REQ = 4;

const int rpcPartialMatch = (1 << 29);

#endif

