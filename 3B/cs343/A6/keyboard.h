#ifndef _KEYBOARD_H_
#define _KEYBOARD_H_

#include <uC++.h>

_Task Browser;

_Task Keyboard {
 public:
  enum Commands {FindTopic, DisplayFile, PrintCache,
                 ClearCache, KillServer, Quit};
  Keyboard(Browser &browser);
 
 private:
  /**
   * Given the command string, return the appropriate Commands type from the
   * enum.
   */
  Commands getType(std::string cmd);
  void main();

 private:
  Browser& _browser;
};


#endif
