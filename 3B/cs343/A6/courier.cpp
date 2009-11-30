#include <uC++.h>
#include <map>
#include <vector>
#include <string>
#include <sstream>
#include "courier.h"
#include "topic_name_server.h"
#include "cache.h"
#include "browser.h"
#include "keyboard.h"
#include "printer.h"

using namespace std;

// task name; will add [id] to create _label
const string myname = "\tCOURIER";

Courier::Courier(TopicNameServer& tns, Cache& cache,
                 Browser& browser, const unsigned int id)
  : _tns(tns), _cache(cache), _browser(browser), _id(id) {

  stringstream ss;
  ss << myname + "[" << _id << "]: ";
  _label = ss.str();
}

Courier::~Courier() {
  Printer::inst()->println(_label + "ending");
}

void Courier::urlExists(bool exists) {
  this->exists = exists;
}

void Courier::putText(bool eof, const std::string& text) {
  // The text passed with eof=true will be ignored!
  if (eof) {
    exists = false;
  } else {
    fileContents += text;
  }
}

void Courier::main() {
  Browser::Job* lastJob = NULL;

  while(true) {
    Printer::inst()->println(_label + "requesting work");
    Browser::Job* job = _browser.requestWork(lastJob);

    // Check if we are supposed to terminate
    if (job == NULL) {
      return;
    }

    // Yield
    yield((rand() % 5) + 1);

    // Handle Job
    switch(job->kind) {
      // Find Topic
      case Keyboard::FindTopic: 
      {
        bool success = false;
        std::ostringstream ss;
        // Find all servers hosting topic
        std::map<unsigned int, Server*>* servers;
        servers = _tns.serversHosting(job->argument);
        std::map<unsigned int, Server*>::iterator it;
        for (it = servers->begin(); it != servers->end(); it++) {
          Server* s = it->second;
          // Get file list from servers for the topiii
          std::vector<std::string>* files = s->getFileNames(job->argument);
          for (unsigned int i = 0; i < files->size(); i++) {
            success = true;
            // Add the file name to the cache, and the result string
            ss << job->argument << ":" << files->at(i) << "\n";
            _cache.addFileName(job->argument, it->first, files->at(i));
          }
          delete files;
        }
        job->success = success;
        job->result = ss.str();
        delete servers;
        break;
      }
      case Keyboard::DisplayFile:
      {
        // Extract topic/filename from url
        unsigned int index = job->argument.find(":");
        if (index == std::string::npos) {
          std::ostringstream ss;
          ss << "Could not parse url " << job->argument << " as topic:filename";
          Printer::inst()->println(ss.str());
          job->success = false;
        } else {
          std::string topic, filename;
          topic = job->argument.substr(0, index);
          filename = job->argument.substr(index+1);
          // Find what servers have the topic
          std::map<unsigned int, Server*>* servers = _tns.serversHosting(topic);
          // Check each server for the file
          std::map<unsigned int, Server*>::iterator it;
          exists = false;
          for (it = servers->begin(); it != servers->end(); it++) {
            Server* s = it->second;
            bool res = s->getFile(job->argument);
            if (res) {
              // Request successful, wait for urlExists
              _Accept(urlExists);
              if (exists) break;
            }
          }
          delete servers;
          // If we found the file
          if (exists) {
            // putText will set exists=false on eof
            fileContents = "";
            while (exists) {
              _Accept(putText);
            }
            // We now have the entire text in fileContents
            _cache.addUrl(job->argument, fileContents);
            job->result = fileContents;
            job->success = true;
          } else {
            std::ostringstream ss;
            ss << _label << "could not find url " << job->argument;
            Printer::inst()->println(ss.str());
            job->success = false;
          }
        }
        break;
      }
      case Keyboard::KillServer: 
      {
        _tns.killServer(job->server);
        job->success = true;
        break;
      }
      default:
        Printer::inst()->error("Courier recieved unexpected Job type");
        break;
    }
    lastJob = job;
  }
}
