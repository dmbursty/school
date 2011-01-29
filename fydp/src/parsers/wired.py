#!/usr/bin/python
import urllib
import sys
import re
from BeautifulSoup import BeautifulSoup
import simplejson
from multiprocessing import Pool

def clean(text):
    """Cleans the page."""
    ret = re.compile("\\<!--\\[if.*\\<\\!\\[endif\\]--\\>", re.MULTILINE | re.DOTALL);
    return re.sub(ret, "", text)


def parse(url):
    """
    CTor.  URL is the URL of the site.
    """
    ret = {}
    ret['link'] = url
    ret['max_score'] = 10
    text = clean(urllib.urlopen(url).read())
    #print text
    soup = BeautifulSoup(text)
    score = -1
    for i in range(0,11): #TODO Optimize
        if soup.find( "div", {"class" : "rating rate-" + str(i) } ):
            score = i
            break
        if soup.find( "h4", {"class" : "rat" + str(i) } ):
            score = i
            break
    if score == -1:
        return None
    ret['score'] = score
    if soup.find("div", {"class" : "review_text"}):
        ret['title'] = soup.find("div", {"class" : "review_text"}).find("h2").text
        ret['content'] = "\n".join([x.prettify() for x in
                              soup.find( "div", {"class" : "review_text" }).
                              findAll("p")])
        print "hi"
    else:
        ret['title'] = soup.find("title").text
        ret['content'] = "\n".join([x.prettify() for x in
                              soup.find( "div", {"class" : "entry" }).
                              findAll("p")])
    ret['product'] = re.sub(r"\|.*", "", soup.find("title").text)
    return ret

def search(keyword):
    query = urllib.urlencode({'q' : 'site:http://www.wired.com/reviews ' + keyword})
    url = 'http://ajax.googleapis.com/ajax/services/search/web?v=1.0&%s' % (query)
    search_results = urllib.urlopen(url)
    json = simplejson.loads(search_results.read())
    results = json['responseData']['results']
    urls = [x['url'] for x in results]
    workers = Pool(5)
    return [x for x in workers.map(parse, urls) if x]

if __name__ == '__main__':
    for result in search(sys.argv[1]):
        for x in result:
            print x, ":", result[x]
        print