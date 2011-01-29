fail_cases = [
# Strings
r'"""',
r'"\"',
r'"\8"',
r'"\a"',
r'"\A"',
r'"\u0000"',
r'"\x00"',
'"\n"',
'"\r"',
# Chars
'\'\'\'',
'\'\\400\'',
'\'\r\'',
'\'\n\'',
'\'\\u0000\'',
'\'\\x00\'',
'\'\\8\'',
'\'\'',
]

for (i, case) in enumerate(fail_cases):
    f = open("invalid_string_%d.java" % (i+1), 'w')
    f.write(case + "\r\n")
