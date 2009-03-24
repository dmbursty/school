package a3;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

// Mocap Viewer should include a MarkerView, a play/pause button
// and the tri-slider
@SuppressWarnings("serial")
public class MocapViewer extends JInternalFrame implements myFrame {
  public final int FPS = 30;
  
  public boolean saveable;
  public int changed;
  public String title;
  public File file;
  
  public MarkerSet mset;
  private MarkerView view;
  public Animator animator;
  
  public UndoManager undoManager;
  
  public final TriSlider slider;
  
  private class DeleteEdit extends AbstractUndoableEdit {
    private MarkerSet frames;
    int location;
    
    public DeleteEdit(MarkerSet frames, int location) {
      super();
      this.frames = frames;
      this.location = location;
    }

    public void undo() {
      super.undo();
      decChanged();
      Disabler.getInstance().saveableUpdate(changed);
      slider.curr = location;
      mset.pasteRange(location, frames);
      slider.paste(frames.getNumFrames());
      selectAll();
    }
    
    public void redo() {
      super.redo();
      incChanged();
      Disabler.getInstance().saveableUpdate(changed);
      slider.left = location;
      slider.right = location + frames.getNumFrames() - 1;
      mset.cutRange(slider.left, slider.right + 1);
      slider.delete();
      selectAll();
    }
  }
  
  private class AddEdit extends AbstractUndoableEdit {
    private MarkerSet frames;
    int location;
    
    public AddEdit(MarkerSet frames, int location) {
      super();
      this.frames = frames;
      this.location = location;
    }

    public void redo() {
      super.redo();
      incChanged();
      Disabler.getInstance().saveableUpdate(changed);
      slider.curr = location;
      mset.pasteRange(location, frames);
      slider.paste(frames.getNumFrames());
      selectAll();
    }
    
    public void undo() {
      super.undo();
      decChanged();
      Disabler.getInstance().saveableUpdate(changed);
      slider.left = location;
      slider.right = location + frames.getNumFrames() - 1;
      mset.cutRange(slider.left, slider.right + 1);
      slider.delete();
      selectAll();
    }
  }
  
  public void resetChanged() {
    changed = 0;
    updateTitle();
  }
  
  public void incChanged() {
    changed++;
    updateTitle();
  }
  
  public void decChanged() {
    changed--;
    updateTitle();
  }
  
  public void updateTitle() {
    if (changed == 0) {
      setTitle("MoCap Viewer: " + title);
    } else {
      setTitle("MoCap Viewer: * " + title);
    }
  }
  
  public void cut() {
    MarkerSet cutRange = mset.cutRange(slider.left, slider.right + 1);
    UndoableEdit edit = new DeleteEdit(cutRange, slider.left);
    undoManager.addEdit(edit);
    incChanged();
    Disabler.getInstance().undoUpdate(undoManager.canUndo(), undoManager.canRedo());
    Disabler.getInstance().saveableUpdate(changed);
    Clipboard.getInstance().setItem(cutRange);
    slider.delete();
    selectAll();
  }

  public void copy() {
    Clipboard.getInstance().setItem(mset.copyRange(slider.left, slider.right));
  }
  
  public void paste() {
    MarkerSet paste = Clipboard.getInstance().getItem();
    UndoableEdit edit = new AddEdit(paste, slider.curr);
    undoManager.addEdit(edit);
    incChanged();
    Disabler.getInstance().undoUpdate(undoManager.canUndo(), undoManager.canRedo());
    Disabler.getInstance().saveableUpdate(changed);
    mset.pasteRange(slider.curr, paste);
    slider.paste(paste.getNumFrames());
  }
  
  public void delete() {
    MarkerSet deleted = mset.cutRange(slider.left, slider.right + 1);
    UndoableEdit edit = new DeleteEdit(deleted, slider.left);
    undoManager.addEdit(edit);
    incChanged();
    Disabler.getInstance().undoUpdate(undoManager.canUndo(), undoManager.canRedo());
    Disabler.getInstance().saveableUpdate(changed);
    slider.delete();
    selectAll();
  }
  
  public void selectAll() {
    slider.setLeft(slider.min);
    slider.setRight(slider.max);
    slider.setCurr(slider.left);
  }

  public MocapViewer(File file) throws IOException {
    super("MoCap Viewer", true, true, true, true);
    changed = 0;
    saveable = false;
    
    // Enclosing Panel so we can specify layout manager
    final JPanel viewer = new JPanel();
    add(viewer);
    viewer.setLayout(new BoxLayout(viewer, BoxLayout.Y_AXIS));
    
    // Marker View
    if (file != null) {
      if (file.getName().endsWith("uwm")) {
        this.file = file;
        saveable = true;
        try {
          mset = MarkerSet.readMarkerSetFromUWMFile(file);
        } catch (ClassNotFoundException e1) {}
      } else {
        mset = MarkerSet.readMarkerSetFromC3D(file);
      }
      title = file.getName();
      setTitle("MoCap Viewer: " + file.getName());
    } else {
      mset = new MarkerSet();
      changed = 1;
      title = "New Mashup";
      setTitle("MoCap Viewer: * New Mashup");
    }
    
    view = new MarkerView(mset, Application.getRenderer());
    view.setPreferredSize(new Dimension(400,300));
    viewer.add(view);
    
    // Controls
    JPanel controls = new JPanel();
    controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
    
    // Play Pause Button
    JButton playPauseButton = new PlayPauseButton(this);
    playPauseButton.setPreferredSize(new Dimension(80,40));
    
    // Slider
    this.slider = new TriSlider(this, 0, mset.getNumFrames()-1, mset.getCurFrame());
    mset.addPropertyChangeListener("CUR_FRAME_PROPERTY", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        slider.setCurr((Integer)evt.getNewValue());
      }
    });
    slider.addCurrentFrameListener(new CurrentFrameListener() {
      public void update() {mset.setCurFrame(slider.curr);}
    });
    slider.setPreferredSize(new Dimension(0, 0));
    slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    controls.add(Box.createRigidArea(new Dimension(5,0)));
    controls.add(playPauseButton);
    controls.add(Box.createRigidArea(new Dimension(5,0)));
    controls.add(slider);
    controls.add(Box.createRigidArea(new Dimension(5,0)));
    
    viewer.add(controls);
    
    this.animator = new Animator(this);
    this.undoManager = new UndoManager();
  }
  
  public void nextFrame() {
    int newFrame = mset.getCurFrame() + 1;
    if (newFrame >= slider.right) newFrame = slider.left;
    mset.setCurFrame(newFrame);
  }

  public Animator getAnimator() {
    return this.animator;
  }
}
