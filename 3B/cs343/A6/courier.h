#ifndef _COURIER_H_
#define _COURIER_H_

#include <uC++.h>
#include <string>
#include "browser.h"

_Task TopicNameServer;
class Cache;

_Task Courier {
 public:
  Courier(TopicNameServer& tns, Cache& cache, Browser& browser,
          const unsigned int id);
  ~Courier();
  void urlExists(bool exists);
  void putText(bool eof, const std::string& text);

 private:
  void main();

  TopicNameServer& _tns;
  Cache& _cache;
  Browser& _browser;
  
  // id of this courier
  const unsigned int _id;

  // Contains COURIER[id] to prepend to trace output
  std::string _label;

  // Last return of urlExists
  // also used to signal eof from putText
  bool exists;

  // File Contents from putText
  std::string fileContents;
};

#endif
