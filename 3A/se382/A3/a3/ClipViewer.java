package a3;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingworker.SwingWorker;

import a3.util.MarkerPreviewRenderer;


@SuppressWarnings("serial")
public class ClipViewer extends JInternalFrame implements myFrame{
  public MarkerSet mset;
  public MarkerView view;
  public TriSlider slider;
  public Animator animator;
  public JButton playPauseButton;
  public JScrollPane previewer;
  public JPanel previewPanel;
  public PropertyChangeListener l = new PropertyChangeListener() {
    public void propertyChange(PropertyChangeEvent evt) {
      slider.setCurr((Integer)evt.getNewValue());
    }
  };

  
  public ClipViewer() {
    super("Clip Viewer");
    mset = new MarkerSet();

    final JPanel viewer = new JPanel();
    add(viewer);
    viewer.setLayout(new BoxLayout(viewer, BoxLayout.Y_AXIS));
    
    view = new MarkerView(mset, Application.getRenderer());
    view.setPreferredSize(new Dimension(200,150));
    view.setMaximumSize(new Dimension(200,150));
    viewer.add(view);
    
    // Controls
    JPanel controls = new JPanel();
    controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
    
    //  Play Pause Button
    playPauseButton = new PlayPauseButton(this);
    playPauseButton.setPreferredSize(new Dimension(80,40));
    
    // Slider
    this.slider = new TriSlider(this, 0, mset.getNumFrames()-1, mset.getCurFrame());
    mset.addPropertyChangeListener("CUR_FRAME_PROPERTY", l);
    slider.addCurrentFrameListener(new CurrentFrameListener() {
      public void update() {mset.setCurFrame(slider.curr);}
    });
    slider.setPreferredSize(new Dimension(0, 0));
    slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    controls.add(Box.createRigidArea(new Dimension(5,0)));
    controls.add(playPauseButton);
    controls.add(Box.createRigidArea(new Dimension(5,0)));
    controls.add(slider);
    controls.add(Box.createRigidArea(new Dimension(5,0)));
    
    viewer.add(controls);
    
    view.setVisible(false);
    slider.setVisible(false);
    playPauseButton.setVisible(false);
    
    JButton dirButton = new JButton("Open Directory");
    dirButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    viewer.add(dirButton);
    dirButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser jc = new JFileChooser();
        jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = jc.showOpenDialog(ClipViewer.this);
        if (ret == JFileChooser.APPROVE_OPTION) {
          File file = jc.getSelectedFile();
          File[] list = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
              return name.endsWith("uwm") || name.endsWith("c3d");
            }
          });
          preview(list);
        }
      }
    });

    previewPanel = new JPanel();
    previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
    previewer = new JScrollPane(previewPanel);
    viewer.add(previewer);
    
    this.animator = new Animator(this);
  }
  
  public void preview(File[] files) {
    FileLoader fl = new FileLoader(files);
    fl.execute();
  }

  private class FMSet {
    public File file;
    public MarkerSet set;
    public FMSet(MarkerSet set, File file) {
      this.set = set;
      this.file = file;
    }
  }
  
  private class FileLoader extends SwingWorker<Void, FMSet>  {
    File[] files;
    public FileLoader(File[] files) {
      this.files = files;
    }
    @Override
    protected Void doInBackground() throws Exception {
      for (int i = 0; i < files.length; i++) {
        MarkerSet set = null;
        if (files[i].getName().endsWith("uwm")) {
          try {
            set = MarkerSet.readMarkerSetFromUWMFile(files[i]);
          } catch (IOException e) {}
          catch (ClassNotFoundException e) {}
        } else {
          try {
            set = MarkerSet.readMarkerSetFromC3D(files[i]);
          } catch (IOException e) {}
        }
        publish(new FMSet(set, files[i]));
      }
      return null;
    }

    @Override
    protected void done() {
    }

    @Override
    protected void process(List<FMSet> chunks) {
      // for each markerset, make button add button, call revalidate
      for (int i = 0; i < chunks.size(); i++) {
        MarkerSet set = chunks.get(i).set;
        Image img = MarkerPreviewRenderer.getPreview(set, Application.getRenderer(), new Dimension(100,100));
        JButton button = new PreviewButton(ClipViewer.this, new ImageIcon(img), chunks.get(i).file);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        previewPanel.add(button);
        previewer.revalidate();
      }
    }
  }
  
  public void showClip(File file) {
    MarkerSet set = null;
    if (file.getName().endsWith("uwm")) {
      try {
        set = MarkerSet.readMarkerSetFromUWMFile(file);
      } catch (IOException e) {}
      catch (ClassNotFoundException e) {}
    } else {
      try {
        set = MarkerSet.readMarkerSetFromC3D(file);
      } catch (IOException e) {}
    }
    mset.removePropertyChangeListener("CUR_FRAME_PROPERTY", l);
    mset = set;
    mset.addPropertyChangeListener("CUR_FRAME_PROPERTY", l);
    view.setMarkers(set);
    slider.newMset(set.getNumFrames());
    showView();
  }
  
  public void showView() {
    view.setVisible(true);
    slider.setVisible(true);
    playPauseButton.setVisible(true);
  }
  
  public void nextFrame() {
    int newFrame = mset.getCurFrame() + 1;
    if (newFrame >= slider.right) newFrame = slider.left;
    mset.setCurFrame(newFrame);
  }
  
  public Animator getAnimator() {
    return this.animator;
  }
  
  public void copy() {
    Clipboard.getInstance().setItem(mset.copyRange(slider.left, slider.right));
  }

  public void selectAll() {
    slider.setLeft(slider.min);
    slider.setRight(slider.max);
    slider.setCurr(slider.left);
  }
}