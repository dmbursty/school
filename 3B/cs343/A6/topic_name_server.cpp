#include "topic_name_server.h"
#include "assassin.h"
#include "printer.h"
#include <sstream>

TopicNameServer::TopicNameServer(ServerConfig &servers,
                                 const unsigned int poolSize)
  : _servers(servers), _poolSize(poolSize) {
  _assassin = new Assassin(*this);
}

void TopicNameServer::main() {
  // Initialize servers
  ServerConfig::iterator it;
  for (it = _servers.begin(); it != _servers.end(); it++) {
    it->second.server = new Server(it->first, _poolSize, it->second.t2fns);
    it->second.alive = true;
  }

  // Wait for destructor
  while (true) {
    _Accept(~TopicNameServer) {
      // Kill assassin
      killQueue.push(NULL);
      killSignal.signal();
      break;
    } or _Accept(killedServer) {}
    or _Accept(killServer) {}
    or _Accept(serversHosting) {}
  }
}

TopicNameServer::~TopicNameServer() {
  // Delete servers
  ServerConfig::iterator it;
  for (it = _servers.begin(); it != _servers.end(); it++) {
    it->second.server->kill();
    delete it->second.server;
  }
  delete _assassin;
}

std::map<unsigned int, Server *>* TopicNameServer::serversHosting(
     const std::string &topic) {

  std::map<unsigned int, Server*>* ret = new std::map<unsigned int, Server*>();

  ServerConfig::iterator it;
  for (it = _servers.begin(); it != _servers.end(); it++) {
    if (!it->second.alive) continue;
    Server::Topic2FileNames::iterator itt;
    itt = it->second.t2fns.find(topic);
    if (itt == it->second.t2fns.end()) continue;
    (*ret)[it->first] = it->second.server;
  }
  std::ostringstream ss;
  ss << "TNS: found topic " << topic << " on " << ret->size() << " servers";
  Printer::inst()->println(ss.str());
  return ret;
}

void TopicNameServer::killServer(unsigned int id) {
  std::ostringstream ss;
  ss << "TNS: request kill of server " << id;
  Printer::inst()->println(ss.str());
  if (_servers.find(id) == _servers.end()) {
    std::ostringstream sss;
    sss << "TNS: could not find server " << id;
    Printer::inst()->println(sss.str());
    return;
  }
  if (!_servers[id].alive) {
    std::ostringstream sss;
    sss << "TNS: server " << id << " already zombied";
    Printer::inst()->println(sss.str());
    return;
  }
  killQueue.push(_servers[id].server);
  killSignal.signal();
  _servers[id].alive = false;
}

Server* TopicNameServer::killedServer() {
  if (killQueue.size() == 0) {
    killSignal.wait();
  }
  assert(killQueue.size() > 0);
  Server* toKill = killQueue.front();
  killQueue.pop();
  return toKill;
}
