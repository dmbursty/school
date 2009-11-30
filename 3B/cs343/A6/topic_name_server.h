#ifndef _TOPICNAMESERVER_H_
#define _TOPICNAMESERVER_H_

#include <uC++.h>
#include <map>
#include <string>
#include <queue>
#include "server.h"

_Task Assassin;

_Task TopicNameServer {
 public:
  struct ServerInfo {
    Server *server;
    bool alive;
    Server::Topic2FileNames t2fns;
  };
  typedef std::map<unsigned int, ServerInfo> ServerConfig;
  TopicNameServer(ServerConfig &servers, const unsigned int poolSize);
  ~TopicNameServer();
  std::map<unsigned int, Server *> *serversHosting(const std::string &topic);
  void killServer(unsigned int id);
  Server *killedServer();

 private:
  void main();

 private:
  ServerConfig& _servers;
  const unsigned int _poolSize;
  Assassin* _assassin;

  std::queue<Server*> killQueue;    // queue of kill jobs for assassin
  uCondition killSignal;            // condition lock for queue of kill jobs
};

#endif
