#ifndef APPWINDOW_HPP
#define APPWINDOW_HPP

#include <gtkmm.h>
#include "viewer.hpp"
#include "scene.hpp"

class AppWindow : public Gtk::Window {
public:
  AppWindow(SceneNode* root);
  
protected:

private:
  // A "vertical box" which holds everything in our window
  Gtk::VBox m_vbox;

  // The menubar, with all the menus at the top of the window
  Gtk::MenuBar m_menubar;
  // Each menu itself
  Gtk::Menu m_menu_app;
  Gtk::Menu m_menu_edit;
  Gtk::Menu m_menu_options;
  Gtk::Menu m_menu_mode;

  Gtk::RadioButtonGroup modes;

  SceneNode* root;
  // The main OpenGL area
  Viewer m_viewer;

  // The Label
  Gtk::Label m_label;
};

#endif
