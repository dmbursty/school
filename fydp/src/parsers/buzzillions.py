#!/usr/bin/python
import mechanize
import re
from BeautifulSoup import BeautifulSoup
from multiprocessing import Pool

def init():
    g = globals()
    g['br'] = mechanize.Browser()
    g['get_title'] = re.compile('<h3 class="summary">(.*?)</h3>', 
       re.DOTALL | re.MULTILINE)
    g['get_comments'] = re.compile(
       '<p class="bz-model-review-comments description">(.*?)</p>', 
       re.DOTALL | re.MULTILINE)
    g['get_ratio'] = re.compile(
       '<span class="prReviewHelpfulCount">(.*?)</span>', re.DOTALL)
    g['get_rating'] = re.compile('<span class="rating">(.*?)</span>',
        re.DOTALL)

def open_urls((url, max_results)):
    results = []
    response = br.open(url)
    output = response.read()
    output = output[output.find('<a name="bz-model-reviews-top">'):]
    titles = get_title.findall(output)
    comments = get_comments.findall(output)
    title_section = get_ratio.findall(output)
    ratings = get_rating.findall(output)
    for i in xrange(len(titles)):
        ret = {}
        ret['max_score'] = 5
        ret['score'] = ratings[i].strip()
        page = comments[i].strip() 
        title = titles[i].strip()
        title_text = ''.join(BeautifulSoup(title,
          convertEntities = BeautifulSoup.HTML_ENTITIES).findAll(text=True))
        content = ''.join(BeautifulSoup(page,
          convertEntities = BeautifulSoup.HTML_ENTITIES).findAll(text=True))
        ret['content'] = content
        ret['title'] = title_text
        ret['link'] = url
        results += [ret]
        if len(results) >= max_results: 
          return results
    return results

def search (keywords, max_results = 10):
    """
    Function search(keywords)
    Searches buzillions for the current set of keywords, note buzillions
    seems to aggressively throttle when more then 3 links are opened ?
    parameters:
    keywords - A string with all the keywords to search
    Output:
    Outputs a dictionary with the following keys
    title    - The title of this review
    title_section - The entire section containing the title in this review
    content  - The content of this review
    link     - The link that lead to this review
    """
    base_url = ("http://www.buzzillions.com/x/s?N=4294811422&D=x&cat=&extra=all-product&Ntt=%s" % keywords)
    print base_url

    workers = Pool(5, init)
    br = mechanize.Browser()
    br.open(base_url)
    all_links = [link for link in br.links(url_regex="/reviews/")]
    seen_urls = set()
    for link in all_links:
        sublink = link.absolute_url.split('#')[0]
        if sublink in seen_urls: continue
        seen_urls.add(sublink)
    urls = [(url, max_results) for url in seen_urls][:3]
    print urls
    return workers.map(open_urls, urls)

print __name__
if __name__ == '__main__':
    for result in search('titanic'):
        print result
