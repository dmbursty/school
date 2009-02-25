#!usr/bin/python

import sys

A = [[1,0,0,0,1],
     [0,1,0,1,1],
     [0,0,1,1,0],
     [0,1,0,1,0],
     [0,0,1,0,1]]

b = [1,1,0,1,1]

def encrypt(m, A, b):
  c = [0,0,0,0,0]
  for i in range(len(A)):
    for j in range(len(m)):
      c[i] += int(m[j]) * A[j][i]
    c[i] %= 2

  for i in range(len(c)):
    c[i] = (c[i] + b[i]) % 2

  return c

bin = [0,1]

for v in bin:
  for w in bin:
    for x in bin:
      for y in bin:
        for z in bin:
          print "%s -> %s" % (str([v,w,x,y,z]), str(encrypt([v,w,x,y,z],A,b)))
