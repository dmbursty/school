// Author: Michael Terry

#include "Application.h"
#include "Component.h"
#include "PaintEvent.h"
#include "XWindow.h"

namespace cs349
{

Component::Component()
{
  this->parent = NULL;
  this->visible = true;
  this->backgroundColor = Application::GetInstance()->GetBlackColor();
  this->foregroundColor = Application::GetInstance()->GetWhiteColor();
}

Component::~Component()
{
  // Clear up any memory laying around
  for (vector<Component*>::iterator iter=this->children.begin();
      iter != this->children.end(); iter++) {
    delete(*iter);
  }
  this->children.clear();
}

void Component::AddComponent(Component* child)
{
  this->children.push_back(child);
  child->SetParent(this);
}

unsigned long Component::GetBackgroundColor() const
{
  return this->backgroundColor;
}

Rectangle Component::GetBounds() const
{
  return this->bounds;
}

void Component::GetLocalBounds(Rectangle* rect) const
{
  rect->x = 0;
  rect->y = 0;
  rect->width = this->bounds.width - 1;
  rect->height = this->bounds.height - 1;
}

unsigned long Component::GetForegroundColor() const
{
  return this->foregroundColor;
}

Component* Component::GetParent()
{
  return this->parent;
}

XWindow* Component::GetParentWindow()
{
  if (this->parent) {
    return this->parent->GetParentWindow();
  }
  return NULL;
}

bool Component::HandleKeyEvent(const KeyEvent & e)
{
  /*
   * Use this as a basic template for how to handle mouse events.
   * Here, we simply pass key events on to child components to see
   * if they can handle the event. As soon as one component handles
   * the event, the method returns.
   */
  for (vector<Component*>::iterator iter=this->children.begin();
      iter != this->children.end(); iter++) {
    Component* c = (*iter);
    if (c->HandleKeyEvent(e)) {
      return true;
    }
  }
  return false;
}

bool Component::HandleMouseEvent(const MouseEvent & e)
{
  for (vector<Component*>::iterator iter=this->children.begin();
      iter != this->children.end(); iter++) {
    Component* c = (*iter);
    if (c->IsPointInComponent(e.GetWhere())) {
      Rectangle bounds = c->GetBounds();
      AffineTransform at = AffineTransform::GetTranslationMatrix(-1 * bounds.x, -1 * bounds.y);
      Point transformed_point = at.Transform(e.GetWhere());
      MouseEvent child_event(e.GetWindow(), e.GetType(), e.GetButton(), transformed_point);
      if (c->HandleMouseEvent(child_event)) {
        return true;
      }
    }
  }
  if (e.GetType() == MouseEvent::buttonRelease) {
    Application::GetInstance()->TriggerMouseUp();
  } else if (e.GetType() == MouseEvent::mouseMove) {
    Application::GetInstance()->SendMouseMove(e.GetWhere());
  }
  return false;
  // TODO
  /*
   * In this default implementation, you should iterate through the children
   * to see if they can handle the event. As soon as one child handles the event
   * (by returning true), the method should return.
   *
   * Some caveats and things to keep in mind:
   * - You shouldn't be able to interact with things you can't see
   * - Points are expected to be in the coordinate system of the component (see notes in the header file)
   */
}


bool Component::IsPointInComponent(const Point & p) const
{
  return bounds.IsPointInRectangle(p);
}

bool Component::IsVisible() const
{
  return visible;
}

void Component::Paint(Graphics* g)
{
  if (this->visible) {
    PaintComponent(g);
    AffineTransform backup_at = g->GetTransform();
    for (int i = 0; i < children.size(); i++) {
      Rectangle bounds = children[i]->GetBounds();
      AffineTransform at(backup_at);
      at.Translate(bounds.x, bounds.y);
      g->SetTransform(at);
      children[i]->Paint(g);
    }
    g->SetTransform(backup_at);
  }
  // TODO
  /*
   * This is one of the primary methods of the Component class.
   * In this method, you should:
   * - Prepare for drawing (think about things like background/foreground colors, clip, and clear backgrounds)
   * - Call PaintComponent
   * - Make sure children can paint themselves
   * - Restore anything you changed in the Graphics component
   * - Think about things like visibility of the component (is it visible?)
   */
}

void Component::PaintComponent(Graphics* g)
{
  // no-op. Subclasses will override this and implement this themselves
}

void Component::Repaint()
{
  Rectangle localBounds = this->GetBounds();
  localBounds.x = 0;
  localBounds.y = 0;
  this->Repaint(localBounds);
}

void Component::Repaint(const Rectangle & r)
{
  if (this->GetParent() != NULL) {
    Rectangle newRect = r;
    newRect.x += this->GetBounds().x;
    newRect.y += this->GetBounds().y;
    this->GetParent()->Repaint(newRect);
  } else {
    Application::GetInstance()->AddEventToQueue(new PaintEvent(this->GetParentWindow(), r));
  }
}

void Component::SetBackgroundColor(unsigned long c)
{
  this->backgroundColor = c;
}

void Component::SetBounds(const Rectangle & r)
{
  this->bounds = r;
}

void Component::SetForegroundColor(unsigned long c)
{
  this->foregroundColor = c;
}

void Component::SetLocation(const Point & p)
{
  this->bounds.x = p.x;
  this->bounds.y = p.y;
}

void Component::SetParent(Component* parent)
{
  this->parent = parent;
}

void Component::SetSize(int width, int height)
{
  this->bounds.width = width;
  this->bounds.height = height;
}

void Component::SetVisible(bool visible)
{
  this->visible = visible;
  this->Repaint();
}

}
