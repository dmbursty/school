import javax.swing.JButton;


@SuppressWarnings("serial")
public class JStepButton extends JButton implements SystemListener {
  JStepButton(String s) {
    super(s);
  }

  public void update(int step, int maxsteps) {
    if (step == maxsteps) {
      setEnabled(false);
    } else {
      setEnabled(true);
    }
  }
}
