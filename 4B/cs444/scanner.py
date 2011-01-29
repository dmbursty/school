#!/usr/bin/env python2.6

import re, sys, getopt

class Token(object):
    def __init__(self, charIndex, lineNumber, charNumber, text, typeName,
        valid, postFun, pattern):
        self.charIndex = charIndex
        self.lineNumber = lineNumber
        self.charNumber = charNumber
        self.text = text
        self.typeName = typeName
        self.valid = valid
        self.postFun = postFun
        self.pattern = pattern

    def postProcess(self):
      if self.postFun is not None:
        return self.postFun(self)
      return True

class NullToken(object):
    def __init__(self):
        self.charIndex = 0
        self.lineNumber = 0
        self.charNumber = 0
        self.text = ''
        self.typeName = 'null_token'
        self.valid = False

class Scanner(object):
    def __init__(self, tokenTypes = [], charIndex = 0, lineNumber = 1, charNumber = 0):
        self.tokenTypes = tokenTypes
        self.charIndex = charIndex
        self.lineNumber = lineNumber
        self.charNumber = charNumber

    def advance(self, text):
        # Do a bunch of newline detection to capture file position data
        self.charIndex += len(text)
        nlText = str(text)
        nlText = nlText.replace('\r\n', '\n')
        nlText = nlText.replace('\n\r', '\n')
        nlText = nlText.replace('\r', '\n')
        nlCount = nlText.count('\n')
        self.lineNumber += nlCount
        if nlCount == 0:
            self.charNumber += len(text)
        else:
            rLast = text.rfind('\r')
            nLast = text.rfind('\n')
            rnLast = max(rLast, nLast)
            assert(rnLast >= 0)
            assert(len(text) > 0)
            self.charNumber = len(text) - rnLast - 1

    def parse(self, text):
        tokenStream = []
        while len(text) > 0:
            # Store list of candidates and do longest prefix match
            candidateToks = []
            for tt in self.tokenTypes:
                tok = self.match(text, tt)
                if tok != None and tok.postProcess():
                    candidateToks.append(tok)
            # Perform longest prefix match
            longestTok = NullToken()
            for c in candidateToks:
                if len(c.text) > len(longestTok.text):
                    longestTok = c
            if longestTok.valid:
                text = text[len(longestTok.text):]
                tokenStream.append(longestTok)
                self.advance(longestTok.text)
            else: # Reject and halt on "forbidden tokens" (including null token)
                break
        if len(text) == 0:
            status = True
        else:
            status = False
        return (status, tokenStream, text)

    def match(self, text, tokenType):
        m = tokenType.regExp.match(text)
        if m != None:
            tok = Token(self.charIndex, self.lineNumber, self.charNumber,
                m.group(0), tokenType.typeName, tokenType.valid,
                tokenType.postFun, tokenType.regExp.pattern)
        else:
            tok = None
        return tok

class TokenType(object):
    def __init__(self, typeName, regExp, valid = True, postFun = None):
        self.typeName = typeName
        self.regExp = regExp
        self.valid = valid
        self.postFun = postFun

def tok2html(tokenStream, unscannedText = ''):
    lineNumber = 1
    lnSpan = '<span class=\"lineNumber\">'
    text = '<html>\n<head><link href=\"scanner.css\" rel=\"stylesheet\" type=\"text/css\" /></head>\n<body>\n<pre>%s %04d </span>' % (lnSpan, 1)
    for tok in tokenStream:
        tokSpan = '<span class=\"%s\" title=\"%s: filechar %d line %d char %d\">' % (
            tok.typeName,
            tok.typeName,
            tok.charIndex,
            tok.lineNumber,
            tok.charNumber,
            )
        tokText = tok.text
        tokText = tokText.replace('\r\n', '\n')
        tokText = tokText.replace('\n\r', '\n')
        tokText = tokText.replace('\r', '\n')
        while tokText.find('\n') != -1:
            lineNumber += 1
            tokText = tokText.replace(
                '\n',
                '</span></pre>__NL__<pre>%s %04d </span>%s' % (
                    lnSpan,
                    lineNumber,
                    tokSpan,
                    ),
                1
                )
        tokText = tokText.replace('__NL__', '\n')
        text += '%s%s</span>' % (tokSpan, tokText)
    text += '</pre>\n<pre><span class=\"unscanned\">%s</span></pre>\n</body>\n</html>\n' % unscannedText
    return text

reEscapeSequences = r'\\b|\\t|\\n|\\f|\\r|\\\"|\\\'|\\\\|\\[0-3]?[0-7]?[0-7]'
r = re.compile
tokenTypes = [
    TokenType(
        'whitespace',
        r('^[ \t\f]+'),
        True,
        ),
    TokenType(
        'newline',
        r('^(\r\n)'),
        True,
        ),
    TokenType(
        'newline',
        r('^(\r|\n)'),
        True,
        ),
    TokenType(
        'sl_comment',
        r('^\/\/[^\r\n]*'),
        True,
        ),
    TokenType(
        'ml_comment',
        r('^\/\*([^\*]|\*[^\/])*\*\/'),
        True,
        ),
    TokenType(
        'float_literal_forbidden',
        r('\d+(([eE][+\-]?\d+)?[dDfF]|([eE][+\-]?\d+)[dDfF]?)'),
        False,
        ),
    TokenType(
        'float_literal_forbidden',
        r('(\d+\.\d*|\.\d+)([eE][+\-]?\d+)?[dDfF]?'),
        False,
        ),
    TokenType(
        'hex_literal_forbidden',
        r('^0[xX][0-9]+'),
        False,
        ),
    TokenType(
        'oct_literal_forbidden',
        r('^0[0-7]+'),
        False,
        ),
    TokenType(
        'max_int_literal',
        r('^2147483648'),
        True,
        ),
    TokenType(
        'int_literal',
        r('^(0|[1-9][0-9]*)'),
        True,
        lambda tok: long(tok.text) <= 2**31
        ), 
    TokenType(
        'char_literal',
        r('^\'([^\\\\\'\n\r]|%s)\'' % reEscapeSequences),
        True,
        ),
    TokenType(
        'string_literal',
        r('^\"([^\\\\\"\n\r]|%s)*\"' % reEscapeSequences),
        True,
        ),
    TokenType(
        'bool_literal',
        r('^(true|false)'),
        True,
        ),
    TokenType(
        'null_literal',
        r('^null'),
        True,
        ),
    TokenType( #incr decr bitshift
        'operator_forbidden',
        r('^([+][+]|--|<<|>>|>>>)'),
        False,
        ),
    TokenType( # assignment ops
        'operator_forbidden',
        r('^([+]=|-=|[*]=|/=|%=|[|]=|&=|\^=|<<=|>>=|>>>=)'),
        False,
        ),
    TokenType( # bitwise ops that are not eager boolean, choice ops
        # note: the : character is completely forbidding because labels aren't
        # supported either
        'operator_forbidden',
        r('^[~^?:]'),
        False,
        ),
    TokenType( # allowed comparison ops
        'operator',
        r('^(==|!=|[|][|]|&&|<=|>=|>|<)'),
        True,
        ),
    TokenType( # allowed arithmetic, eagar bool, assign =
        'operator',
        r('^[=+*/%|!&\-]'),
        True,
        ),
    TokenType(
        'separator',
        r('^[\[\](){};,\.]'),
        True,
        ),
    TokenType(
        'keyword_forbidden',
        r('^(synchronized|transient|continue|strictfp|volatile|default|finally|private|double|switch|throws|break|catch|const|float|super|throw|case|goto|long|try|do)'),
        False,
        ),
    TokenType(
        'keyword',
        r('^(implements|instanceof|interface|protected|abstract|boolean|extends|package|import|native|public|return|static|class|final|short|while|byte|char|else|this|void|for|int|new|if)'),
        True,
        ),
    TokenType(
        'identifier',
        r('^[a-zA-Z_$][a-zA-Z0-9_$]*'),
        True,
        ),
    ]




optlist, args = getopt.getopt(sys.argv[1:], 't:o:')
if len(args) == 0:
    print 'Error: No input file specified'
    exit(1)

validOpts = {'t': ['status', 'html']}
opts = {'t': 'status', 'o': None}
for o in optlist:
    flag = o[0][1:]
    val = o[1]
    if flag in opts:
        if flag not in validOpts or flag in validOpts and val in validOpts[flag]:
            opts[flag] = val
        else:
            print 'Invalid option or value -%s %s' % (flag, val)
            exit(1)

# FixMe: Should do a range check on args... or something...
fileName = args[0]

fileHandle = open(fileName, 'r')
fileContents = fileHandle.read()
fileHandle.close()

# check if there are non-ascii UTF8 chars 
try: 
    fileContents.decode('ascii')
except UnicodeDecodeError:
    status = False
    tokenStream = []
    text = fileContents
else:
    scanner = Scanner(tokenTypes)
    status, tokenStream, text = scanner.parse(fileContents)


if opts['t'] == 'status':
    if status:
        output = 'Scan ok\r\n'
    else:
        output = 'Scan not ok\r\n'
elif opts['t'] == 'html':
    output = tok2html(tokenStream, text)
else:
    print 'Error: Invalid output type %s' % (opts['t'])

if opts['o'] == None:
    outFile = sys.stdout
else:
    outFile = open(opts['o'], 'w')
outFile.write(output)
if outFile != sys.stdout:
    outFile.close()
if status:
    exit(0)
else:
    exit(42)
