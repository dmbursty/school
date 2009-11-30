#include "server.h"
#include "worker.h"
#include "printer.h"
#include <sstream>

Server::Server(const unsigned int id, const unsigned int poolSize,
               Topic2FileNames& t2fns)
  : _id(id), _poolSize(poolSize), _t2fns(t2fns), _shutdown(false), waiting(0) {
  // Make pool of workers
  workers = new Worker*[poolSize];
  for (unsigned int i = 0; i < poolSize; i++) {
    workers[i] = new Worker(i, *this);
  }

  std::ostringstream ss;
  ss << "\tSERVER[" << _id << "]";
  _name = ss.str();
}

Server::~Server() {
  for (unsigned int i = 0; i < _poolSize; i++) {
    delete workers[i];
  }
  delete workers;
}

void Server::main() {
  // Wait for shutdown
  while(!_shutdown) {
    _Accept(getFileNames) {}
    or _Accept(getFile) {}
    or _Accept(requestWork) {}
    or _Accept(kill) {}
  }
  Printer::inst()->println(_name + ": cleanup");

  // Wait for outstanding work to finish
  while (workQueue.size() > 0) {
    _Accept(requestWork) {}
  }

  unsigned int toWait = _poolSize - waiting;

  // Wait until all workers are idle
  for (unsigned int i = 0; i < toWait; i++) {
    _Accept(requestWork) {}
  }

  // Kill workers
  for (unsigned int i = 0; i < _poolSize; i++) {
    workQueue.push(NULL);
    workSignal.signal();
  }
}

unsigned int Server::getId() const {
  return _id;
}

std::vector<std::string>* Server::getFileNames(const std::string& topic) {
  std::vector<std::string>* ret = new std::vector<std::string>();
  std::vector<std::string> files = _t2fns[topic];
  for (unsigned int i = 0; i < files.size(); i++) {
    ret->push_back(files[i]);
  }
  return ret;
}

bool Server::getFile(const std::string& url) {
  if (_shutdown) return false;
  Courier* c = dynamic_cast<Courier*>(&uThisTask());
  Job* job = new Job(url, c);
  workQueue.push(job);
  workSignal.signal();
  return true;
}

Server::Job* Server::requestWork(Server::Job* job) {
  if (job != NULL) delete job;
  if (workQueue.size() == 0) {
    waiting++;
    workSignal.wait();
    waiting--;
  }
  assert(workQueue.size() > 0);
  Server::Job* next = workQueue.front();
  workQueue.pop();
  return next;
}

void Server::kill() {
  _shutdown = true;
}
