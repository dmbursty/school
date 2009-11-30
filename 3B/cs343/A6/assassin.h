#ifndef _ASSASSIN_H_
#define _ASSASSIN_H_

#include <uC++.h>
#include <string>

_Task TopicNameServer;

_Task Assassin {
 public:
  Assassin(TopicNameServer& tns);
  ~Assassin();

 private:
  void main();

 private:
  TopicNameServer& _tns;
  std::string _name;      // Name of task to prepend in front of trace output
};

#endif
