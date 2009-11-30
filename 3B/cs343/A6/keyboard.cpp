#include <uC++.h>
#include "keyboard.h"
#include "browser.h"
#include "printer.h"
#include <iostream>
#include <sstream>

using namespace std;

const string myname = "KEYBOARD: ";

Keyboard::Keyboard(Browser& browser) : _browser(browser) {
  Printer::inst()->println(myname + "starting");
}

Keyboard::Commands Keyboard::getType(string cmd) {
  if (cmd.compare("f") == 0) {
    return FindTopic;
  } else if (cmd.compare("d") == 0) {
    return DisplayFile;
  } else if (cmd.compare("p") == 0) {
    return PrintCache;
  } else if (cmd.compare("c") == 0) {
    return ClearCache;
  } else if (cmd.compare("k") == 0) {
    return KillServer;
  } else if (cmd.compare("q") == 0) {
    return Quit;
  }
  return Quit;
}

void Keyboard::main() {
  string cmd;
  string line;
  
  // grab entire line
  for (getline(cin, line); cin.good(); getline(cin, line)) {
    // put line into a string stream, pull out first word
    stringstream ss;
    ss << line;
    ss >> cmd;

    // get command type
    Commands type = getType(cmd);

    switch(type) {
      case PrintCache:
      case ClearCache:
      {
        // check if there are more chars in the line
        string leftover = ss.str().substr(ss.tellg());
        if (!leftover.empty()) {
          Printer::inst()->println(myname + "\"" + leftover + "\"" 
              + " was discarded");
        }

        // pass command to browser
        _browser.keyboard(type);
        break;
      }
      case Quit:
      {
        // check if there are leftover chars
        string leftover = ss.str().substr(ss.tellg());
        if (!leftover.empty()) {
          Printer::inst()->println(myname + "\"" + leftover + "\"" 
              + " was discarded");
        }
        // quit input for loop
        goto inputloop;
      }
      case FindTopic:
      case DisplayFile:
      {
        // try to pull out second arg
        string arg;
        if (!ss.good()) {
          // skip line; not enough params
          Printer::inst()->println(myname + "not enough params. Skipping.");
        } else {
          ss >> arg;
          // check if there are leftovers on the line
          string leftover = ss.str().substr(ss.tellg());
          if (!leftover.empty()) {
            Printer::inst()->println(myname + "\"" + leftover + "\"" 
                + " was discarded");
          }
          // give command to browser
          _browser.keyboard(type, arg);
        }
        break;
      }
      case KillServer:
      {
        // try to pull out second arg
        unsigned int server;
        if (!ss.good()) {
          // skip line; not enough parms
          Printer::inst()->println(myname + "not enough params. Skipping.");
        } else {
          ss >> server;
          // check for left over characters on the line
          string leftover = ss.str().substr(ss.tellg());
          if (!leftover.empty()) {
            Printer::inst()->println(myname + "\"" + leftover + "\"" 
                + " was discarded");
          }
           // send command to browser
          _browser.keyboard(type, server);
        }
        break;
      }
    }
  } inputloop:


  // send quit command to browser
  Printer::inst()->println(myname + "quit received");
  _browser.keyboard(Quit);
}

