#!/usr/bin/python
#
# Fuzzy logic inverted pendulum controller
# ECE457 - Project 3
# November 22, 2009
# Authors: Daniel Burstyn (dmbursty)
#          Alex Amato (ajamato)

from math import radians, degrees, cos, sin
from sys import argv

# Constants
g = 9.8
m = 2.0
M = 8.0
L = 1.0
step = 0.1


def fuzzify(low, high, value):
    # Get the membership degree for a value with the function
    # defined as the triangle of height 1 with base from low to high
    assert (low < high)
    centroid = (high+low)/(2.0);
    slopeLeft = 1.0/(centroid - low)
    bLeft = -slopeLeft*low
    slopeRight = -slopeLeft
    bRight = -slopeRight*high

    if (value>=low and value <=centroid):
        return slopeLeft*value + bLeft
    elif (value>=centroid and value <=high):
        return slopeRight*value + bRight
    else:
        return 0.0

def defuzzify(tupples):
    # a list of (low, high, value) tupples
    areas = []
    centroids = []
    totalAreas = 0.0;
    for tupple in tupples:
        low, high, value = tupple
        centroid = (low + high)/2.0
        # area = base * height * degree membership
        area = value * 1.0 * (high-low)/2.0;
        areas.append(area)
        centroids.append(centroid)
        totalAreas += area
    weightedSumCentroids = 0.0
    for i in range(0, len(centroids)):
        weightedSumCentroids+=centroids[i]*areas[i]
    return 0.0 if totalAreas == 0.0 else weightedSumCentroids/totalAreas



class FuzzyController:
  def __init__(self, file, a, v, tmax = 3):
    # Initial values
    self.a = radians(a)     # Angle
    self.v = radians(v)     # Angular Velocity
    self.u = 0              # Initial force
    self.time = 0           # Current time
    self.tmax = tmax
    # Parse specification file
    self.parse(file)

  def parse(self, file):
    f = open(file)
    # Name lookups
    a_l = {}
    v_l = {}
    u_l = {}
    # Set lists
    a_s = []
    v_s = []
    u_s = []
    # Matrix
    m = []

    # Read Angle Input Fuzzy Sets
    num = int(f.readline())
    for i in range(num):
      name, low, high = f.readline().split()
      a_l[name] = i
      a_s.append((radians(int(low)), radians(int(high))))
    # Read Angular Velocity Input Fuzzy Sets
    num = int(f.readline())
    for i in range(num):
      name, low, high = f.readline().split()
      v_l[name] = i
      v_s.append((radians(int(low)), radians(int(high))))
    # Read Output Fuzzy Sets
    num = int(f.readline())
    for i in range(num):
      name, low, high = f.readline().split()
      u_l[name] = i
      u_s.append((int(low), int(high)))
    # Read Decision Matrix
    for a in range(len(a_s)):
      m.append([u_l[n] for n in f.readline().split()])

    # Save variables to instance
    self.a_sets = a_s
    self.v_sets = v_s
    self.u_sets = u_s
    self.matrix = m

  # Do one time step
  def doStep(self):
    self.time += step

    # Calculate amount of force needed
    self.u = self.controlOutput()

    # Calculate new values
    f1 = 2.0 * g * sin(self.a)
    f2 = m * L * self.v * self.v * sin(2.0 * self.a) / (2.0 * (m + M))
    f3 = 2.0 * cos(self.a) * self.u / (M + m)
    f4 = 4.0 * L / 3.0
    f5 = m * L * cos(self.a) * cos(self.a) / (M + m)

    da = self.v
    dv = (f1 - f2 - f3) / (f4 - f5)

    self.a += da * step
    self.v += dv * step

  # Run the fuzzy logic controller
  def controlOutput(self):
    # Calculates angle input memberships
    angleMem = []
    for set in self.a_sets:
      angleMem.append(fuzzify(set[0], set[1], self.a))
    # Calculates angular velocity input memberships
    velocityMem = []
    for set in self.v_sets:
      velocityMem.append(fuzzify(set[0], set[1], self.v))

    # Calculate output memberships
    outputMemberships = self.evaluateMatrix(angleMem, velocityMem)
    tupples = []
    for i, set in enumerate(self.u_sets):
      tupples.append((set[0], set[1], outputMemberships[i]))
    # Get crisp output
    return defuzzify(tupples)

  # Given degrees of membership and decision matrix, return the output
  # degrees of membership.
  def evaluateMatrix(self, angleMem, velocityMem):
    #angleMem - list of degres of membership values 0..1, first index
    #velocity - list of degres of membership values 0..1, second index to matrix
    #matrix - a 2d list with the degrees of
    #returns a tupple - (index of FuzzyOutputSet, DegreeOfMembership)

    #initially all the values are 0
    #this is where we store the degree of membership of each fuzzy output set
    maxOutputDegreeMems = [0] * len(self.u_sets)

    for i in range(0, len(angleMem)):
      for j in range(0, len(velocityMem)):
        outputDegreeMem = min(angleMem[i], velocityMem[j])
        outputSetIndex = self.matrix[i][j]
        maxOutputDegreeMems[outputSetIndex] = \
                max(maxOutputDegreeMems[outputSetIndex], outputDegreeMem)

    return maxOutputDegreeMems

  # Run the whole controller
  def run(self):
    # Quit when the pendulum falls to far, or we reach max time
    while cos(self.a) > 0 and self.time < self.tmax:
      print "%s,%s,%s,%s" % (self.time, degrees(self.a),
                             degrees(self.v), self.u)
      self.doStep()
    if cos(self.a) <= 0:
      print "Fell over!"


if __name__ == "__main__":
  try:
    controller = FuzzyController(argv[1], int(argv[2]), int(argv[3]), float(argv[4]))
    controller.run()
  except Exception, e:
    print "Usage: python simulate.py <specification file> <initial angle> <initial angular velocity>"
