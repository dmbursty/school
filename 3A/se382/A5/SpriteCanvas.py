import java.awt.Color as Color
import java.awt.Graphics as Graphics
import java.awt.Graphics2D as Graphics2D
import java.awt.Rectangle as Rectangle
import java.awt.event.ActionEvent as ActionEvent
import java.awt.event.ActionListener as ActionListener
import java.awt.event.KeyEvent as KeyEvent
import java.awt.event.MouseAdapter as MouseAdapter
import java.awt.event.MouseEvent as MouseEvent
import java.awt.event.MouseMotionAdapter as MouseMotionAdapter
import java.awt.event.MouseWheelEvent as MouseWheelEvent
import java.awt.event.MouseWheelListener as MouseWheelListener
import java.io.EOFException as EOFException
import java.io.FileInputStream as FileInputStream
import java.io.FileNotFoundException as FileNotFoundException
import java.io.FileOutputStream as FileOutputStream
import java.io.IOException as IOException
import java.io.ObjectInputStream as ObjectInputStream
import java.io.ObjectOutputStream as ObjectOutputStream
import java.io.Serializable as Serializable
import java.util.Iterator as Iterator
import java.util.Vector as Vector

import javax.swing.BoxLayout as BoxLayout
import javax.swing.ImageIcon as ImageIcon
import javax.swing.JFrame as JFrame
import javax.swing.JLabel as JLabel
import javax.swing.JMenu as JMenu
import javax.swing.JMenuBar as JMenuBar
import javax.swing.JMenuItem as JMenuItem
import javax.swing.JOptionPane as JOptionPane
import javax.swing.JPanel as JPanel
import javax.swing.KeyStroke as KeyStroke

import java.lang.Math as Math
import java.lang.System as System
from Sprite import Sprite
from Sprite import DRAGGING
from Sprite import ROTATING

class SpriteCanvas(JPanel):
  def __init__(self):
    self.interactiveSprite = None
    self.sprites = []
    self.initialize()

  def addSprite(self, s):
    self.sprites.append(s)

  def clearSprites(self):
    self.sprites = []

  #
  # Inner Classes
  #
  class scMouseAdapter(MouseAdapter):
    def __init__(self, canvas):
      self.canvas = canvas
    def mousePressed(self, e):
      canvas.handleMousePress(e)
    def mouseReleased(self, e):
      canvas.handleMouseReleased(e)

  class scMouseMotionAdapter(MouseMotionAdapter):
    def __init__(self, canvas):
      self.canvas = canvas
    def mouseDragged(self, e):
      canvas.handleMouseDragged(e)

  class scMouseWheelListener(MouseWheelListener):
    def __init__(self, canvas):
      self.canvas = canvas
    def mouseWheelMoved(self, e):
      canvas.handleMouseWheelMoved(e)

  class scResetListener(ActionListener):
    def __init__(self, canvas):
      self.canvas = canvas
    def actionPerformed(self, e):
      canvas.clearSprites()
      canvas.addSprite(buildPaperDoll())
      canvas.repaint()

  class scQuitListener(ActionListener):
    def __init__(self, canvas):
      self.canvas = canvas
    def actionPerformed(self, e):
      System.exit(0)

  # Initialize the canvas
  def initialize(self):
    self.addMouseListener(self.scMouseAdapter(self))
    self.addMouseMotionListener(self.scMouseMotionAdapter(self))
    self.addMouseWheelListener(self.scMouseWheelListener(self))

  # Handler Methods
  def handleMousePress(self, e):
    for sprite in self.sprites:
      self.interactiveSprite = sprite.getSpriteHit(e)
      if self.interactiveSprite is not None:
        self.interactiveSprite.handleMouseDownEvent(e)
        break
    self.repaint()

  def handleMouseReleased(self, e):
    for sprite in self.sprites:
      self.interactiveSprite = sprite.getSpriteHit(e)
      if self.interactiveSprite is not None:
        self.interactiveSprite.handleMouseUp(e)
        break
    self.repaint()

  def handleMouseDragged(self, e):
    if self.interactiveSprite is not None:
      self.interactiveSprite.handleMouseDragEvent(e)
      self.repaint()

  def handleMouseWheelMoved(self, e):
    for sprite in self.sprites:
      self.interactiveSprite = sprite.getSpriteHit(e)
      if self.interactiveSprite is not None:
        self.interactiveSprite.handleMouseWheelMovedEvent(e)
        break
    self.repaint()


  # Drawing
  def paint(self, g):
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, self.getWidth(), self.getHeight())
    g.setColor(Color.BLACK)
    for sprite in self.sprites:
      sprite.draw(g)
    self.paintChildren(g)

  def buildMenuBar(self):
    bar = JMenuBar()
    menu = JMenu()
    item = JMenuItem()

    menu = JMenu("File")
    item = JMenuItem("Reset")
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK))
    item.addActionListener(self.scResetListener(self))
    menu.add(item)
    menu.addSeparator()

    item = JMenuItem("Quit")
    item.addActionListener(self.scQuitListener(self))
    menu.add(item)

    bar.add(menu)

    return bar

def buildPaperDoll():
  body = Sprite(Rectangle(0,0,160,220), 0,
                ImageIcon("body.png"), 350, 150,
                DRAGGING, False)

  head = Sprite(Rectangle(-55,-80,110,110), Math.PI * (5.0/18),
                ImageIcon("head.png"), 80, 0,
                ROTATING, False, body)
  lleg = Sprite(Rectangle(-20,-20,40,110), Math.PI * 0.5,
                ImageIcon("leg.png"), 40, 195,
                ROTATING, True, body)
  rleg = Sprite(Rectangle(-20,-20,40,110), Math.PI * 0.5,
                ImageIcon("leg.png"), 115, 195,
                ROTATING, True, body)
  larm = Sprite(Rectangle(-25,-30,40,90), Math.PI,
                ImageIcon("up_arm.png"), 10, 40,
                ROTATING, False, body)
  rarm = Sprite(Rectangle(-15,-30,40,90), Math.PI,
                ImageIcon("r_up_arm.png"), 150, 40,
                ROTATING, False, body)

  rlowleg = Sprite(Rectangle(-20,-20,40,110), Math.PI * 0.5,
                   ImageIcon("r_low_leg.png"), 0, 95,
                   ROTATING, True, rleg)
  llowleg = Sprite(Rectangle(-20,-20,40,110), Math.PI * 0.5,
                   ImageIcon("low_leg.png"), 0, 95,
                   ROTATING, True, lleg)
  llowarm = Sprite(Rectangle(-20,0,40,90), Math.PI * (135.0/180),
                   ImageIcon("low_arm.png"), -10, 45,
                   ROTATING, False, larm)
  rlowarm = Sprite(Rectangle(-20,0,40,90), Math.PI * (135.0/180),
                   ImageIcon("r_low_arm.png"), 10, 45,
                   ROTATING, False, rarm)

  lfoot = Sprite(Rectangle(-70,-10,90,65), Math.PI * (35.0/180),
                 ImageIcon("foot.png"), -5, 85,
                 ROTATING, False, llowleg)
  rfoot = Sprite(Rectangle(-20,-10,90,65), Math.PI * (35.0/180),
                 ImageIcon("r_foot.png"), 5, 85,
                 ROTATING, False, rlowleg)
  lhand = Sprite(Rectangle(-20,5,35,65), Math.PI * (35.0/180),
                 ImageIcon("hand.png"), -5, 75,
                 ROTATING, False, llowarm)
  rhand = Sprite(Rectangle(-15,5,35,65), Math.PI * (35.0/180),
                 ImageIcon("r_hand.png"), 5, 75,
                 ROTATING, False, rlowarm)

  lleg.addScaleBuddy(rleg)
  lleg.addScaleBuddy(llowleg)
  lleg.addScaleBuddy(rlowleg)
  rleg.addScaleBuddy(lleg)
  rleg.addScaleBuddy(llowleg)
  rleg.addScaleBuddy(rlowleg)
  llowleg.addScaleBuddy(lleg)
  llowleg.addScaleBuddy(rleg)
  llowleg.addScaleBuddy(rlowleg)
  rlowleg.addScaleBuddy(lleg)
  rlowleg.addScaleBuddy(rleg)
  rlowleg.addScaleBuddy(llowleg)

  return body


if __name__ == "__main__":
  f = JFrame()
  body = buildPaperDoll()
  canvas = SpriteCanvas()
  canvas.addSprite(body)

  f.setJMenuBar(canvas.buildMenuBar())
  f.getContentPane().setLayout(BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS))
  canvas.add(JLabel("Click and drag to move and rotate.  Use the scroll wheel to scale the legs."))
  f.getContentPane().add(canvas)
  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  f.setSize(900, 650)
  f.setVisible(True)

