#include "worker.h"
#include "server.h"
#include "printer.h"
#include <iostream>
#include <sstream>
#include <fstream>

Worker::Worker(const unsigned int id, Server& server)
  : _id(id), _server(server) {
  std::ostringstream ss;
  ss << "\tWORKER[" << _server.getId() << "][" << id << "]";
  _name = ss.str();
}

void Worker::main() {
  Printer::inst()->println(_name + ": starting");
  Server::Job* lastJob = NULL;
  while (true) {
    Server::Job* job = _server.requestWork(lastJob);

    // Check if we are supposed to terminate
    if (job == NULL) {
      break;
    }

    // Yield
    yield((rand() % 5) + 1);

    // Handle Job
    int found = job->url.find(":");
    std::string topic = job->url.substr(0, found);
    std::string filename = job->url.substr(found+1);
    std::ostringstream ss;
    ss << _name << ": Looking for file \"" << filename;
    ss << "\" in topic \"" << topic << "\"";
    Printer::inst()->println(ss.str());

    std::vector<std::string>* filenames = _server.getFileNames(topic);
    bool exists = false;
    for (unsigned int i = 0; i < filenames->size(); i++) {
      if (filenames->at(i).compare(filename) == 0) {
        // Found it!
        Printer::inst()->println(_name + ": Found url " + job->url);
        exists = true;
        job->courier->urlExists(true);
        // Open file and read contents
        std::ifstream* file = new std::ifstream();
        file->open(filename.c_str());
        std::string line;
        while (true) {
          getline(*file, line);
          if (!file->good()) break;
          job->courier->putText(false, line + "\n");
        }
        job->courier->putText(true, "");
        file->close();
        delete file;
        break;
      }
    }
    if (!exists) {
      Printer::inst()->println(_name + ": Could not find url");
      job->courier->urlExists(false);
    }
    delete filenames;

    lastJob = job;
  }
  Printer::inst()->println(_name + ": ending");
}
