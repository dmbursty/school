m = 9
g = 256
p = 569
#p = 113
#g = 3
#m = 11

map = {}
expos = []
for j in range(m):
  e = int(g**j % p)
  map[e] = j
  expos.append(e)

expos.sort()
for i in expos:
  print "(%i, %i)" % (map[i], i)

h = 327
#h = 57
gi = int(g**(p-2) % p)
gm = int(gi**m % p)

for i in range(m):
  e = int((h* gm**i) % p)
  if e in expos:
    print "a = m%i + %i" % (i, map[e])
    print m*i + map[e]
