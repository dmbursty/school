#!/usr/bin/env python

# Usage:
#   python test.py [options] [test1] [test2] ...
# Options:
#   -t <log|html>     Output format
#   -o <file>         Results output file
#   -O <dir>          Output path
#   -v <0|1|2>        Verbosity setting (0 = silent, 2 = verbose)
#   -s <suite>        Suite of tests to run
#
# All tests in suite will be run, unless specific test names are given as args

import sys, getopt, os, subprocess

def arrayChunks(l, n):
    for i in xrange(0, len(l), n):
        yield l[i:i+n]

class LogWriter(object):
    def __init__(self, fileObj):
        self.f = fileObj

    def record(self, test):
        if test['success']:
            self.f.write('SUCCESS: %s: returned %s\r\n' % (test['name'], test['return']))
        else:
            self.f.write('FAILURE: %s: returned %s\r\n' % (test['name'], test['return']))

    def __del__(self):
        self.f.close()

class HtmlWriter(object):
    def __init__(self, fileObj):
        self.f = fileObj
        self.f.write('<html>\n<head><link href=\"test.css\" rel=\"stylesheet\" type=\"text/css\" /></head>\n<body>')

    def record(self, test):
        if test['success']:
            self.f.write('<a class="success" href="%s">SUCCESS</a>: <a href="%s">%s</a>: returned %s<br />\n' 
                % (test['outputPath'], test['inputPath'], test['name'], test['return']))
        else:
            self.f.write('<a class="failure" href="%s">FAILURE</a>: <a href="%s">%s</a>: returned %s<br />\n' 
                % (test['outputPath'], test['inputPath'], test['name'], test['return']))

    def __del__(self):
        self.f.write('</body>\n</html>')
        self.f.close()

def findTestByName(suite, name):
    for test in suite:
        if test['name'] == name:
            return test

    return None

# Test program for scanner
def testScanner(writer, procOutDir, verbosity, testnames):
    from test_config import scannerSuite

    # Handle test arguments
    if testnames == []:
        scannerTests = scannerSuite
    else:
        scannerTests = []
        for testname in testnames:
            test = findTestByName(scannerSuite, testname)
            if test is None:
                print "Couldn't find test '%s' in suite" % testname
            else:
                scannerTests.append(test)

    testChunks = arrayChunks(list(scannerTests), 9)
    completedTests = []
    for tests in testChunks:
        for t in tests:
            t['outputPath'] = '%s/scanner_%s.html' % (procOutDir, t['name'])
            procOutFile = open(t['outputPath'], 'w')
            procArgs = ['python', 'scanner.py', '-t', 'html', '%s' % t['inputPath']]
            t['proc'] = subprocess.Popen(procArgs, stdout = procOutFile)
        for t in tests:
            t['return'] = t['proc'].wait()
            t['success'] = t['return'] == t['expectedReturn']
            if verbosity == 1:
              sys.stdout.write('-' if t['success'] else 'F')
              sys.stdout.flush()
            elif verbosity == 2:
              print '%s finished with return code %s' % (t['name'], str(t['return']))
            writer.record(t)
        completedTests += tests
    print ""
    return completedTests

if __name__ == "__main__":
    # Handle command line arguments
    optlist, args = getopt.getopt(sys.argv[1:], 'v:t:o:O:s:')
    validOpts = {'t': ['log', 'html'], 'v': ['0', '1', '2']}
    opts = {'t':'log', 'o':None, 'O':'tests/output', 'v':'1', 's':'Unspecified'}
    for o in optlist:
        flag = o[0][1:]
        val = o[1]
        if flag in opts:
            if flag not in validOpts or (flag in validOpts and val in validOpts[flag]):
                opts[flag] = val
            else:
                print 'Invalid option or value -%s %s' % (flag, val)
                exit(1)

    # Handle flags
    if opts['o'] == None:
        outFile = sys.stdout
    else:
        outFile = open(opts['o'], 'w')
    if opts['t'] == None or opts['t'] == 'log':
        writer = LogWriter(outFile)
    elif opts['t'] == 'html':
        writer = HtmlWriter(outFile)
    else:
        print 'Error: invalid output type specified %s' % (opts['t'])
        exit(1)

    if opts['s'] == 'scanner':
        testScanner(writer, opts['O'], int(opts['v']), args)
    else:
        print 'Error: Unknown test suite: %s' % opts['s']
