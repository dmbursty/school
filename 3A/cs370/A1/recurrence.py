#!/usr/bin/python
import sys

glo_p = {}
glo_q = {}

def p(n):
  try:
    return glo_p[n]
  except:
    pass
  if n == 0:
    return 1.0
  elif n == 1:
    return 0.2
  elif n < 0:
    raise "Exception"
  else:
    glo_p[n] = p(n-2)*0.24 - p(n-1)
    return glo_p[n]

def q(n):
  try:
    return glo_q[n]
  except:
    pass
  if n == 0:
    return 1.0
  elif n == 1:
    return 0.2
  elif n < 0:
    raise "Exception"
  else:
    glo_q[n] = q(n-1) - q(n-2)*0.16
    return glo_q[n]

def exp(i):
  return 1.0 / 5**i

def err(p, q, i):
  c = exp(i)
  print "n = %d" % i
  print "p's error: %s" % (p - c)
  print "q's error: %s" % (q - c)

for i in range(100,101):
  err(p(i), q(i), i)
