#include "assassin.h"
#include "topic_name_server.h"
#include "printer.h"
#include <sstream>

Assassin::Assassin(TopicNameServer& tns) : _tns(tns) {
  _name = "\tASSASSIN: ";
}

void Assassin::main() {
  Printer::inst()->println(_name + "starting");
  while(true) {
    Server* toKill = _tns.killedServer();
    if (toKill == NULL) break;

    std::ostringstream ss;
    ss << _name << "stalking server SERVER[" << toKill->getId() << "]";
    Printer::inst()->println(ss.str());
    toKill->kill();
  }
}

Assassin::~Assassin() {
  Printer::inst()->println(_name + "ending");
}
