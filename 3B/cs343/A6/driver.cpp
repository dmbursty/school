#include <uC++.h>
#include <map>
#include <fstream>
#include <iostream>
#include <string>
#include "printer.h"
#include "browser.h"
#include "topic_name_server.h"

using namespace std;

void uMain::main() {
  // initialize synchronous printer
  Printer::init();
  
  //Read command line arguments
  int couriers;
  int workers;
  if (argc > 2) {
    couriers = atoi(argv[1]);
    workers = atoi(argv[2]);
    if (couriers < 1 || workers < 1) {
      Printer::inst()->error("Pool size must be greater than 0.");
      exit(1);
    }
  } else {
    Printer::inst()->error(
        "Usage: ./webBrowser C (courier-pool size) W (worker-pool size)");
    exit(1);
  }

  // Read server config
  TopicNameServer::ServerConfig config;
  std::ifstream* file = new std::ifstream();
  try {
    file->open("server.config");
  } catch (...) {
    // Could not open file
    Printer::inst()->error("Could not open server.config");
    exit(1);
  }
  // Parse file
  while (true) {
    int serverNo;
    string topic, filename;
    *file >> serverNo;
    *file >> topic;
    *file >> filename;
    if (!file->good()) break;

    // Check for ServerInfo from ServerConfig
    TopicNameServer::ServerConfig::iterator it;
    TopicNameServer::ServerInfo info;
    it = config.find(serverNo);
    // New server in config
    if (it == config.end()) {
      // Add filename to info's t2fns
      std::vector<std::string> filenamelist;
      filenamelist.push_back(filename);
      info.t2fns.insert(pair<std::string,std::vector<std::string> >(
          topic, filenamelist));

      // Add new info to the config
      config.insert(pair<unsigned int, TopicNameServer::ServerInfo>(
          serverNo, info));
    } else {
      // Check if topic exists
      std::vector<std::string> filenamelist;
      Server::Topic2FileNames::iterator itt;
      itt = it->second.t2fns.find(topic);
      if (itt == it->second.t2fns.end()) {
        filenamelist.push_back(filename);
        it->second.t2fns.insert(pair<std::string,std::vector<std::string> >(
            topic, filenamelist));
      } else {
        itt->second.push_back(filename);
      }
    }
  }
  file->close();
  delete file;

  TopicNameServer* tns = new TopicNameServer(config, workers);

  Browser* browse = new Browser(*tns, couriers);

  delete browse;
  delete tns;
  // clean up printer
  Printer::end();
}

