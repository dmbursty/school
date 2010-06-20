def name():
  return "name"

def type():
  return "int"

def length():
  return 4

def stype():
  return "native"

file = open("doc-info.xml", 'w')
file.write("1.7\n")
file.write("10\n")
file.write("10\n")
file.write("0\n")
file.write("1\n")
file.write("500\n")
for i in range(500):
  file.write("%s %s %s %s\n" % (name(), type(), length(), stype()))
file.write("500\n")
for i in range(500):
  file.write("%s %s\n" % (name(), stype()))
