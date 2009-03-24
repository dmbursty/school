# Print out the distribution of reapeated triple letter blocks

f = open("cipher.txt")
cipher = f.readline()[:-1]

d = {}

for i in range(0, len(cipher)-2):
  try:
    s = cipher[i:i+3]
    d[s]
  except KeyError:
    d[s] = 1
  else:
    d[s] += 1
    print "%s came up %d" % (s, d[s])

print d
