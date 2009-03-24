package a3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Animator {
  public final int FPS = 30;
  
  private Timer timer;
  private boolean playing;
  private myFrame viewer;
  
  public Animator(myFrame mviewer) {
    this.viewer = mviewer;
    this.playing = true;
    
    timer = new Timer(1000 / FPS, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        viewer.nextFrame();
      }
    });
    timer.start();
  }
  
  // Control the animation
  public void play(){
    timer.start();
    playing = true;
  }
  public void pause(){
    timer.stop();
    playing = false;
  }
  
  // Temporary hold on animation
  public void hold(){
    timer.stop();
  }
  public void unhold(){
    if (playing) timer.start();
  }
}
