#!/usr/bin/python
import mechanize
import re
from BeautifulSoup import BeautifulSoup
from multiprocessing import Pool

def init():
    g = globals()
    g['br'] = mechanize.Browser()
    g['get_text'] = re.compile('^<p>(.*?)</p>', re.DOTALL | re.MULTILINE)
    g['get_title'] = re.compile('^<b>(.*?)</b>', re.DOTALL | re.MULTILINE)
    g['get_rating'] = re.compile('showtimes/(.*?).gif', re.DOTALL)

def open_urls((url, max_results)):
    response = br.open(url)
    output = response.read()
    matches = get_text.findall(output)
    results = []
    for i in xrange(1, len(matches) - 1, 2):
        ret = {}
        ret['title_section'] = matches[i]
        page = matches[i + 1].strip()
        content = ''.join(BeautifulSoup(page, 
          convertEntities = BeautifulSoup.HTML_ENTITIES).findAll(text=True))
        ret['content'] = content
        title = get_title.search(matches[i]).group(0)
        title_text = ''.join(BeautifulSoup(title,
          convertEntities = BeautifulSoup.HTML_ENTITIES).findAll(text=True))
        ret['title'] = title_text
        ret['link'] = url
        ret['max_score'] = 100
        ret['score'] = None
        try:
            rating = get_rating.search(matches[i]).group(1)
            ret['score'] = rating     
        except IndexError:
            pass
        except AttributeError:
            pass

        results += [ret]
        if len(results) >= max_results: return results
    return results

def search (keyword, max_results = 10):
    base_url = "http://www.imdb.com/find?s=all&q=%s" % (keyword)
    br = mechanize.Browser()
    br.open(base_url)
    workers = Pool(5, init)
    urls = [(link.absolute_url + 'usercomments', max_results) for link in
        br.links(url_regex="/title/")][:5]
    print urls
    return workers.map(open_urls, urls)

if __name__ == '__main__':
    print 'here i am'
    for result in search('starcraft'):
        print result
