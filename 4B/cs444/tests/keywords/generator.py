fail_cases = [
"synchronized",
"transient",
"continue",
"strictfp",
"volatile",
"default",
"finally",
"private",
"double",
"switch",
"throws",
"break",
"catch",
"const",
"float",
"super",
"throw",
"case",
"goto",
"long",
"try",
"do",
]

for (i, case) in enumerate(fail_cases):
    f = open("invalid_keyword_%d.java" % (i+1), 'w')
    f.write(case + "\r\n")
