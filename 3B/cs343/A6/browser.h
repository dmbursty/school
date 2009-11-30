#ifndef _BROWSER_H_
#define _BROWSER_H_

#include <uC++.h>
#include <uSemaphore.h>
#include <string>
#include <queue>
#include "keyboard.h"

class Cache;
_Task TopicNameServer;
_Task Courier;

_Task Browser {
 public:
  struct Job {
    const Keyboard::Commands kind;
    std::string argument;
    unsigned int server;
    bool success;
    std::string result;

    Job(Keyboard::Commands kind) : kind(kind) {};
    Job(Keyboard::Commands kind, std::string argument) : kind(kind),
      argument(argument) {};
    Job(Keyboard::Commands kind, unsigned int server) : kind(kind), 
      server(server) {};
  };
  Browser(TopicNameServer& tns, const unsigned int poolSize);
  ~Browser();
  void keyboard(const Keyboard::Commands kind);
  void keyboard(const Keyboard::Commands kind, const std::string& argument);
  void keyboard(const Keyboard::Commands kind, const unsigned int server);
  Job* requestWork(Job* job);

 private:
  void main();

 private:
  uCondition _workSignal;         // condition lock for work queue
  std::queue<Job*> _workQueue;    // queued Jobs for couriers

  Cache& _cache;
  TopicNameServer& _tns;
  const unsigned int _poolSize;
  Courier** _couriers;            // list of couriers
  unsigned int _waiting;          // number of idle couriers waiting on cond lock

  bool _shutDown;                 // while false, keep browsing
  Keyboard* _in;
};

#endif
