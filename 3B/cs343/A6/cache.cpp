#include "cache.h"
#include "printer.h"
#include <string>
#include <sstream>

using namespace std;

// string to prepend in trace output
const string myname = "CACHE: ";

Cache::Cache() {}

Cache::~Cache() {
  Printer::inst()->println(myname + "ending");
}

void Cache::addFileName(const std::string &topic, const unsigned int server,
                        const std::string &fileName) {
  // Add filename, server pair to cache for given topic
  rwlock.startWrite();
  filenames[topic].push_back(std::pair<int, std::string>(server, fileName));
  rwlock.endWrite();
}

void Cache::addUrl(const std::string &url, const std::string &content) {
  // Cache contents of url
  rwlock.startWrite();
  contents[url] = content;
  rwlock.endWrite();
}

bool Cache::retrieveTopic(std::string &fileNames, const std::string &topic) {
  rwlock.startRead();
  // Check if topic is in cache
  std::map<std::string,std::vector<std::pair<int,std::string> > >::iterator it;
  it = filenames.find(topic);
  if (it == filenames.end()) {
    rwlock.endRead();
    return false;
  }
  // If in cache, return results in fileNames parameter
  for (unsigned int i = 0; i < it->second.size(); i++) {
    fileNames += topic + ":";
    fileNames += it->second[i].second + "\n";
  }
  rwlock.endRead();
  return true;
}

bool Cache::retrieveUrl(std::string &content, const std::string &url) {
  rwlock.startRead();
  // Check if url is in cache
  std::map<std::string, std::string>::iterator it;
  it = contents.find(url);
  if (it == contents.end()) {
    rwlock.endRead();
    return false;
  }
  // Simply retur the cached content
  content = it->second;
  rwlock.endRead();
  return true;
}

void Cache::clear() {
  rwlock.startWrite();
  // Clear both caches
  filenames.clear();
  contents.clear();
  rwlock.endWrite();
}

void Cache::printAll() {
  rwlock.startRead();
  // Print topic cache
  Printer::inst()->println("CACHE: Topic to Url");
  std::map<std::string,std::vector<std::pair<int,std::string> > >::iterator it;
  for (it = filenames.begin(); it != filenames.end(); it++) {
    Printer::inst()->println("Topic: " + it->first);
    for (unsigned int i = 0; i < it->second.size(); i++) {
      ostringstream ss;
      ss << "\tServer/File name: ";
      ss << it->second[i].first << " ";
      ss << it->second[i].second;
      Printer::inst()->println(ss.str());
    }
  }

  // Print file content cache
  Printer::inst()->println("CACHE: Url to File Content");
  std::map<std::string, std::string>::iterator itt;
  for (itt = contents.begin(); itt != contents.end(); itt++) {
    Printer::inst()->println("url " + itt->first + "   some content:");
    Printer::inst()->println(itt->second.substr(0, 60) + "...\n");
  }
  rwlock.endRead();
}
