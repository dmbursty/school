package a3;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.undo.UndoManager;

public class Application{
  JDesktopPane desktop;
  public JFrame frame;
  
  public JMenuItem newItem;
  public JMenuItem openItem;
  public JMenuItem closeItem;
  public JMenuItem saveItem;
  public JMenuItem saveAsItem;
  public JMenuItem quitItem;

  public JMenuItem undoItem;
  public JMenuItem redoItem;
  public JMenuItem cutItem;
  public JMenuItem copyItem;
  public JMenuItem pasteItem;
  public JMenuItem deleteItem;
  public JMenuItem selectAllItem;
  
  final ActionListener saveAs = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      JInternalFrame f = desktop.getSelectedFrame();
      if (f != null) {
        MocapViewer v = ((MocapViewer)f);
        MarkerSet set = v.mset;
        JFileChooser fc = new JFileChooser();
        int ret = fc.showSaveDialog(desktop);
        if (ret == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          if (!file.getName().endsWith("uwm")) file = new File(file.getPath() + ".uwm");
          if(!file.exists()) {
            try {
              file.createNewFile();
            } catch (IOException e1) {}
          }
          try {
            set.writeMarkerSet(file);
          } catch (IOException e1) {}
          v.title = file.getName();
          v.file = file;
          v.saveable = true;
          v.resetChanged();
          Disabler.getInstance().saveableUpdate(v.changed);
        }
      }
    }
  };;
  final ActionListener save = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      JInternalFrame f = desktop.getSelectedFrame();
      if (f != null) {
        MocapViewer v = ((MocapViewer)f);
        if (v.saveable) {
          try {
            v.mset.writeMarkerSet(v.file);
            v.resetChanged();
            Disabler.getInstance().saveableUpdate(v.changed);
          } catch (IOException e1) {}
        } else {
          saveAs.actionPerformed(null);
        }
      }
    }
  };
  
  
  // Derrived from Sun InternalFrameDemo Tutorial
  @SuppressWarnings("static-access")
  public void makeGUI() {
    frame = new JFrame("MoCap Mashups");
    frame.setDefaultLookAndFeelDecorated(true);
    frame.setPreferredSize(new Dimension(800,600));
    
    desktop = new JDesktopPane();

    // Make a starter Mocap Viewer
    //createMocapViewer(new File("a3/90_17.c3d"));
    createClipViewer();
    
    // Menu Bar
    frame.setJMenuBar(createMenuBar());
    
    Disabler.getInstance().setApplication(this);
    Disabler.getInstance().noSelection();
    
    frame.setContentPane(desktop);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
  
  private JMenuBar createMenuBar() {
    JMenuBar bar = new JMenuBar();
    
    JMenu menu;
    
    // File
    menu = new JMenu("File");
    bar.add(menu);

    newItem = new JMenuItem("New");
    newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
    newItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        createMocapViewer(null);
      }
    });
    menu.add(newItem);
    
    openItem = new JMenuItem("Open");
    openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    openItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        int ret = fc.showOpenDialog(desktop);
        if (ret == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          createMocapViewer(file);
        }
      }
    });
    menu.add(openItem);
    
    closeItem = new JMenuItem("Close");
    closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
    closeItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          f.dispose();
          JInternalFrame[] frames = desktop.getAllFrames();
          if (frames.length > 0) desktop.setSelectedFrame(frames[0]);
        }
      }
    });
    menu.add(closeItem);

    
    saveItem = new JMenuItem("Save");
    saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    saveItem.addActionListener(save);
    menu.add(saveItem);
    
    saveAsItem = new JMenuItem("Save As...");
    saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));

    saveAsItem.addActionListener(saveAs);
    menu.add(saveAsItem);
    
    menu.addSeparator();
    
    quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    menu.add(quitItem);
    
    
    // Edit Menu
    menu = new JMenu("Edit");
    bar.add(menu);

    undoItem = new JMenuItem("Undo");
    undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    undoItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          UndoManager um = ((MocapViewer)f).undoManager;
          um.undo();
          Disabler.getInstance().undoUpdate(um.canUndo(), um.canRedo());
        }
      }
    });
    menu.add(undoItem);

    redoItem = new JMenuItem("Redo");
    redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
    redoItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          UndoManager um = ((MocapViewer)f).undoManager;
          um.redo();
          Disabler.getInstance().undoUpdate(um.canUndo(), um.canRedo());
        }
      }
    });
    menu.add(redoItem);
    
    menu.addSeparator();

    cutItem = new JMenuItem("Cut");
    cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    cutItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          ((MocapViewer)f).cut();
        }
      }
    });
    menu.add(cutItem);

    copyItem = new JMenuItem("Copy");
    copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    copyItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          ((myFrame)f).copy();
        }
      }
    });
    menu.add(copyItem);

    pasteItem = new JMenuItem("Paste");
    pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    pasteItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          ((MocapViewer)f).paste();
        }
      }
    });
    menu.add(pasteItem);

    deleteItem = new JMenuItem("Delete");
    deleteItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          ((MocapViewer)f).delete();
        }
      }
    });
    menu.add(deleteItem);
    
    menu.addSeparator();

    selectAllItem = new JMenuItem("Select All");
    selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
    selectAllItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JInternalFrame f = desktop.getSelectedFrame();
        if (f != null) {
          ((myFrame)f).selectAll();
        }
      }
    });
    menu.add(selectAllItem);
    
    
    return bar;
  }

  public MocapViewer createMocapViewer(File file) {
    // Mocap Viewer
    final MocapViewer viewer;
    try {
      viewer = new MocapViewer(file);
      viewer.addInternalFrameListener(new InternalFrameListener() {
        public void internalFrameClosing(InternalFrameEvent e) {
          if (viewer.changed != 0) {
            int response = JOptionPane.showConfirmDialog(viewer, "The mashup " + viewer.title + " has not been saved.  Save it now?",
                                                        "Do you wish to save?", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
              save.actionPerformed(null);
            }
          }
          viewer.dispose();
        }
        public void internalFrameActivated(InternalFrameEvent e) {Disabler.getInstance().selectedViewer(viewer);}
        public void internalFrameDeactivated(InternalFrameEvent e) {Disabler.getInstance().noSelection();}
        
        public void internalFrameOpened(InternalFrameEvent e) {}
        public void internalFrameIconified(InternalFrameEvent e) {}
        public void internalFrameDeiconified(InternalFrameEvent e) {}
        public void internalFrameClosed(InternalFrameEvent e) {}
      });
      viewer.setSize(new Dimension(400,300));
      viewer.setLocation((int)(Math.random()*400),(int)(Math.random()*300));
      desktop.add(viewer);
      viewer.setVisible(true);
    } catch (IOException e) {return null;}
    return viewer;
  }
  
  public void createClipViewer(){
    final ClipViewer viewer = new ClipViewer();
    viewer.addInternalFrameListener(new InternalFrameListener() {
      public void internalFrameClosing(InternalFrameEvent e) {}
      public void internalFrameActivated(InternalFrameEvent e) {Disabler.getInstance().selectedClipViewer(viewer);}
      public void internalFrameDeactivated(InternalFrameEvent e) {Disabler.getInstance().noSelection();}
      
      public void internalFrameOpened(InternalFrameEvent e) {}
      public void internalFrameIconified(InternalFrameEvent e) {}
      public void internalFrameDeiconified(InternalFrameEvent e) {}
      public void internalFrameClosed(InternalFrameEvent e) {}
    });
    viewer.setSize(190,530);
    viewer.setLocation(600,0);
    desktop.add(viewer);
    viewer.setVisible(true);
  }
  
  public static MarkerRenderer getRenderer() {
    return new DefaultMarkerRenderer(5);
  }
  
  public static void main(String[] args) {
    final Application a = Application.getInstance();
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        a.makeGUI();
      }
    });
  }
  
  private static Application a = null;
  public static Application getInstance() {
    if (a == null) a = new Application();
    return a;
  }
}