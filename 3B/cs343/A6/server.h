#ifndef _SERVER_H_
#define _SERVER_H_

#include <uC++.h>
#include <string>
#include <vector>
#include <queue>
#include <map>
#include "courier.h"

_Task Worker;

_Task Server {
 public:
  typedef std::map<std::string, std::vector<std::string> > Topic2FileNames;
  struct Job {
    const std::string url;
    Courier* courier;
    Job(std::string url, Courier *courier = NULL)
      : url(url), courier(courier) {}
  };
  Server(const unsigned int id, const unsigned int poolSize,
         Topic2FileNames& t2fns);
  ~Server();
  _Nomutex unsigned int getId() const;
  std::vector<std::string>* getFileNames(const std::string& topic);
  bool getFile(const std::string& url);
  Job* requestWork(Job* job);
  void kill();

 private:
  void main();

 private:
  const unsigned int _id;
  const unsigned int _poolSize;
  Topic2FileNames& _t2fns;
  bool _shutdown;               // if false, keep running
  std::string _name;            // prepend to all trace output

  Worker** workers;
  std::queue<Job*> workQueue;   // queue of requested jobs
  uCondition workSignal;        // condition lock for work queue
  int waiting;                  // number of idle workers waiting on cond lock
};

#endif
