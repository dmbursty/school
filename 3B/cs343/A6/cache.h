#ifndef _CACHE_H_
#define _CACHE_H_

#include <uC++.h>
#include <uSemaphore.h>
#include <string>
#include <map>
#include <vector>

class Cache {
 public:
  Cache();
  ~Cache();
  void addFileName(const std::string &topic, const unsigned int server,
                   const std::string &fileName);
  void addUrl(const std::string &url, const std::string &content);
  bool retrieveTopic(std::string &fileNames, const std::string &topic);
  bool retrieveUrl(std::string &content, const std::string &url);
  void clear();                        // Clear the cache
  void printAll();                     // Print contents of cache

 private:
  // mutex for method calls
  uSemaphore mutex;

  // Topic to server/file name
  std::map<std::string, std::vector<std::pair<int, std::string> > > filenames;

  // Url to file content
  std::map<std::string, std::string> contents;
};


#endif
