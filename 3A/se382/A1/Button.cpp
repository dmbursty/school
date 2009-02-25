#include <string.h>
#include "Button.h"
#include "Application.h"

namespace cs349
{

void Button::Invert() {
  this->inverted = !this->inverted;
  this->Repaint();
}

void Button::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  // Draw the border
  if (this->inverted) {
    g->Invert();
  }
  g->Invert();
  g->FillRect(localbounds);
  g->Invert();
  g->DrawRect(localbounds);
  if (!this->disabled) {
    // Only draw one border for disabled buttons
    localbounds.x += 2;
    localbounds.y += 2;
    localbounds.width -= 4;
    localbounds.height -= 4;
    g->DrawRect(localbounds);
  }
  if (this->disabled) {
    // Draw an X if disabled
    localbounds.x += 2;
    localbounds.y += 2;
    localbounds.width -= 4;
    localbounds.height -= 4;
    for (int i = -1; i <= 1; i+=2) {
      // Bottom left, to top right
      g->DrawLine(Point(localbounds.x + i,
                        localbounds.y + localbounds.height + i),
                  Point(localbounds.x + localbounds.width + i,
                        localbounds.y + i));
      // Top left, to bottom right
      g->DrawLine(Point(localbounds.x + i, localbounds.y - i),
                  Point(localbounds.x + localbounds.width + i,
                        localbounds.y + localbounds.height - i));
    }
  }
  // Draw the text
  int x,y;
  x = (GetBounds().width - (strlen(GetText()) * 6)) / 2;
  y = ((GetBounds().height - 10) / 2) + 9;
  g->DrawText(Point(x, y), GetText());
  if (this->inverted) {
    g->Invert();
  }
}

bool Button::HandleMouseEvent(const MouseEvent& e) {
  if (e.GetType() == MouseEvent::buttonPress) {
    if (this->disabled) return false;
    this->pressed = true;
    this->Invert();
    Application::GetInstance()->SubscribeForMouseUp(this);
  } else if (e.GetType() == MouseEvent::buttonRelease) {
    if (this->pressed) {
      // Click
      this->pressed = false;
      this->Invert();
      // Only click if we are not disabled
      if (!this->disabled) {
        OnClick();
      }
    } else {
      // Unexpected Release
      return false;
    }
  }
  return true;
}

void Button::OnClick() {
  // Extend me
}

void Button::UncaughtMouseUp() {
  if (this->pressed) {
    this->pressed = false;
    this->Invert();
  } else {
    // This is actually expected if you mouse down on nothing
  }
}

const char* Button::GetText() {
  return this->text;
}


void ToggleButton::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  if (this->GetToggled()) g->Invert();
  g->Invert();
  // So we don't draw over our parent's box
  g->FillRect(localbounds);
  g->Invert();
  g->DrawRect(localbounds);
  localbounds.x += 2;
  localbounds.y += 2;
  localbounds.width -= 4;
  localbounds.height -= 4;
  g->DrawRect(localbounds);
  int x,y;
  x = (GetBounds().width - (strlen(GetText()) * 6)) / 2;
  y = ((GetBounds().height - 10) / 2) + 9;
  g->DrawText(Point(x, y), GetText());
  if (this->GetToggled()) g->Invert();
}

void ToggleButton::OnClick() {
  cout << "click\n";
  this->toggled = !this->toggled;
  if (this->toggled) {
    this->OnToggleOn();
  } else {
    this->OnToggleOff();
  }
  this->Invert();
}

const char* ToggleButton::GetText() {
  return this->toggled ? this->alttext : this->text;
}

void ToggleButton::SilentToggle() {
  this->toggled = !this->toggled;
  this->Repaint();
}

void ToggleButton::TriggerToggle() {
  this->toggled = !this->toggled;
  this->toggled ? this->OnToggleOn() : this->OnToggleOff();
  this->Repaint();
}

void ToggleButton::OnToggleOn() { cout << "Toggled On\n"; }
void ToggleButton::OnToggleOff() { cout << "Toggled Off\n"; }

}  // cs349
