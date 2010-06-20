import collections, sys, os, re, math

def main(goodText, badText):
    """
    Returns a sorted list of [word, strength] pairs for the good text.
    """
    goodWordMap = collections.defaultdict(lambda : 1)
    badWordMap = collections.defaultdict(lambda : 1)
    aistuff = {}
    p = {}
    q = {}
    for word in goodText.split():
        goodWordMap[word] = goodWordMap[word] + 1;
    for word in badText.split():
        badWordMap[word] = badWordMap[word] + 1;
    goodLen = float(len(goodText.split())) + 1
    badLen = float(len(badText.split())) + 1
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
    bestGood = q.items();
    bestGood.sort( key = lambda x:x[1] )
    bestGood.reverse()

    bestBad = p.items();
    bestBad.sort( key = lambda x:x[1] )
    bestBad.reverse()

    return (bestGood[:10], bestBad[:10])

if __name__ == '__main__':
    goodFile = open(sys.argv[1])
    badFile =  open(sys.argv[2])
    for (word, prob) in main( re.sub("<!-- BOUNDARY -->", "", goodFile.read()),
                              re.sub("<!-- BOUNDARY -->", "", badFile.read()) )[:20]:
        print (word, prob)

#good - p log (p / q) + q log (q / p)
#p - probability word appears in good side
#q - probablility word appears in bad side

