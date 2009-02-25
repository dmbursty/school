import javax.swing.JComboBox;


@SuppressWarnings("serial")
public class ZBox extends JComboBox implements ZoomListener{
  private boolean updating = false;
  
  ZBox() {
    super();
    addItem(new Double(0.25));
    addItem(new Double(0.5));
    addItem(new Double(1.0));
    addItem(new Double(2.0));
    addItem(new Double(4.0));
    setSelectedIndex(2);
  }

  public void update(double zoom) {
    updating = true;
    setSelectedItem(new Double(zoom));
    updating = false;
  }
  
  public boolean isUpdating() {
    return updating;
  }
}
