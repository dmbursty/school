import javax.swing.JButton;

@SuppressWarnings("serial")
public class JBackButton extends JButton implements SystemListener {
  JBackButton(String s) {
    super(s);
    setEnabled(false);
  }

  public void update(int step, int maxsteps) {
    if (step == 0) {
      setEnabled(false);
    } else {
      setEnabled(true);
    }
  }
}
