package a3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PlayPauseButton extends JButton {
  boolean playing;
  myFrame viewer;

  public PlayPauseButton(myFrame mviewer) {
    super("Pause");
    this.viewer = mviewer;
    playing = true;
    
    addActionListener(new ActionListener() {
    
      public void actionPerformed(ActionEvent e) {
        if(playing) {
          setText("Play");
          playing = false;
          viewer.getAnimator().pause();
        } else {
          setText("Pause");
          playing = true;
          viewer.getAnimator().play();
        }
      }
    
    });
  }
}
