from random import randint
import sys

n = int(sys.argv[2]) - 1
for i in range(int(sys.argv[1])):
  print "%d %d %d %d" % (randint(0,n), randint(0,n), randint(0,n), randint(0,n))
