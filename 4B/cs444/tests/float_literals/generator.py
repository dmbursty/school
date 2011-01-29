fail_cases = [
"1.",
"1.1",
"1.1e2",
"1.1e2f",
"1.e-2",
"1.e-2F",
"1.1f",
"1.f",
".1",
".1e+2",
".1e+2d",
".1D",
"1e+2",
"1e+2D",
"1f",
"12345.67890",
"12345.67890D",
"09876.54321",
"0e0",
]

for (i, case) in enumerate(fail_cases):
    f = open("invalid_float_%d.java" % (i+1), 'w')
    f.write(case + "\r\n")
