#include "Label.h"
#include <string.h>

namespace cs349
{

void Label::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  int x,y;
  x = (GetBounds().width - (strlen(text) * 6)) / 2;
  y = ((GetBounds().height - 10) / 2) + 9;
  g->DrawText(Point(x, y), text);
}

void LeftLabel::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  int x = 0;
  int y = ((GetBounds().height - 10) / 2) + 9;
  g->DrawText(Point(x, y), GetText());
}

}  // cs349
