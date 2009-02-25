import javax.swing.JButton;

@SuppressWarnings("serial")
public class JPPButton extends JButton implements TimerEventListener{
  public JPPButton(String s) {
    super(s);
  }

  public void update(boolean playing) {
    if (playing) {
      setText("Pause");
    } else {
      setText("Play");
    }
  }
}
