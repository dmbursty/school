import urllib, re, htmlentitydefs

rePositive = re.compile('<tr><td><a href="features/(?P<file_name>[a-zA-Z0-9_\-]*).html">(?P<feature_name>[ \ta-zA-Z0-9&;/()\-]*)</a></td>[ \r\n\t]*<td align=center>[ \ta-zA-Z0-9&;]*</td><td align=center>[a-zA-Z0-9&;]*</td><td align=center><b>[ \t]*X[ \t]*</b></td><td align=center>[a-zA-Z0-9&;]*</td><td align=center>[a-zA-Z0-9&;]*</td>')
reNegative = re.compile('<tr><td><a href="features/(?P<file_name>[a-zA-Z0-9_\-]*).html">(?P<feature_name>[ \ta-zA-Z0-9&;/()\-]*)</a></td>[ \r\n\t]*<td align=center>[ \ta-zA-Z0-9&;]*</td><td align=center>[a-zA-Z0-9&;]*</td><td align=center><b>[^X]*</b></td><td align=center>[a-zA-Z0-9&;]*</td><td align=center>[a-zA-Z0-9&;]*</td>')
# Used to ensure all tests are downloaded (see test_lists.py)
reAll = re.compile('<[aA][ \t\r\na-zA-Z0-9\"_\-]* (href|HREF)="features/(?P<file_name>.*)"[ \t\r\na-zA-Z0-9\"_\-]*>(?P<feature_name>.*)</a>')
reHtmlTag = re.compile('<[ a-zA-Z0-9=\"\/]*>')
# reHtmlEntity = re.compile('&[a-zA-Z0-9];')

fileHandle = urllib.urlopen('http://www.student.cs.uwaterloo.ca/~cs444/joos.html')
fileContents = fileHandle.read()
fileHandle.close()



def convertHtmlEntities(s):
    matches = re.findall("&#\d+;", s)
    if len(matches) > 0:
        hits = set(matches)
        for hit in hits:
                name = hit[2:-1]
                try:
                        entnum = int(name)
                        s = s.replace(hit, unichr(entnum))
                except ValueError:
                        pass

    matches = re.findall("&\w+;", s)
    hits = set(matches)
    amp = "&amp;"
    if amp in hits:
        hits.remove(amp)
    for hit in hits:
        name = hit[1:-1]
        if htmlentitydefs.name2codepoint.has_key(name):
                s = s.replace(hit, unichr(htmlentitydefs.name2codepoint[name]))
    s = s.replace(amp, "&")
    return s



res = [rePositive, reNegative]
data = [
    {'re': rePositive, 'searchStr': str(fileContents), 'data': [], 'class': 'pass'},
    {'re': reNegative, 'searchStr': str(fileContents), 'data': [], 'class': 'fail'},
    # Used to ensure all tests are downloaded (see test_lists.py)
    {'re': reAll, 'searchStr': str(fileContents), 'data': [], 'class': 'all'},
    ]
for d in data:
    m = d['re'].search(d['searchStr'])
    while m != None:
        d['data'].append(m.groupdict())
        d['searchStr'] = d['searchStr'][m.end():]
        m = d['re'].search(d['searchStr'])
    for dd in d['data']:
        url = 'http://www.student.cs.uwaterloo.ca/~cs444/features/%s.html' % (dd['file_name'])
        htmlHandle = urllib.urlopen(url)
        htmlContents = htmlHandle.read()
        htmlHandle.close()
        noTags = reHtmlTag.sub('', htmlContents)
        code = convertHtmlEntities(noTags)
        codeHandle = open('tests/%s/%s_%s.joos' % (d['class'], d['class'], dd['file_name']), 'w')
        codeHandle.write('/* %s: %s */' % (d['class'], dd['feature_name']))
        codeHandle.write(str(code))
        codeHandle.close()
