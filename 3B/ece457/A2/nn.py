#!/usr/bin/python

import sys
import math
import random

def rand():
  return (random.random() - 0.5) * 2

def sigmoid(i):
  #return math.tanh(i)
  try:
    return 1.0 / (1.0 + math.exp(-i))
  except:
    return 0.0

def sigmoid_prime(i):
  #return 1.0 - i**2
  return sigmoid(i) * (1 - sigmoid(i))

class NeuralNet:
  def __init__(self, n_in, n_h, n_out):
    self.n_in = n_in + 1
    self.n_h = n_h
    self.n_out = n_out

    # Values for nodes
    self.val_in = [0.0] * self.n_in
    self.val_h = [0.0] * self.n_h
    self.val_out = [0.0] * self.n_out

    # Weights
    self.in_weights = {}
    self.out_weights = {}

    for k in range(self.n_in):
      for j in range(self.n_h):
        self.in_weights[(k, j)] = rand()

    for j in range(self.n_h):
      for i in range(self.n_out):
        self.out_weights[(j, i)] = rand()

  def feed_forward(self, input):
    # Load input
    for k in range(self.n_in - 1):
      self.val_in[k] = input[k]

    # Set threshold neuron
    self.val_in[-1] = 1

    # Push to hidden nodes
    for j in range(self.n_h):
      sum = 0.0
      for k in range(self.n_in):
        sum += self.val_in[k] * self.in_weights[(k, j)]
      self.val_h[j] = sigmoid(sum)

    # Push to output nodes
    for i in range(self.n_out):
      sum = 0.0
      for j in range(self.n_h):
        sum += self.val_h[j] * self.out_weights[(j, i)]
      self.val_out[i] = sigmoid(sum)

    return self.val_out

  def back_propagate(self, target, learning):
    # Calculate error
    error = [a - b for a, b in zip(target, self.val_out)]

    # Calculate delta i
    delta_i = []
    for i in range(self.n_out):
      delta_i.append(error[i] * sigmoid_prime(self.val_out[i]))

    # Calculate delta j
    delta_j = []
    for j in range(self.n_h):
      factor = 0.0
      for i in range(self.n_out):
        factor += self.out_weights[(j,i)] * delta_i[i]
      delta_j.append(factor * sigmoid_prime(self.val_h[j]))

    # Update out_weights
    for j in range(self.n_h):
      for i in range(self.n_out):
        self.out_weights[(j, i)] += learning * self.val_h[j] * delta_i[i]

    # Update in_weights
    for k in range(self.n_in):
      for j in range(self.n_h):
        self.in_weights[(k, j)] += learning * self.val_in[k] * delta_j[j]

    return 0.5 * sum([e * e for e in error])

  def train(self, inputs, targets, iterations=1000, learning=0.5, threshold=0.5):
    for i in xrange(iterations):
      error = 0
      for input, target in zip(inputs, targets):
        self.feed_forward(input)
        error += self.back_propagate(target, learning)
      if i % 20 == 0:
        sys.stderr.write(str(error) + "\n")
        print error
      if error < threshold:
        return
    print "Done Iterations"

  def print_weights(self):
    print "Input weights:"
    print self.in_weights
    print "Output weights:"
    print self.out_weights

if __name__ == "__main__":
  # Read in training data
  training_data = { 5:[], 6:[], 7:[], 8:[], 9:[] }
  train_file = open("train_data.txt")
  for line in train_file:
    points = [float(p) for p in line.split(",")]
    training_data[points[-1]].append(points[:-1])

  # Make neural nets
  input = 144
  hidden = 50
  print "%d hidden nodes" % hidden
  output = 5
  net = NeuralNet(input, hidden, output)

  inputs = []
  targets = []
  for i in range(5, 10):
    for input in training_data[i]:
      inputs.append(input)
      target = []
      if i == 5:
        target = [1.0, 0.0, 0.0, 0.0, 0.0]
      elif i == 6:
        target = [0.0, 1.0, 0.0, 0.0, 0.0]
      elif i == 7:
        target = [0.0, 0.0, 1.0, 0.0, 0.0]
      elif i == 8:
        target = [0.0, 0.0, 0.0, 1.0, 0.0]
      elif i == 9:
        target = [0.0, 0.0, 0.0, 0.0, 1.0]
      targets.append(target)

  net.train(inputs, targets, 1000, 0.5)

  # Try the testing data
  results = {5:[0, 0], 6:[0, 0], 7:[0, 0], 8:[0, 0], 9:[0, 0], 0:[0, 0]}
  for i in range(5, 10):
    for input in training_data[i]:
      r = net.feed_forward(input)
      results[i][0] += 1
      results[0][0] += 1
      if i == r.index(max(r))+5:
        results[i][1] += 1
        results[0][1] += 1

  print "Training Data: %d out of %d" % (results[0][1], results[0][0])
  for k, v in results.iteritems():
    print "  %d: %d out of %d" % (k, v[1], v[0])

  results = {5:[0, 0], 6:[0, 0], 7:[0, 0], 8:[0, 0], 9:[0, 0], 0:[0, 0]}
  # Try the test data
  test_file = open("test_data.txt")
  for line in test_file:
    points = [float(p) for p in line.split(",")]
    digit =  points[-1]
    r = net.feed_forward(points[:-1])
    results[digit][0] += 1
    results[0][0] += 1
    if digit == r.index(max(r))+5:
      results[digit][1] += 1
      results[0][1] += 1

  print "Testing Data: %d out of %d" % (results[0][1], results[0][0])
  for k, v in results.iteritems():
    print "  %d: %d out of %d" % (k, v[1], v[0])

  results = {5:[0, 0], 6:[0, 0], 7:[0, 0], 8:[0, 0], 9:[0, 0], 0:[0, 0]}
  # Try the real test data
  test_file = open("real_test_data.txt")
  for line in test_file:
    points = [float(p) for p in line.split(",")]
    digit =  points[-1]
    r = net.feed_forward(points[:-1])
    results[digit][0] += 1
    results[0][0] += 1
    if digit == r.index(max(r))+5:
      results[digit][1] += 1
      results[0][1] += 1

  print "Real Testing Data: %d out of %d" % (results[0][1], results[0][0])
  for k, v in results.iteritems():
    print "  %d: %d out of %d" % (k, v[1], v[0])
