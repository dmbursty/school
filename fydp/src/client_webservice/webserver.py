import cherrypy
from cherrypy import tools
from json import JSONEncoder
from SearchFilter import SearchFilter
from urllib2 import *
import solr
import MySQLdb
import getpass
import ngrams
import re

encoder = JSONEncoder()

def jsonify_tool_callback(*args, **kwargs):
    response = cherrypy.response
    response.headers['Content-Type'] = 'application/json'
    response.body = encoder.iterencode(response.body)

tools.jsonify = cherrypy.Tool('before_finalize', jsonify_tool_callback, priority=30)

class HelloWorld(object):
    def index(self):
        page = open("../frontend/prototype/index.html")
        content = page.read()
        page.close()
        return content

    index.exposed = True

class Solr(object):
    def index(self):
        return "Solr mode"

    @tools.jsonify()
    def getBlogPost(self, filter):
        conn = solr.SolrConnection('http://localhost:8983/solr')
        print filter
        parsedFilter = SearchFilter()
        parsedFilter.decode(filter)
        params = parsedFilter.toSolr()
        params['q'] = params['q'] +  " sentiment:true"
        good_response = conn.query(**(params))
        params = parsedFilter.toSolr()
        params['q'] = params['q'] +  " sentiment:false"
        bad_response = conn.query(**(params))
        conn.close()
        goodText = ""
        badText = ""
        results = good_response.results + bad_response.results
        for hit in results:
            if hit['sentiment'] == True:
                goodText += hit['content']
            elif hit['sentiment'] == False:
                badText += hit['content']
        q, p = ngrams.main(goodText, badText)
        return (q, p, [(hit['title'], hit['content'], hit['sentiment']) for hit in results if hit['content']])

    @tools.jsonify()
    def getBlog(self, filter):
        conn = solr.SolrConnection('http://localhost:8983/solr')
        print filter
        parsedFilter = SearchFilter()
        parsedFilter.decode(filter)
        print parsedFilter.toSolr()
        response = conn.query(**(parsedFilter.toSolr()))
        conn.close()
        return [hit['link'] for hit in response.results]

    index.exposed = True
    getBlogPost.exposed = True
    getBlog.exposed = True


class MySQL(object):
    def __init__(self, username, password):
        self.username = username
        self.password = password

    def index(self):
        return "Sql Mode!"

    @tools.jsonify()
    def getBlogPost(self, filter = None):
        conn = MySQLdb.connect (host = "localhost",
                                user = self.username,
                                passwd = self.password,
                                db = "fydp_db")
        parsedFilter = SearchFilter()
        parsedFilter.decode(filter)

        cursor = conn.cursor ()
        # Write to log!
        select = "SELECT title FROM fydp_db.feeditem " + parsedFilter.toSQL()
        print 'select recieved: %s' % select
        cursor.execute (select)
        #TODO make this read the limit and send that many,
        #TODO call an outside function and return the proper results depending
        # on the link
        row = cursor.fetchone()
        cursor.close ()
        conn.close ()
        return row

    @tools.jsonify()
    def getBlog(self, filter = None):
        parsedFilter = SearchFilter()
        parsedFilter.decode(filter)

        conn = MySQLdb.connect (host = "localhost",
                                user = "root",
                                passwd = "helloall",
                                db = "fydp_db")
        cursor = conn.cursor ()

        select = "SELECT feedurl FROM fydp_db.feeditem " + parsedFilter.toSQL()
        print "select received: %s" % select
        cursor.execute (select)
        row = cursor.fetchone()
        cursor.close ()
        conn.close ()
        return row

    index.exposed = True
    getBlogPost.exposed = True
    getBlog.exposed = True

print 'start page!'
root = HelloWorld()
root.mysql = MySQL('fydp', 'fydp')
root.solr = Solr()
print dir(cherrypy)
#cherrypy.config.update({'server.socket.port': 15863,})
cherrypy.quickstart(root, '/', config='webserver.config')
