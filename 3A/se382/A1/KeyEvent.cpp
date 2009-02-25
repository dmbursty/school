// Author: Michael Terry

#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include "KeyEvent.h"
#include "Application.h"
#include "XWindow.h"

namespace cs349
{

KeyEvent::KeyEvent(XWindow* window, KeyEvent::EventType type, XKeyEvent e) : ComponentEvent(window)
{
  this->type = type;
  XLookupString(&e, &(this->key), 1, NULL, NULL);
}

KeyEvent::~KeyEvent()
{
}

void KeyEvent::DispatchEvent()
{
  Application::GetInstance()->HandleKeyEvent(*this);
}

char KeyEvent::GetChar() const
{
  return this->key;
}

KeyEvent::EventType KeyEvent::GetType() const
{
  return this->type;
}

}
