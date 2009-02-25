/*
 *
 */
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import javax.swing.Timer;

import lsystem.DragonCurve;
import lsystem.FractalFern;
import lsystem.FractalPlant;
import lsystem.IteratedSystem;
import lsystem.KochCurve;
import lsystem.SierpinksiTriangle;

public class IteratedSystemManager extends Observable implements IteratedSystem {

  private HashMap<String, IteratedSystem> iteratedSystems = new HashMap<String, IteratedSystem>();
  private static IteratedSystemManager s_Instance = null;
  private IteratedSystem curSystem = null;
  private Timer timer;
  
  private Vector<TimerEventListener> timerEventListeners = new Vector<TimerEventListener>();
  private Vector<SystemListener> systemListeners = new Vector<SystemListener>();
  private Vector<SystemNameListener> systemNameListeners = new Vector<SystemNameListener>();
  private Vector<ZoomListener> zoomListeners = new Vector<ZoomListener>();
  
  private double zoom = 1.0;
  private Double pan = new Double(0.5,0.5);
  
  public void addTimerEventListener(TimerEventListener l) {
    timerEventListeners.add(l);
  }
  
  public void notifyTimerEventListeners(boolean playing) {
    for (int i = 0; i < timerEventListeners.size(); i++) {
      timerEventListeners.get(i).update(playing);
    }
  }

  public void addSystemListener(SystemListener l) {
    systemListeners.add(l);
  }
  
  public void notifySystemListeners(int step, int maxsteps) {
    for (int i = 0; i < systemListeners.size(); i++) {
      systemListeners.get(i).update(step, maxsteps);
    }
  }
  
  public void addSystemNameListener(SystemNameListener l) {
    systemNameListeners.add(l);
  }
  
  public void notifySystemNameListeners(String name) {
    for (int i = 0; i < systemNameListeners.size(); i++) {
      systemNameListeners.get(i).update(name);
    }
  }
  
  public void addZoomListener(ZoomListener l) {
    zoomListeners.add(l);
  }
  
  public void notifyZoomListeners(double zoom) {
    for (int i = 0; i < zoomListeners.size(); i++) {
      zoomListeners.get(i).update(zoom);
    }
  }

  private IteratedSystemManager() {
    IteratedSystem[] stockSystems = new IteratedSystem[] { new DragonCurve(),
                    new FractalFern(),
                    new FractalPlant(),
                    new KochCurve(),
                    new SierpinksiTriangle() };
    for (IteratedSystem sys : stockSystems)
    {
      this.addLSystem(sys);
      curSystem = sys; // Sets current system to the last one added
    }
    timer = new Timer(500,new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        iterate();
      }
    });
    timer.start();
  }
  public static IteratedSystemManager getInstance() {
    if (s_Instance == null) {
      s_Instance = new IteratedSystemManager();
    }
    return s_Instance;
  }

  /**
   * Adds an iterated system to the manager
   */
  public void addLSystem(IteratedSystem sys) {
    this.iteratedSystems.put(sys.getName(), sys);
  }

  /**
   * Gets the names of all the L-Systems currently installed.
   */
  public Set<String> getLSystemNames() {
    return iteratedSystems.keySet();
  }

  /**
   * Sets the current L-System to the one with the given name.
   * Use getLSystemNames() to get the list of installed L-Systems.
   */
  public void setLSystem(String lSystemName) {
    curSystem = iteratedSystems.get(lSystemName);
    notifySystemNameListeners(curSystem.getName());
    setZoom(1.0);
    if (curSystem.getNumIterations() == 0) {
      setChanged();
      notifyObservers();
    } else {
      reset();
    }
  }
  public Vector<Double> getLines(Rectangle displayArea) {
    return curSystem.getLines(displayArea);
  }
  public Vector<Vector<Double>> getLineSegments(Rectangle displayArea) {
    return curSystem.getLineSegments(displayArea);
  }
  public int getMaxNumIterations() {
    return curSystem.getMaxNumIterations();
  }
  public String getName() {
    return curSystem.getName();
  }
  public int getNumIterations() {
    return curSystem.getNumIterations();
  }
  public int getNumParams() {
    return curSystem.getNumParams();
  }
  public int getParamMax(int paramNum) {
    return curSystem.getParamMax(paramNum);
  }
  public int getParamMin(int paramNum) {
    return curSystem.getParamMin(paramNum);
  }
  public String getParamName(int paramNum) {
    return curSystem.getParamName(paramNum);
  }
  public int getParamValue() {
    return curSystem.getParamValue();
  }
  public Vector<Double> getPoints(Rectangle displayArea) {
    return curSystem.getPoints(displayArea);
  }
  public void iterate() {
    int oldStep = curSystem.getNumIterations();
    curSystem.iterate();
    if (curSystem.getNumIterations() != oldStep) {
      notifySystemListeners(curSystem.getNumIterations(), curSystem.getMaxNumIterations());
      setChanged();
      notifyObservers();
    } else {
      iterateToStep(0);
    }
  }
  public void back() {
    iterateToStep(Math.max(0, curSystem.getNumIterations() - 1));
  }
  public void iterateToStep(int stepNum) {
    if (stepNum != curSystem.getNumIterations()) {
      curSystem.iterateToStep(stepNum);
      notifySystemListeners(stepNum, curSystem.getMaxNumIterations());
      setChanged();
      notifyObservers();
    }
  }
  public void reset() {
    setZoom(1.0);
    if (curSystem.getNumIterations() != 0) {
      curSystem.reset();
      notifySystemListeners(0, curSystem.getMaxNumIterations());
      setChanged();
      notifyObservers();
    }
  }
  public void pause() {
    timer.stop();
    notifyTimerEventListeners(false);
  }
  public void play() {
    timer.start();
    notifyTimerEventListeners(true);
  }
  public boolean isPlaying() {
    return timer.isRunning();
  }
  public void setTimerSpeed(int speed) {
    timer.setDelay(1100 - speed);
  }
  public void setZoom(double zoom) {
    if (zoom <= 0.0) {
      zoom = 1.0;
    } else if (zoom > 15.0) {
      zoom = 15.0;
    }
    this.zoom = zoom;
    this.pan = new Double(0.5,0.5);
    setChanged();
    notifyObservers();
    notifyZoomListeners(zoom);
  }
  public void setZoomWithoutCentre(double zoom) {
    if (zoom <= 0.0) {
      zoom = 1.0;
    } else if (zoom > 15.0) {
      zoom = 15.0;
    }
    this.zoom = zoom;
    setChanged();
    notifyObservers();
    notifyZoomListeners(zoom);
  }
  public double getZoom() {
    return this.zoom;
  }
  public void setPan(Double pan) {
    this.pan = pan;
  }
  public Double getPan() {
    return this.pan;
  }
  public void setParamValue(int paramNum, int value) {
    curSystem.setParamValue(paramNum, value);
  }
  /*
   * TODO
   * You will need to have methods to add and remove listeners
   * to this class (along with field(s) to hold the listeners).
   *
   * You will be defining your own listener interface.
   */
}
