#include "appwindow.hpp"

AppWindow::AppWindow(SceneNode* root) : root(root), m_viewer(root)
{
  set_title("Advanced Ergonomics Laboratory");

  // A utility class for constructing things that go into menus, which
  // we'll set up next.
  using Gtk::Menu_Helpers::MenuElem;
  using Gtk::Menu_Helpers::CheckMenuElem;
  using Gtk::Menu_Helpers::RadioMenuElem;
  
  // Set up the application menu
  // The slot we use here just causes AppWindow::hide() on this,
  // which shuts down the application.
  m_menu_app.items().push_back(MenuElem("_Quit", Gtk::AccelKey("q"),
    sigc::mem_fun(*this, &AppWindow::hide)));
  m_menu_app.items().push_back(MenuElem("Reset Pos_ition", Gtk::AccelKey("i"),
    sigc::mem_fun(m_viewer, &Viewer::resetPosition)));
  m_menu_app.items().push_back(MenuElem("Reset _Orientation", Gtk::AccelKey("o"),
    sigc::mem_fun(m_viewer, &Viewer::resetOrientation)));
  m_menu_app.items().push_back(MenuElem("Reset Joi_nts", Gtk::AccelKey("n"),
    sigc::mem_fun(m_viewer, &Viewer::resetJoints)));
  m_menu_app.items().push_back(MenuElem("Reset _All", Gtk::AccelKey("a"),
    sigc::mem_fun(m_viewer, &Viewer::resetAll)));
  
  m_menu_edit.items().push_back(MenuElem("_Undo", Gtk::AccelKey("u"),
    sigc::mem_fun(m_viewer, &Viewer::undo)));
  m_menu_edit.items().push_back(MenuElem("_Redo", Gtk::AccelKey("r"),
    sigc::mem_fun(m_viewer, &Viewer::redo)));

  m_menu_options.items().push_back(CheckMenuElem("_Circle", Gtk::AccelKey("c"),
    sigc::mem_fun(m_viewer, &Viewer::toggleTrackball)));
  m_menu_options.items().push_back(CheckMenuElem("_Z-Buffer", Gtk::AccelKey("z"),
    sigc::mem_fun(m_viewer, &Viewer::toggleZBuffer)));
  m_menu_options.items().push_back(CheckMenuElem("_Backface Cull", Gtk::AccelKey("b"),
    sigc::mem_fun(m_viewer, &Viewer::toggleBackface)));
  m_menu_options.items().push_back(CheckMenuElem("_Frontface Cull", Gtk::AccelKey("f"),
    sigc::mem_fun(m_viewer, &Viewer::toggleFrontface)));


  m_menu_mode.items().push_back(RadioMenuElem(modes, "_Position/Orientation", Gtk::AccelKey("p"),
    sigc::mem_fun(m_viewer, &Viewer::modePosition)));
  m_menu_mode.items().push_back(RadioMenuElem(modes, "_Joints", Gtk::AccelKey("j"),
    sigc::mem_fun(m_viewer, &Viewer::modeJoint)));


  // Set up the menu bar
  m_menubar.items().push_back(Gtk::Menu_Helpers::MenuElem("Application", m_menu_app));
  m_menubar.items().push_back(Gtk::Menu_Helpers::MenuElem("Edit", m_menu_edit));
  m_menubar.items().push_back(Gtk::Menu_Helpers::MenuElem("Options", m_menu_options));
  m_menubar.items().push_back(Gtk::Menu_Helpers::MenuElem("Mode", m_menu_mode));
  
  // Pack in our widgets
  
  // First add the vertical box as our single "top" widget
  add(m_vbox);

  // Put the menubar on the top, and make it as small as possible
  m_vbox.pack_start(m_menubar, Gtk::PACK_SHRINK);

  // Put the viewer below the menubar. pack_start "grows" the widget
  // by default, so it'll take up the rest of the window.
  m_viewer.set_size_request(300, 300);
  m_vbox.pack_start(m_viewer);

  // Add in the label
  m_label.set_size_request(300, 30);
  m_vbox.pack_start(m_label);
  m_label.set_text("Undo: 0     Redo: 0");
  m_viewer.set_label(&m_label);

  show_all();
}
