for x in range(11):
  for y in range(11):
    if (y*y)%11 == (x*x*x + 2*x + 5)%11:
      print "(%i,%i)" % (x,y)
