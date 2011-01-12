from BeautifulSoup import BeautifulSoup
import re, sys, urllib
from multiprocessing import Pool

score_pattern = re.compile("(\d)\.0 out of 5 stars")
product_pattern = re.compile("http://www.amazon.com/([^/]*)/")
#title_pattern = re.compile("vertical-align:middle;.....(.*)./b.")
title_pattern = re.compile("vertical-align:middle;.....(.*)./b.")

def parse_url(url):
    text = urllib.urlopen(url).read()
    reviews = text.split("<!-- BOUNDARY -->")
    reviews = reviews[1:]
    lst = []
    for review in reviews:
        try:
            ret = {}
            ret['link'] = url
            ret['max_score'] = 5
            ret['score'] = score_pattern.search(review).groups()[0]
            ret['product'] = product_pattern.search(url).groups()[0]
            ret['title'] = title_pattern.search(review).groups()[0]
            lst.append(ret)
        except:
            pass
    return lst


def search(keyword):
    url = "http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=" + keyword + "%s&x=0&y=0"
    soup = BeautifulSoup(urllib.urlopen(url).read())
    workers = Pool(5)
    urls = [x.a['href'] for x in soup.findAll('div', {"class": "productTitle"})]
    print urls
    return workers.map(parse_url, urls)

if __name__ == '__main__':
    print search(sys.argv[1])

