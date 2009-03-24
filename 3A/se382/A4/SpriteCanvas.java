/*
 *
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


/**
 * A canvas that draws sprites and provides capabilities for recording
 * events and playing them back.
 * 
 * Michael Terry
 */
@SuppressWarnings("serial")
public class SpriteCanvas extends JPanel {

    /**
     * Tracks our current interactive mode
     */
    private enum InteractionMode {
        IDLE,
        RECORDING,
        PLAYBACK
    }

    private Vector<Sprite>          sprites             = new Vector<Sprite>(); // All sprites we're managing
    private Sprite                  interactiveSprite   = null;                 // Sprite with which user is interacting
    private InteractionMode         interactionMode     = InteractionMode.IDLE; // Current interactive mode
    private Vector<Serializable>    eventStream         = null;                 // Event stream for recording events
    private long                    lastTime            = -1;                   // Time of last event
    
    public SpriteCanvas() {
        initialize();
    }

    private void initialize() {
        // Install our event handlers
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                handleMousePress(e);
            }
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {
          public void mouseWheelMoved(MouseWheelEvent e) {
            handleMouseWheelMoved(e);
          }
        });
    }
    
    /**
     * Handle mouse press events from user or demo file
     */
    private void handleMouseWheelMoved(java.awt.event.MouseWheelEvent e) {
      for (Sprite sprite : sprites) {
          interactiveSprite = sprite.getSpriteHit(e);
          if (interactiveSprite != null) {
              recordEvent(e);
              interactiveSprite.handleMouseWheelMovedEvent(e);
              break;
          }
      }
      repaint();
    }
    
    private void handleMousePress(java.awt.event.MouseEvent e) {
        for (Sprite sprite : sprites) {
            interactiveSprite = sprite.getSpriteHit(e);
            if (interactiveSprite != null) {
                recordEvent(e);
                interactiveSprite.handleMouseDownEvent(e);
                break;
            }
        }
    }
    
    /**
     * Handle mouse released events from user or demo file
     */
    private void handleMouseReleased(MouseEvent e) {
        if (interactiveSprite != null) {
            recordEvent(e);
            interactiveSprite.handleMouseUp(e);
            repaint();
        }
        interactiveSprite = null;
    }
    
    /**
     * Handle mouse dragged events from user or demo file
     */
    private void handleMouseDragged(MouseEvent e) {
        if (interactiveSprite != null) {
            recordEvent(e);
            interactiveSprite.handleMouseDragEvent(e);
            repaint();
        }
    }

    /**
     * Record an event, but only if recording is turned on
     */
    private void recordEvent(MouseEvent event) {
        if (interactionMode.equals(InteractionMode.RECORDING)) {
            long thisTime = System.currentTimeMillis();
            eventStream.add(thisTime - lastTime);
            eventStream.add(event);
            lastTime = thisTime;
        }
    }
    
    /**
     * Add a top-level sprite to manage
     */
    public void addSprite(Sprite s) {
        sprites.add(s);
    }
    
    /**
     * Paint our canvas
     */
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        for (Sprite sprite : sprites) {
            sprite.draw((Graphics2D)g);
        }
        paintChildren(g);
    }
    
    /**
     * Starts recording of events.
     */
    public void startRecording() {
        interactionMode = InteractionMode.RECORDING;
        eventStream = new Vector<Serializable>();
        lastTime = System.currentTimeMillis();
    }
    
    /**
     * Stops recording events, writes them to a file called "demo"
     */
    public void stopRecording() {
        interactionMode = InteractionMode.IDLE;
        if (eventStream != null) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("demo"));
                for (Serializable item : eventStream) {
                    out.writeObject(item);
                }
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Starts the demo. Important note: This simply plays back events
     * that were previously recorded. Thus, it is assumed that the sprites
     * are in the *exact same state* as when recording started. If not, you
     * will get unexpected results. This demo code DOES NOT try to reset
     * the sprites to the initial state they were in when recording started.
     * You may want to add this feature.
     * 
     * A message box will be shown if the demo file cannot be found.
     */
    public void startDemo() {
        interactionMode = InteractionMode.PLAYBACK;
        final Vector<Serializable> playbackStream = new Vector<Serializable>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("demo"));
            while (true) {
                Serializable event = (Serializable)in.readObject();
                playbackStream.add(event);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Demo file not found.");
        } catch (EOFException e) {
            ; // ignore: This is how we know we're done reading the file
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // Create and start a thread to play back the events
        Thread playbackThread = new Thread(new Runnable() {
            public void run() {
                boolean hadFirstWait = false;
                Iterator<Serializable> iter = playbackStream.iterator();
                while (iter.hasNext() && interactionMode.equals(InteractionMode.PLAYBACK)) {
                    Long waitTime = (Long)iter.next();
                    MouseEvent event = (MouseEvent)iter.next();
                    try {
                        if (hadFirstWait) {
                            Thread.sleep(waitTime);
                        } else {
                            hadFirstWait = true;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switch (event.getID()) {
                        case MouseEvent.MOUSE_PRESSED:
                            handleMousePress(event);
                            break;
                        case MouseEvent.MOUSE_DRAGGED:
                            handleMouseDragged(event);
                            break;
                        case MouseEvent.MOUSE_RELEASED:
                            handleMouseReleased(event);
                            break;
                    }
                }
            }
        });
        playbackThread.start();
    }
    
    /**
     * Stops the demo.
     */
    public void stopDemo() {
        interactionMode = InteractionMode.IDLE;
    }

    private static Sprite buildPaperDoll() {
      Sprite body = new Body();
      body.translate(350,150);
      
      Sprite head = new Head();
      head.translate(80, 0);
      Sprite lleg = new LLeg();
      lleg.translate(40,195);
      Sprite rleg = new RLeg();
      rleg.translate(115,195);
      Sprite larm = new LArm();
      larm.translate(10, 40);
      Sprite rarm = new RArm();
      rarm.translate(150, 40);
      body.addChild(head);
      body.addChild(lleg);
      body.addChild(rleg);
      body.addChild(larm);
      body.addChild(rarm);
      
      Sprite llowleg = new LLowLeg();
      llowleg.translate(0,95);
      Sprite rlowleg = new RLowLeg();
      rlowleg.translate(0,95);
      Sprite llowarm = new LLowArm();
      llowarm.translate(-10,45);
      Sprite rlowarm = new RLowArm();
      rlowarm.translate(10,45);
      lleg.addChild(llowleg);
      rleg.addChild(rlowleg);
      larm.addChild(llowarm);
      rarm.addChild(rlowarm);
      
      Sprite lfoot = new LFoot();
      lfoot.translate(-5,85);
      Sprite rfoot = new RFoot();
      rfoot.translate(5,85);
      Sprite lhand = new LHand();
      lhand.translate(-5,75);
      Sprite rhand = new RHand();
      rhand.translate(5,75);
      llowleg.addChild(lfoot);
      rlowleg.addChild(rfoot);
      llowarm.addChild(lhand);
      rlowarm.addChild(rhand);
      
      // Link together the legs for scaling
      lleg.addScaleBuddy(rleg);
      lleg.addScaleBuddy(llowleg);
      lleg.addScaleBuddy(rlowleg);
      rleg.addScaleBuddy(lleg);
      rleg.addScaleBuddy(llowleg);
      rleg.addScaleBuddy(rlowleg);
      llowleg.addScaleBuddy(lleg);
      llowleg.addScaleBuddy(rleg);
      llowleg.addScaleBuddy(rlowleg);
      rlowleg.addScaleBuddy(lleg);
      rlowleg.addScaleBuddy(rleg);
      rlowleg.addScaleBuddy(llowleg);
      
      return body;
    }
    
    private JMenuBar buildMenuBar() {
      JMenuBar bar = new JMenuBar();
      JMenu menu;
      JMenuItem item;
      
      menu = new JMenu("File");
      item = new JMenuItem("Reset");
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
      item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          sprites = new Vector<Sprite>();
          addSprite(buildPaperDoll());
          repaint();
        }
      });
      menu.add(item);
      menu.addSeparator();
      
      item = new JMenuItem("Quit");
      item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          System.exit(0);
        }
      });
      menu.add(item);
      
      bar.add(menu);
      
      return bar;
    }
    
    /**
     * Test code.
     */
    public static void main(String[] args) {
        Sprite body = buildPaperDoll();
      
        SpriteCanvas canvas = new SpriteCanvas();
        canvas.addSprite(body);
        
        JFrame f = new JFrame();
        f.setJMenuBar(canvas.buildMenuBar());
        f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
        canvas.add(new JLabel("Click to move and rotate.  Use the scroll wheel to scale the legs."));
        f.getContentPane().add(canvas);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 650);
        f.setVisible(true);
    }

}
