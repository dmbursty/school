#include "LSystemViewer.h"

#include "Application.h"

namespace cs349
{

LSystemViewer::LSystemViewer() {
  Application::GetInstance()->MakeLSController(this);
  IteratedSystemsManager* ism = IteratedSystemsManager::GetInstance();
  this->system = ism->GetSystem(0);
  this->zoom = 1;
  this->pan = Point(0,0);
  this->pan_reference = Point(0,0);
}

void LSystemViewer::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  g->DrawRect(localbounds);
  Rectangle clip_backup = g->GetClipRect();
  g->SetClipRect(localbounds);
  localbounds.x += 1;
  localbounds.y += 1;
  localbounds.width -= 2;
  localbounds.height -= 2;
  g->Invert();
  g->FillRect(localbounds);
  g->Invert();

  localbounds.x -= this->zoom/2;
  localbounds.y -= this->zoom/2;
  localbounds.width += this->zoom;
  localbounds.height += this->zoom;

  localbounds.x += this->pan.x;
  localbounds.y += this->pan.y;

  g->DrawPoints(system->GetPoints(localbounds));
  g->DrawLineSegments(system->GetLineSegments(localbounds));
  g->DrawPolygon(system->GetLines(localbounds));
  g->SetClipRect(clip_backup);
}

void LSystemViewer::SetSystem(IteratedSystem* system) {
  this->system = system;
  system->Reset();
  ResetView();
  Repaint();
}

bool LSystemViewer::HandleMouseEvent(const MouseEvent& e) {
  if (e.GetType() == MouseEvent::buttonPress) {
    switch(e.GetButton()) {
      case 4: this->zoom += 20; Repaint(); return true;
      case 5: this->zoom -= 20; Repaint(); return true;
      default:
        if (!this->pressed) this->pressed = true;
        this->pan_reference = Point(e.GetWhere().x - pan.x, e.GetWhere().y - pan.y);
        Application::GetInstance()->SubscribeForMouseUp(this);
        Application::GetInstance()->SubscribeForMouseMove(this);
        return true;
    }
  } else if (e.GetType() == MouseEvent::buttonRelease) {
    switch(e.GetButton()) {
      case 4: break;
      case 5: break;
      default:
        if (this->pressed) this->pressed = false; return true;
    }
  }
  return false;
}

void LSystemViewer::MouseMoved(const Point& p) {
  if (this->pressed) {
    this->pan = Point(p.x - pan_reference.x, p.y - pan_reference.y);
    Repaint();
  }
}

void LSystemViewer::UncaughtMouseUp() {
  if (this->pressed) this->pressed = false;
}

void LSystemViewer::ResetView() {
  this->zoom = 0;
  this->pan = Point(0,0);
  this->pan_reference = Point(0,0);
}

}  // cs349
