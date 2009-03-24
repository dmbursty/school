package a3;

public class Disabler {
  private static Disabler d = null;
  private Disabler() {}
  private Application app = null;
  
  public static Disabler getInstance() {
    if (d == null) d = new Disabler();
    return d;
  }
  
  public void setApplication(Application a) {
    app = a;
  }
  
  /*
   * Items are:
   * newItem    saveItem
   * openItem   saveAsItem
   * closeItem  quitItem
   * 
   * cutItem    undoItem
   * copyItem   redoItem
   * pasteItem  selectAllItem
   * deleteItem
   */
  
  // Call at the start of program
  public void noSelection() {
    app.newItem.setEnabled(true);
    app.openItem.setEnabled(true);
    app.closeItem.setEnabled(false);
    app.saveItem.setEnabled(false);
    app.saveAsItem.setEnabled(false);
    app.quitItem.setEnabled(true);
    
    app.cutItem.setEnabled(false);
    app.copyItem.setEnabled(false);
    app.pasteItem.setEnabled(false);
    app.deleteItem.setEnabled(false);
    app.undoItem.setEnabled(false);
    app.redoItem.setEnabled(false);
    app.selectAllItem.setEnabled(false);
  }
  
  public void selectedViewer(MocapViewer v) {
    app.newItem.setEnabled(true);
    app.openItem.setEnabled(true);
    app.closeItem.setEnabled(true);
    saveableUpdate(v.changed);
    app.saveAsItem.setEnabled(true);
    app.quitItem.setEnabled(true);
    
    // Dependent on selection
    //selectionUpdate(v.slider.right - v.slider.left);
    selectionUpdate(1);
    app.pasteItem.setEnabled(true);
    
    // Dependent on undoManager
    undoUpdate(v.undoManager.canUndo(), v.undoManager.canRedo());
    
    app.selectAllItem.setEnabled(true);
  }
  
  public void saveableUpdate(int changed) {
    app.saveItem.setEnabled(changed != 0);
  }
  
  public void selectionUpdate(int size) {
    app.cutItem.setEnabled(size != 0);
    app.copyItem.setEnabled(size != 0);
    app.deleteItem.setEnabled(size != 0);
  }
  
  public void undoUpdate(boolean undo, boolean redo) {
    app.undoItem.setEnabled(undo);
    app.redoItem.setEnabled(redo);
  }
  
  public void selectedClipViewer(ClipViewer v) {

    app.newItem.setEnabled(true);
    app.openItem.setEnabled(true);
    app.closeItem.setEnabled(false);
    saveableUpdate(0);
    app.saveAsItem.setEnabled(false);
    app.quitItem.setEnabled(true);
    
    // Dependent on selection
    selectionUpdate(0);
    app.copyItem.setEnabled(true);
    app.pasteItem.setEnabled(false);
    
    // Dependent on undoManager
    undoUpdate(false, false);
    
    app.selectAllItem.setEnabled(true);
  }
}
