#include "browser.h"
#include "topic_name_server.h"
#include "printer.h"
#include "keyboard.h"
#include "cache.h"
#include "courier.h"
#include <sstream>

using namespace std;

// string to prepend to trace output
const string myname = "BROWSER: ";

Browser::Browser(TopicNameServer& tns, const unsigned int poolSize) : 
  _cache(*new Cache()), _tns(tns), 
  _poolSize(poolSize), _waiting(0), _shutDown(false), _in(NULL) {

  // create couriers
  _couriers = new Courier*[_poolSize];
  for (unsigned int i = 0; i < _poolSize; i++) {
    _couriers[i] = new Courier(_tns, _cache, *this, i);
  }
}

Browser::~Browser() { 
  // wait for couriers to end
  for (unsigned int i = 0; i < _poolSize; i++) {
    delete _couriers[i];
  }

  delete[] _couriers;

  // wait for keyboard to end; 
  delete _in;

  // deallocate cache
  delete &_cache;

  stringstream ss;
  ss << myname << "ending";
  Printer::inst()->println(ss.str());
} 

void Browser::main() {
  _in = new Keyboard(*this);

  while(!_shutDown) {
      _Accept(keyboard) {
      } or  _Accept(requestWork) {
      }
  }

  Printer::inst()->println(myname + "cleaning up");
  // Note: keyboard should kill itself after receiving a EOF or q.

  // wait for outstanding work to finish
  while (_workQueue.size() > 0) {
    _Accept(requestWork);
  }

  // get number of idle couriers
  unsigned int toWait = _poolSize - _waiting;

  // wait until all couriers are idle
  for (unsigned int i = 0; i < toWait; i++) {
    _Accept(requestWork);
  }

  // kill couriers by passing NULL jobs
  // note: signal won't be lost because all couriers are idle by this point
  for (unsigned int i = 0; i < _poolSize; i++) {
    _workQueue.push(NULL);
    _workSignal.signal();
  }
}

void Browser::keyboard(const Keyboard::Commands kind) {
  switch(kind) {
    case Keyboard::Quit:
      Printer::inst()->println(myname + "quit received");
      _shutDown = true;
      break;
    case Keyboard::PrintCache:
      _cache.printAll();
      break;
    case Keyboard::ClearCache:
      _cache.clear();
      break;
    default:
      Printer::inst()->error(myname 
          + "Wrong command kind for keyboard(kind)!");
      break;
  }
}

void Browser::keyboard(const Keyboard::Commands kind,
                       const std::string& argument) {
  stringstream ss;

  switch (kind) {
    case Keyboard::FindTopic:
    {
      ss << myname << "requesting topic information " << argument;
      Printer::inst()->println(ss.str());

      string filenames;
      // check if we have a cache hit first
      if (_cache.retrieveTopic(filenames, argument)) {
        Printer::inst()->println("TOPIC: " + argument);
        Printer::inst()->println(filenames);
      } else {
        // add work to work queue
        Job* newJob = new Job(kind, argument);
        _workQueue.push(newJob);
        _workSignal.signal();
      }
      break;
    }
    case Keyboard::DisplayFile:
    {
      ss << myname << "requesting file " << argument;
      Printer::inst()->println(ss.str());

      string content;
      // check if we have a cache hit first
      if (_cache.retrieveUrl(content, argument)) {
        Printer::inst()->println("URL: " + argument);
        Printer::inst()->println(content);
      } else {
        // add work to work queue
        Job* newJob = new Job(kind, argument);
        _workQueue.push(newJob);
        _workSignal.signal();
      }
      break;
    }
    default:
      Printer::inst()->error(myname 
          + "Wrong command kind for keyboard(kind, arg)!");
      break;
  }
}

void Browser::keyboard(const Keyboard::Commands kind,
                       const unsigned int server) {
  stringstream ss;
  ss << myname << "request termination of server " << server;
  Printer::inst()->println(ss.str());

  Job* newJob = new Job(kind, server);

  // put into work queue
  _workQueue.push(newJob);
  _workSignal.signal();
}

Browser::Job* Browser::requestWork(Browser::Job* job) {
  // return work 
  if (job) {
    switch(job->kind) {
      case Keyboard::FindTopic:
      {
        if (job->success) {
          // print result
          Printer::inst()->println("TOPIC: " + job->argument);
          Printer::inst()->println(job->result);

          // error checking: make sure result is in cache
          string filenames;
          if (!_cache.retrieveTopic(filenames, job->argument)) {
            stringstream ss;
            ss << myname << "topic " << job->argument << " wasn't in cache!";
            Printer::inst()->error(ss.str());
          }
        } else {
          stringstream ss;
          ss << "No matches found for topic " << job->argument;
          Printer::inst()->println(ss.str());
        }
        break;
      }
      case Keyboard::DisplayFile:
      {
        if (job->success) {
          // print result
          Printer::inst()->println("URL: " + job->argument);
          Printer::inst()->println(job->result);

          // error checking: make sure result is in cache
          string content;
          if (!_cache.retrieveUrl(content, job->argument)) {
            stringstream ss;
            ss << myname << "url" << job->argument << " wasn't in cache!";
            Printer::inst()->error(ss.str());
          }
        } else {
          stringstream ss;
          ss << "URL " << job->argument << " not found";
          Printer::inst()->println(ss.str());
        }
        break;
      }
      case Keyboard::KillServer:
      {
        stringstream ss;
        if (job->success) {
          // print informational message
          ss << myname << "server " << job->server << " kill delivered.";
          Printer::inst()->println(ss.str());
        } else {
          // error situation
          ss << myname << "server " << job->server << " didn't die!";
          Printer::inst()->error(ss.str());
        }
        break;
      }
      default:
        Printer::inst()->error(myname 
            + "Wrong command kind for job!");
        break;
    }
    delete job;
  }

  // get work from queue; block if no work is available
  if (_workQueue.size() == 0) {
    _waiting++;
    _workSignal.wait();
    _waiting--;
  }
  assert(_workQueue.size() > 0);
  Job* next = _workQueue.front();
  _workQueue.pop();
  return next;
}

