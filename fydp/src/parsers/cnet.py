#!/usr/bin/python
#import mechanize
from BeautifulSoup import BeautifulSoup
import re
import sys
import urllib
from multiprocessing import Pool

def parse(url):
    try:
        ret = {}
        output = urllib.urlopen(url).read()
        soup = BeautifulSoup(output)
        ret['score'] = soup.find("span", {"class" : "rating"}).text
        ret['max_score'] = 5
        ret['link'] = url
        ret['title'] = soup.find("title").text
        get_content = re.compile('<p><strong>(.*)</?p> <div class=\"reviewWrap\"', re.DOTALL | re.MULTILINE)
        #matches = get_content.findall(output);
        if get_content.findall(output):
            ret['content'] = get_content.findall(output)[0]
        else:
            ret['content'] = soup.find("div", {"class" : "reviewSummary"}).text
        return ret
    except:
        return None

def search(keyword):
    base_url = "http://reviews.cnet.com/1770-5_7-0.html?query=%s&tag=srch" % (keyword)
    output = urllib.urlopen(base_url).read()
    soup = BeautifulSoup(output)
    workers = Pool(5)
    urls = [("http://reviews.cnet.com" + x.find("a", {"class" : "resultName"})['href'] + "?tag=contentMain;contentBody;1r")
            for x in soup.findAll("div", {"class" : "resultInfo"})]
    for url in urls: print url
    return [x for x in workers.map(parse, urls) if x]

    #return soup

if __name__ == '__main__':
    for result in search(sys.argv[1]):
        for key in result:
            print key
            print result[key]
            print
        print

