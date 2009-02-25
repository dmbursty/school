import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Code derived from Java Swing Tutorial
public class Application {
  private static void makeGUI() {
    final IteratedSystemManager ism = IteratedSystemManager.getInstance();
    GridBagConstraints c = new GridBagConstraints();
    
    // Frame
    final JFrame frame = new JFrame("L-System Viewer");
    frame.setPreferredSize(new Dimension(800,600));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Menu Bar
    JMenuBar menubar = new JMenuBar();
    JMenu menu;
    JMenuItem menuitem;
    
    menu = new JMenu("File");
    menubar.add(menu);
    menuitem = new JMenuItem("Quit");
    menuitem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    menu.add(menuitem);

    menu = new JMenu("L-Systems");
    menubar.add(menu);
    Set<String> names = ism.getLSystemNames();
    for (final String name : names) {
      menuitem = new JMenuItem(name);
      menuitem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          IteratedSystemManager ism = IteratedSystemManager.getInstance();
          ism.setLSystem(name);
        }
      });
      menu.add(menuitem);
    }
    
    menu = new JMenu("Help");
    menubar.add(menu);
    menuitem = new JMenuItem("About");
    menuitem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, "Daniel Burstyn's *NEW* L-System Viewer");
      }
    });
    menu.add(menuitem);
    
    // Main Panel
    JPanel root_panel = new JPanel();
    root_panel.setLayout(new BoxLayout(root_panel, BoxLayout.Y_AXIS));
    root_panel.setPreferredSize(new Dimension(800,600));

    // Components
    // IteratedSystemComponent
    JPanel systemPanel = new JPanel();
    systemPanel.setLayout(new BoxLayout(systemPanel, BoxLayout.Y_AXIS));
    root_panel.add(systemPanel);
    
    JPanel titlePanel = new JPanel(new GridBagLayout());
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    final JLabel title = new JLabel("Sierpinksi Triangle");
    IteratedSystemManager.getInstance().addSystemNameListener(new SystemNameListener() {
      public void update(String name) {
        title.setText(name);
      }
    });
    title.setFont(title.getFont().deriveFont(Font.BOLD));
    titlePanel.setBorder(new LineBorder(Color.BLACK));
    titlePanel.add(title, c);
    systemPanel.add(titlePanel);
    
    IteratedSystemComponent systemcomponent = new IteratedSystemComponent();
    systemcomponent.setBorder(new LineBorder(Color.BLACK));
    JScrollPane scroller = new JScrollPane(systemcomponent);
    scroller.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
    systemcomponent.setScroller(scroller);
    systemPanel.add(scroller);
    
    
    // Controls
    JPanel controlPanel = new JPanel();
    controlPanel.setBorder(new EmptyBorder(5,0,0,0));
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    root_panel.add(controlPanel);

    // Sliders
    JPanel sliderPanel = new JPanel();
    sliderPanel.setLayout(new GridBagLayout());
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(2,5,2,5);
    controlPanel.add(sliderPanel);

    c.gridx = 0;
    c.gridy = 0;
    sliderPanel.add(new JLabel("Current Iteration:", JLabel.RIGHT), c);
    final SystemSlider sSlider = new SystemSlider(JSlider.HORIZONTAL, 0, ism.getMaxNumIterations(), 0);
    sSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        ism.iterateToStep(sSlider.getValue());
      }
    });
    ism.addSystemListener(sSlider);
    c.gridx = 1;
    c.gridy = 0;
    sSlider.setMinimumSize(new Dimension(200, 25));
    sSlider.setPreferredSize(new Dimension(200, 25));
    sliderPanel.add(sSlider, c);

    c.gridx = 0;
    c.gridy = 1;
    sliderPanel.add(new JLabel("Animation Speed:", JLabel.RIGHT), c);
    final JSlider aSlider = new JSlider(JSlider.HORIZONTAL, 100, 1000, 500);
    aSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        ism.setTimerSpeed(aSlider.getValue());
      }
    });
    aSlider.setMajorTickSpacing(100);
    aSlider.setMinorTickSpacing(50);
    aSlider.setPaintTicks(true);
    c.gridx = 1;
    c.gridy = 1;
    aSlider.setMinimumSize(new Dimension(200, 25));
    aSlider.setPreferredSize(new Dimension(200, 25));
    sliderPanel.add(aSlider, c);
    
    c.gridx = 0;
    c.gridy = 2;
    sliderPanel.add(new JLabel("Zoom Level:", JLabel.RIGHT), c);
    final ZBox zoomBox = new ZBox();
    zoomBox.setEditable(true);
    zoomBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!zoomBox.isUpdating()) {
          ism.setZoom((Double)(zoomBox.getSelectedItem()));
        }
      }
    });
    ism.addZoomListener(zoomBox);
    c.gridx = 1;
    c.gridy = 2;
    zoomBox.setMinimumSize(new Dimension(200,18));
    zoomBox.setPreferredSize(new Dimension(200,18));
    sliderPanel.add(zoomBox, c);
    
    // Add some spacing between sliders and buttons
    controlPanel.add(Box.createHorizontalGlue());
    
    // Buttons
    JPanel buttonPanel = new JPanel();
    GridLayout layout = new GridLayout(2,2);
    layout.setHgap(5);
    layout.setVgap(5);
    buttonPanel.setLayout(layout);
    controlPanel.add(buttonPanel);
    
    JStepButton stepButton = new JStepButton("Step");
    JBackButton backButton = new JBackButton("Back");
    JButton resetButton = new JButton("Reset");
    final JPPButton playPauseButton = new JPPButton("Pause");
    stepButton.setMinimumSize(new Dimension(100, 25));
    backButton.setMinimumSize(new Dimension(100, 25));
    resetButton.setMinimumSize(new Dimension(100, 25));
    playPauseButton.setMinimumSize(new Dimension(100, 25));
    stepButton.setPreferredSize(new Dimension(150, 25));
    backButton.setPreferredSize(new Dimension(150, 25));
    resetButton.setPreferredSize(new Dimension(150, 25));
    playPauseButton.setPreferredSize(new Dimension(150, 25));
    ism.addTimerEventListener(playPauseButton);
    ism.addSystemListener(stepButton);
    ism.addSystemListener(backButton);
    
    // Button's Listeners
    stepButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        IteratedSystemManager ism = IteratedSystemManager.getInstance();
        ism.iterate();
        ism.pause();
      }
    });
    backButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        IteratedSystemManager ism = IteratedSystemManager.getInstance();
        ism.back();
        ism.pause();
      }
    });
    resetButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        IteratedSystemManager.getInstance().reset();
      }
    });
    playPauseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        // Ask the timer what our current state is
        IteratedSystemManager ism = IteratedSystemManager.getInstance();
        if (ism.isPlaying()) {
          ism.pause();
        } else {
          ism.play();
        }
      }
    });

    buttonPanel.add(backButton);
    buttonPanel.add(stepButton);
    buttonPanel.add(resetButton);
    buttonPanel.add(playPauseButton);

    frame.setJMenuBar(menubar);
    frame.getContentPane().add(root_panel);

    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        makeGUI();
      }
    });
  }
}
