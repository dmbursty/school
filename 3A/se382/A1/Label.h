// Author: Daniel Burstyn
#ifndef LABEL_H_
#define LABEL_H_

#include "Component.h"
#include <string.h>

namespace cs349
{

class Label : public Component
{
 private:
  char* text;

 protected:
  virtual void PaintComponent(Graphics* g);

 public:
  Label(char* text) {
    this->text = new char[100];
    memset(this->text, '\0', 100);
    strcpy(this->text, text);
  }
  virtual ~Label() {}
  const char* GetText() {return text;}
  void SetText(const char* text) {strcpy(this->text,text); Repaint();}
};

class LeftLabel : public Label
{
 protected:
  virtual void PaintComponent(Graphics* g);
 public:
  LeftLabel(char* text) : Label(text) {}
};

}  // cs349

#endif /*LABEL_H_*/
