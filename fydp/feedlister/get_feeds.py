#!/usr/bin/python

import urllib
import re
import socket
import sys

if __name__ == "__main__":
  socket.setdefaulttimeout(5)
  pattern = re.compile(
    "<link rel=\"alternate\" type=\"application/rss\+xml\" title=\"(.*?)\" href=\"(.*?)\"")

  blogs = open("blogs")
  count = 0
  matches = 0
  for line in blogs:
    count += 1
    sys.stderr.write(str(count) + "\r")
    try:
      blog = urllib.urlopen(line)
    except Exception, e:
      #print "Exception %s" % e
      continue
    try:
      match = re.search(pattern, blog.read())
    except:
      continue
    if match is not None:
      matches += 1
      print match.group(2)
