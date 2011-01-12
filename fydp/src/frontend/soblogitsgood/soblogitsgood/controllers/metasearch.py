import logging

from multiprocessing import Pool

from pylons import request, response, session, tmpl_context as c, url
from pylons.controllers.util import abort, redirect

from soblogitsgood.lib.base import BaseController, render

log = logging.getLogger(__name__)

import sys
sys.path.append("/usr/lib/pymodules/python2.6")
sys.path.append("/usr/lib/python2.6/dist-packages/")
sys.path.append("/usr/local/lib/python2.6/dist-packages/")
sys.path.append("/usr/local/lib/python2.6/dist-packages/mechanize-0.2.4-py2.6.egg")
sys.path.append("../../parsers/")
sys.path.append("../../n-grams")
import ngrams
import wired
import imdb
import epinions
import cnet
import buzzillions

class MetasearchController(BaseController):
    # Dispatchers return results as dictionaries with the following format:
    #    title
    #    link
    #    content
    #    score*
    #    max_score*
    #    title_section*
    # Where asterisks mark optional fields
  def __init__(self):
    self.pool = Pool(len(self.getSearchers()))

  def getSearchWrappers(self, query):
    '''Generates functions that search for given query.
       Inner functions only need to know which searcher to use.
    '''
    def wrapper(searcher):
      return searcher(query)
    return wrapper

  def index(self):
    # Return a rendered template
    #return render('/metasearch.mako')
    # or, return a string
    c.service = "search"
    return render('/index.mako')

  def getSearchers(self):
#    return [wired.search, imdb.search, buzzillions.search, cnet.search]
    return [buzzillions.search, imdb.search, epinions.search] #, wired.search, ]

  def getAllResults(self, query):
    query = query.replace(' ', '+')
    searchers = self.getSearchers()
    workers = [self.pool.apply_async(searcher, (query,)) 
    for searcher in searchers] 

    results = []
    i = 0
    while workers:
      i = (i + 1) % len(workers)
      worker = workers[i]
      worker.wait(3)
      if not worker.ready(): continue

      workers.pop(i)
      # Watch out if the text transfer time is too high shit can hit the fan
      # Using a callback function may fix this in the future
      current_results = worker.get()
      for result in current_results:
        try:
          result['sentiment'] = float(result['score']) / float(result['max_score'])
        except (KeyError, TypeError), e:
          result['sentiment'] = 1

        if result['content'] is None:
          result['content'] = ""
      results += current_results


    return results
    
  def search(self):
    c.service = "search"
    c.query = request.params['query']

    if c.query.strip() == "":
      return render('/index.mako')

    c.results = self.getAllResults(c.query)

    return render('/metaresults.mako')

  def polarize(self):
    c.service = "polarize"
    c.query = request.params['query']

    if c.query.strip() == "":
      return render('/index.mako')

    c.results = self.getAllResults(c.query)

    if len(c.results) == 0:
      return render('/noresults.mako')

    good_results = [hit for hit in c.results if hit['sentiment'] >= 0.5]
    bad_results = [hit for hit in c.results if hit['sentiment'] < 0.5]

    goodText = ''.join(hit['content'] for hit in good_results)
    badText = ''.join(hit['content'] for hit in bad_results)

    c.goodTerms, c.badTerms = ngrams.main(goodText, badText, ngrams.getWordsForDisplay)

    c.goodResults = good_results
    c.badResults = bad_results

    return render('/polarize.mako')

  def analysis(self):
    c.service = "analysis"
    c.query = request.params['query']

    if c.query.strip() == "":
      return render('/index.mako')

    c.results = self.getAllResults(c.query)

    if len(c.results) == 0:
      return render('/noresults.mako')

    good_results = [hit for hit in c.results if hit['sentiment'] >= 0.5]
    bad_results = [hit for hit in c.results if hit['sentiment'] < 0.5]

    goodText = ''.join(hit['content'] for hit in good_results)
    badText = ''.join(hit['content'] for hit in bad_results)

    c.goodTerms, c.badTerms = ngrams.main(goodText, badText, 
        ngrams.getWordsForDisplay)

    c.goodResults = good_results
    c.badResults = bad_results

    return render('/analysis.mako')

  def custom(self):
    c.service = "custom"
    c.query = request.params['query']

    if c.query.strip() == "":
      return render('/index.mako')

    c.results = self.getAllResults(c.query)

    if request.params.has_key('start'):
      start = float(request.params['start'])
    else:
      start = 0.0

    if request.params.has_key('end'):
      end = float(request.params['end'])
    else:
      end = 1.0
    
    c.results = [hit for hit in c.results \
        if hit['sentiment'] <= end and hit['sentiment'] >= start]

    if len(c.results) == 0:
      return render('/noresults.mako')

    c.start = start
    c.end = end

    return render('/custom.mako')

# vi: set ts=2 sts=2 sw=2:
