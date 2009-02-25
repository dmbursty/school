#include "LSystemChooser.h"
#include "Application.h"
#include <string.h>

namespace cs349 {

LSystemChooser::LSystemChooser() {
  IteratedSystemsManager* ism = IteratedSystemsManager::GetInstance();
  for (int i = 0; i < ism->GetNumSystems(); i++) {
    IteratedSystem* system = ism->GetSystem(i);
    ChooserItem* item = new ChooserItem(system->GetName(), this, system);
    this->AddComponent(item);
    if (i == 0) selected = item;
  }
  selected->Select();
}

void LSystemChooser::SetBounds(const Rectangle& r) {
  this->bounds = r;
  for (int i = 0; i < children.size(); i++) {
    children[i]->SetBounds(Rectangle(0, 20*i, r.width, 20));
  }
}

void LSystemChooser::SelectMe(ChooserItem* ci) {
  this->selected->Deselect();
  this->selected = ci;
  ci->Select();
  Application::GetInstance()->GetLSystemController()->UseSystem(ci->GetSystem());
  this->Repaint();
}

void LSystemChooser::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  g->DrawRect(localbounds);
}

void ChooserItem::PaintComponent(Graphics* g) {
  Rectangle localbounds;
  GetLocalBounds(&localbounds);
  if (this->GetToggled()) g->Invert();
  g->Invert();
  // So we don't draw over our parent's box
  localbounds.x += 1;
  localbounds.y += 1;
  localbounds.width -= 1;
  localbounds.height -= 1;
  g->FillRect(localbounds);
  g->Invert();
  int x,y;
  x = (GetBounds().width - (strlen(text) * 6)) / 2;
  y = ((GetBounds().height - 10) / 2) + 9;
  g->DrawText(Point(x, y), text);
  if (this->GetToggled()) g->Invert();
}

void ChooserItem::OnToggleOn() {
  chooser->SelectMe(this);
}

void ChooserItem::OnToggleOff() {
  // Don't toggle off, controller will handle this
  this->SilentToggle();
}

}  // cs349
