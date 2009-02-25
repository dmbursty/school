import javax.swing.JSlider;


@SuppressWarnings("serial")
public class SystemSlider extends JSlider implements SystemListener {
  public SystemSlider(int mode, int min, int max, int init) {
    // TODO Auto-generated constructor stub
    super(mode, min, max, init);
    setMajorTickSpacing(1);
    setPaintTicks(true);
    setSnapToTicks(true);
  }

  public void update(int step, int maxsteps) {
    if (getMaximum() != maxsteps) {
      setMaximum(maxsteps);
      repaint();
    }
    setValue(step);
  }
}
