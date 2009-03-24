package a3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class PreviewButton extends JButton {
  final ClipViewer c;
  final File file;
  public PreviewButton(ClipViewer c, Icon i, File infile) {
    super(i);
    this.c = c;
    this.file = infile;
    this.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PreviewButton.this.c.showClip(file);
      }
    });
  }
}
