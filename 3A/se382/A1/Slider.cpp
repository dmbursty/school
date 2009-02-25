#include "Slider.h"
#include "Application.h"

namespace cs349
{

Slider::Slider() : numTicks(10), currentTick(0) {}

void Slider::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  g->Invert();
  g->FillRect(localbounds);
  g->Invert();
  // Fix localbounds so we don't draw out of our slider
  localbounds.x += 2;
  localbounds.width -= 4;
  int y = localbounds.height / 2;
  // Draw midline
  g->DrawLine(Point(localbounds.x, y), Point(localbounds.x + localbounds.width, y));
  // Draw ticks
  for (double i = 0.0; i <= this->numTicks; i++) {
    int x = localbounds.x + (i / this->numTicks) * localbounds.width;
    g->DrawLine(Point(x, y+3), Point(x, y-3));
  }
  // Draw bar
  int x = localbounds.x + (static_cast<double>(this->currentTick) / this->numTicks) * localbounds.width;
  g->FillRect(Rectangle(x-2, y-10, 5, 21));
}

bool Slider::HandleMouseEvent(const MouseEvent& e) {
  if (e.GetType() == MouseEvent::buttonPress) {
    Rectangle localbounds;
    GetLocalBounds(&localbounds);
    localbounds.x += 2;
    localbounds.width -= 4;
    // Check if in rectangle
    int current_x = localbounds.x + (static_cast<double>(this->currentTick) / this->numTicks) * localbounds.width;
    int dist = e.GetWhere().x - current_x;
    if (-2 <= dist && 2 >= dist) {
      this->pressed = true;
      Application::GetInstance()->SubscribeForMouseMove(this);
      Application::GetInstance()->SubscribeForMouseUp(this);
    } else {
      // Translate to tick
      int tick = ((static_cast<double>(e.GetWhere().x) / localbounds.width) * numTicks) + 0.5;
      currentTick = tick;
      OnSelectTick(tick);
    }
  }
  return false;
}

void Slider::MouseMoved(const Point& p) {
  if (this->pressed) {
    Rectangle localbounds;
    GetLocalBounds(&localbounds);
    localbounds.x += 2;
    localbounds.width -= 4;
    int tick = ((static_cast<double>(p.x) / localbounds.width) * numTicks) + 0.5;
    if (tick < 0) tick = 0;
    if (tick > numTicks) tick = numTicks;
    if (tick != currentTick) {
      currentTick = tick;
      OnSelectTick(tick);
    }
  }
}

void Slider::UncaughtMouseUp() {
  if (this->pressed) {
    this->pressed = false;
  }
}

void Slider::SetNumTicks(int i) {
  this->numTicks = i;
  this->currentTick = 0;
}

void Slider::JumpToTick(int i) {
  if (i < 0) {
    i = 0;
  } else if (i > this->numTicks) {
    i = this->numTicks;
  }
  this->currentTick = i;
}

void Slider::OnSelectTick(int i) {
  if (currentTick != i) {
    currentTick = i;
    Repaint();
  }
}

}  // cs349
