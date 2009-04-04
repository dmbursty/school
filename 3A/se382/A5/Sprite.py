import java.awt.Rectangle as Rectangle
import java.awt.Graphics2D as Graphics2D
import java.awt.Point as Point
import java.awt.event.MouseEvent as MouseEvent
import java.awt.event.MouseWheelEvent as MouseWheelEvent
import java.awt.geom.AffineTransform as AffineTransform
import java.awt.geom.NoninvertibleTransformException as NoninvertibleTransformException
import java.awt.geom.Point2D as Point2D
import java.util.Vector as Vector

import javax.swing.ImageIcon as ImageIcon
import java.lang.Math as Math

# Global Constants
IDLE = 1
DRAGGING = 2
SCALING = 3
ROTATING = 4

MAX_SCALE = 2.5
MIN_SCALE = 0.4

class Sprite:
    def __init__(self, bounds, theta, img, transX, transY, mode, canScale, parent=None):
      self.myBounds = bounds
      self.minTheta = -1 * theta
      self.maxTheta = theta
      self.img = img
      self.mode = mode
      self.canScale = canScale

      self.children = []
      self.parent = parent
      self.scaleBuddies = []

      self.translate = Point2D.Double(transX,transY)
      self.rotation = 0
      self.scale = Point2D.Double(1,1)

      self.startPoint = None
      self.startRotation = None
      self.startTransform = None

      self.lastPoint           = None                      # Last mouse point
      self.interactionMode     = IDLE     # Current interaction mode

      if parent is not None:
        parent.addChild(self)

    def addChild(self, s):
        self.children.append(s)
        s.setParent(self)

    def getParent(self):
        return self.parent

    def setParent(self,s):
        self.parent = s

    def addScaleBuddy(self, s):
      self.scaleBuddies.append(s)

    def handleMouseWheelMovedEvent(self, e):
      if self.canScale:
        new_sy = self.scale.getY() + (-0.1 * e.getWheelRotation())
        if new_sy < MIN_SCALE:
          new_sy = MIN_SCALE
        if new_sy > MAX_SCALE:
          new_sy = MAX_SCALE
        self.scaleMe(self.scale.getX(), new_sy)
        for s in self.scaleBuddies:
          s.scaleMe(self.scale.getX(), new_sy)

    def handleMouseDownEvent(self, e):
        self.lastPoint = e.getPoint()
        self.startPoint = self.lastPoint
        self.startRotation = self.rotation
        try:
          if self.parent == None:
            self.startTransform = AffineTransform()
          else:
            self.startTransform = self.getNonScaleFullTransform().createInverse()
        except NoninvertibleTransformException, e1:
          pass
        if e.getButton() == MouseEvent.BUTTON1:
          self.interactionMode = DRAGGING
        elif e.getButton() == MouseEvent.BUTTON3:
          self.interactionMode = ROTATING
        if self.mode is not None:
          self.interactionMode = self.mode

    # Handle mouse drag event, with the assumption that we have already
    # been "selected" as the sprite to interact with.
    # This is a very simple method that only works because we
    # assume that the coordinate system has not been modified
    # by scales or rotations. You will need to modify self method
    # appropriately so it can handle arbitrary transformations.
    def handleMouseDragEvent(self, e):
        oldPoint = self.lastPoint
        newPoint = e.getPoint()
        self.lastPoint = e.getPoint()

        if self.interactionMode == IDLE:
          pass # Do nothing
        elif self.interactionMode == DRAGGING:
          oldPoint = self.toParent(oldPoint)
          newPoint = self.toParent(newPoint)
          x_diff = newPoint.getX() - oldPoint.getX()
          y_diff = newPoint.getY() - oldPoint.getY()

          if self.parent is not None:
            x_diff /= self.parent.scale.getX()
            y_diff /= self.parent.scale.getY()

          self.translateMe(x_diff, y_diff)
        elif self.interactionMode == ROTATING:
            oldPoint = self.toStart(self.startPoint)
            newPoint = self.toStart(newPoint)
            theta = self.startRotation + self.angleBetween(oldPoint, newPoint)
            while theta > Math.PI:
              theta -= 2*Math.PI
            while theta < -1*Math.PI:
              theta += 2*Math.PI
            if theta < self.minTheta:
              theta = self.minTheta
            if theta > self.maxTheta:
              theta = self.maxTheta
            self.rotateMe(theta)

    def handleMouseUp(self, e):
        self.interactionMode = IDLE

    # Locates the sprite that was hit by the given event.
    # You *may* need to modify self method, depending on
    # how you modify other parts of the class.
    #
    # @return The sprite that was hit, or None if on sprite was hit
    def getSpriteHit(self, e):
      for sprite in self.children:
        s = sprite.getSpriteHit(e)
        if s != None:
          return s
      if self.pointInside(e.getPoint()):
        return self
      return None

    # Important note: How transforms are handled here are only an example. You will
    # likely need to modify self code for it to work for your assignment.

    def toParent(self, p):
      ret = Point()
      if self.parent is None:
        t = AffineTransform()
      else:
        t = self.parent.getNonScaleFullTransform().createInverse()
      t.transform(p, ret)
      return ret

    def toLocal(self, p):
      ret = Point()
      if self.parent is None:
        t = AffineTransform()
      else:
        t = self.getFullTransform().createInverse()
      t.transform(p, ret)
      return ret

    def toStart(self, p):
      ret = Point()
      self.startTransform.transform(p, ret)
      return ret

    # Returns the full transform to self object from the root
    def getFullTransform(self):
      returnTransform = self.getLocalTransform()
      curSprite = self.parent
      while (curSprite != None):
        returnTransform.preConcatenate(curSprite.getNonScaleTransform())
        curSprite = curSprite.getParent()
      return returnTransform


    def getNonScaleFullTransform(self):
      returnTransform = AffineTransform()
      curSprite = self
      while (curSprite != None):
          returnTransform.preConcatenate(curSprite.getNonScaleTransform())
          curSprite = curSprite.getParent()
      return returnTransform

    # Returns our local transform
    def getLocalTransform(self):
      ret = AffineTransform()
      if self.parent is None:
        mod_x = 1
        mod_y = 1
      else:
        mod_x = self.parent.scale.getX()
        mod_y = self.parent.scale.getY()
      mod_x *= self.translate.getX()
      mod_y *= self.translate.getY()
      ret.concatenate(AffineTransform.getTranslateInstance(mod_x, mod_y))
      ret.concatenate(AffineTransform.getRotateInstance(self.rotation))
      ret.concatenate(AffineTransform.getScaleInstance(self.scale.getX(), self.scale.getY()))
      return ret

    def getNonScaleTransform(self):
      ret = AffineTransform()
      if self.parent is None:
        mod_x = 1
        mod_y = 1
      else:
        mod_x = self.parent.scale.getX()
        mod_y = self.parent.scale.getY()
      mod_x *= self.translate.getX()
      mod_y *= self.translate.getY()
      ret.concatenate(AffineTransform.getTranslateInstance(mod_x, mod_y))
      ret.concatenate(AffineTransform.getRotateInstance(self.rotation))
      return ret

    def rotateMe(self, theta):
      self.rotation = theta

    def translateMe(self, x_diff, y_diff):
      self.translate.setLocation(self.translate.getX() + x_diff, self.translate.getY() + y_diff)

    def scaleMe(self, sx, sy):
      self.scale.setLocation(sx, sy)

    # Draws the sprite. This method will call drawSprite after
    # the transform has been set up for self sprite.
    def draw(self, g):
        oldTransform = g.getTransform().clone()

        trans = g.getTransform().clone()
        trans.concatenate(self.getFullTransform())
        # Set to our transform
        g.setTransform(trans)
        # Draw the sprite (delegated to sub-classes)
        self.drawSprite(g)

        # Restore original transform
        g.setTransform(oldTransform)

        # Draw children
        for sprite in self.children:
          sprite.draw(g)


    def angleBetween(self, a, b):
      dotProduct = a.getX() * b.getX() + a.getY() * b.getY()
      a_normal = Math.sqrt(a.getX()*a.getX() + a.getY()*a.getY())
      b_normal = Math.sqrt(b.getX()*b.getX() + b.getY()*b.getY())
      if a_normal * b_normal == 0:
        return 0
      theta = dotProduct / (a_normal * b_normal)
      if theta > 1:
        theta = 1
      if theta < -1:
        theta = -1
      crossProduct = a.getX() * b.getY() - a.getY() * b.getX()
      theta = Math.acos(theta)
      if crossProduct < 0:
        theta = theta * -1
      while theta > Math.PI:
        theta -= 2*Math.PI
      while theta < -1*Math.PI:
        theta += 2*Math.PI
      # -pi <= return <= pi
      return theta

    # The method that actually does the sprite drawing. This method
    # is called after the transform has been set up in the draw() method.
    # Sub-classes should override self method to perform the drawing.
    def drawSprite(self, g):
      g.drawImage(self.img.getImage(),
                  int(self.myBounds.getX()),
                  int(self.myBounds.getY()),
                  int(self.myBounds.getWidth()),
                  int(self.myBounds.getHeight()), None);

    def pointInside(self, p):
      fullTransform = self.getFullTransform()
      inverseTransform = None
      try:
        inverseTransform = fullTransform.createInverse()
      except NoninvertibleTransformException, e:
        e.printStackTrace()
      newPoint = p.clone()
      inverseTransform.transform(newPoint, newPoint)
      return self.myBounds.contains(newPoint)
