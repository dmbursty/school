// Author: Michael Terry

#ifndef KEYEVENT_H_
#define KEYEVENT_H_

#include <X11/Xlib.h>

namespace cs349
{
  class KeyEvent;
}

#include "ComponentEvent.h"

namespace cs349
{

/*
 * Encapsulates a key event.
 * TODO: You will likely need to add functionality that will allow
 *       you to get more information than the key's character.
 *       (Hint: the "Escape" key doesn't represent a character.)
 */
class KeyEvent :  public ComponentEvent
{
 public:
  enum EventType {
    keyPress,
    keyRelease
  };
 private:
  EventType type;
  char     key;

 public:
  KeyEvent(XWindow* window, EventType type, XKeyEvent e);
  virtual ~KeyEvent();

  virtual void  DispatchEvent();
  EventType    GetType() const;
  char      GetChar() const;
};

}

#endif /*KEYEVENT_H_*/
