#!/usr/bin/python
import mechanize
import re
from BeautifulSoup import BeautifulSoup
from multiprocessing import Pool

def init():
    g = globals()
    g['br'] = mechanize.Browser()
    g['get_text'] = re.compile('^<tr bgcolor="white">(.*?)^</tr>',
        re.DOTALL | re.MULTILINE)
    g['get_title'] = re.compile('<h1 class="title" style="padding-bottom:5px;">(.*?)</h1>', re.DOTALL | re.MULTILINE)
    g['get_rating'] = re.compile('alt="Product Rating: (.*?)"', re.DOTALL)

def open_urls((url, max_results)):
    br.open(url)
    seen_reviews = set()
    review_links = [l for l in br.links(url_regex="/review/")][:max_results]

    results = []
    for review_link in review_links:
        if review_link.absolute_url in seen_reviews: continue
        seen_reviews.add(review_link.absolute_url)
        response = br.follow_link(review_link)
        raw_review = response.read()
        review = raw_review[raw_review.find("Full Review:"):]
        review = review.split('\n')
        # After getting the a Full Review Tag the next line containing
        # nothing but whitespace is followed by the review
        for j in xrange(len(review)):
            if not review[j].strip(): break
                                                                            
        ret = {}
        page = review[j + 1].strip()
        title = get_title.search(raw_review).group(1).strip()
        title_text = ''.join(BeautifulSoup(title,
            convertEntities = BeautifulSoup.HTML_ENTITIES).findAll(text=True))
        title_text = title_text[:title_text.find('-')]
        content = ''.join(BeautifulSoup(page,
            convertEntities = BeautifulSoup.HTML_ENTITIES).findAll(text=True))

        ret['content'] = content
        ret['title'] = title_text
        ret['link'] = review_link.absolute_url
        ret['max_score'] = 5
        ret['score'] = None

        try:
            rating = get_rating.search(raw_review).group(1).strip()
            ret['score'] = rating     
        except IndexError:
            pass
        except AttributeError:
            pass
        results += [ret]
        br.back()
    return results


def search (keyword, max_results = 5):
    base_url = "http://www99.epinions.com/search/?search_string=%s" % (keyword)
    br = mechanize.Browser()
    br.open(base_url)
    seen_prices = set()
    all_links = [l for l in br.links(url_regex='/prices/')]
    for link in all_links:
        if link.absolute_url in seen_prices: continue
        link.absolute_url = link.absolute_url.replace('/prices/', '/reviews/')
        seen_prices.add(link.absolute_url)

    urls = [(url, max_results) for url in seen_prices][:5]
    workers = Pool(5, init)
    
    results = []
    for result in workers.imap(open_urls, urls):
        results += [result]
    return results

print __name__
if __name__ == '__main__':
    print 'here i am'
    for result in search('starcraft'):
        print result
