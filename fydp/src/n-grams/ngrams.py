import collections, sys, os, re, math

# make sure the stopwords file is in the same dir as this script
this_dir = os.path.dirname(os.path.abspath(__file__))
stopWords = set(open("%s/stopwords" % this_dir).read().split())

def getWordsForDisplay(text):
    """
    Returns a list of 'words' that should be used for n-gram analysis.
    Arguments:
    - `text`: text to split
    """
    text = re.sub( """['"/-]""", "", text );
    text = re.sub( '[^a-zA-Z ]', " ",  text).lower()

    words = text.split()
    for i in range( 0, len(words) -1 ):
        if words[i] not in stopWords:
	    builder = [words[i]]
            j = i + 1
            while j < len(words) and words[j] in stopWords:
                builder += [words[j]]
                j = j + 1;
            if j < len(words):
                builder += [words[j]]
            words[i] = ' '.join(builder)
    words = [x for x in words if x not in stopWords]
    return words[:-1]

def getWordsForAnalysis(text):
    """
    Returns a list of 'words' that should be used for n-gram analysis.
    Arguments:
    - `text`: text to split
    """
    text = re.sub( """['"/-]""", "", text );
    text = re.sub( '[^a-zA-Z ]', " ",  text).lower()

    words = text.split()
    words = [x for x in words if x not in stopWords]

    for i in range( 0, len(words) - 3 ):
        for j in range(1, 4):
            words[i] = words[i] + " " + words[i + j]
    return words[:-1]

def main(goodText, badText, getWordsFn):
    """
    Returns a sorted list of [word, strength] pairs for the good text.
    """
    goodWordMap = collections.defaultdict(lambda : 1)
    badWordMap = collections.defaultdict(lambda : 1)
    aistuff = {}
    p = {}
    q = {}
    goodTextWords = getWordsFn(goodText)
    badTextWords = getWordsFn(badText)
    for word in goodTextWords:
        goodWordMap[word] = goodWordMap[word] + 1;
    for word in badTextWords:
        badWordMap[word] = badWordMap[word] + 1;
    goodLen = float(len(goodTextWords)) + 1
    badLen = float(len(badTextWords)) + 1
    goodFrequency = collections.defaultdict(lambda : 1. / goodLen)
    badFrequency = collections.defaultdict(lambda : 1. / badLen)
    for word in goodWordMap:
        goodFrequency[word] = float(goodWordMap[word] + 1) / goodLen
    for word in badWordMap:
        badFrequency[word] = float(badWordMap[word] + 1) / badLen
    for word in goodWordMap:
        q[word] = goodFrequency[word] * math.log(goodFrequency[word] / badFrequency[word])
    for word in badWordMap:
        p[word] = badFrequency[word] * math.log(badFrequency[word] / goodFrequency[word])

    averageFrequency = len(set(goodTextWords + badTextWords)) / float(goodLen + badLen)

    bestGood = q.items();
    bestGood.sort( key = lambda x:x[1] )
    bestGood.reverse()
    bestGood = [x for x in bestGood if (x[0] not in q or x[0] not in p) or (q[x[0]] > averageFrequency and p[x[0]] > averageFrequency) ]

    bestBad = p.items();
    bestBad.sort( key = lambda x:x[1] )
    bestBad.reverse()
    bestBad = [x for x in bestBad if (x[0] not in q or x[0] not in p) or (q[x[0]] > averageFrequency and p[x[0]] > averageFrequency) ]

    #if getWordsFn == getWordsForDisplay:
    #    bestGood = [(re.search( '[^a-zA-Z]*'.join(['[' + c + c.upper() + ']' for c in phrase[0] if c != ' '] ),
    #                            goodText ).group( 0 ), phrase[1]) for phrase in bestGood]
    #    bestBad = [(re.search( '[^a-zA-Z]*'.join(['[' + c + c.upper() + ']' for c in phrase[0] if c != ' '] ),
    #                            badText ).group( 0 ), phrase[1]) for phrase in bestBad]

    return (bestGood[:10], bestBad[:10])

if __name__ == '__main__':
    goodFile = open(sys.argv[1])
    badFile =  open(sys.argv[2])
    good, bad =  main( re.sub("<!-- BOUNDARY -->", "", goodFile.read()),
                              re.sub("<!-- BOUNDARY -->", "", badFile.read()),
			      getWordsForDisplay)
    print good
    print bad

#good - p log (p / q) + q log (q / p)
#p - probability word appears in good side
#q - probablility word appears in bad side

