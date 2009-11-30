#ifndef _WORKER_H_
#define _WORKER_H_

#include <uC++.h>

_Task Server;

_Task Worker {
 public:
  Worker(const unsigned int id, Server& server);

 private:
  void main();

 private:
  const unsigned int _id;
  Server& _server;
  std::string _name;      // prepend to trace output
};

#endif
