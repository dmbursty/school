import MySQLdb
import httplib, urllib
import htmllib
from datetime import datetime
from dateutil import parser
from dateutil.tz import *
from xml.sax.saxutils import escape


# unused; escape_string doesn't exist anymore
def store_mysql(entries):
    print "store_mysql"
    date_format = "%Y-%m-%d %H:%M:%S"
    try:
        conn = MySQLdb.connect (host = "localhost",
                               user = "fydp",
                               charset = "utf8",
                               passwd = "fydp",
                               db = "fydp_db")
    except MySQLdb.Error, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        exit(1)

    try: 
        cursor = conn.cursor ()
        for entry in entries:
            guid = escape_string(entry.guid)
            feedurl = escape_string(entry.feedurl)
            content = escape_string(entry.content)
            title = escape_string(entry.title)
            link = escape_string(entry.link)
            author = escape_string(entry.author)
            comments = escape_string(entry.comments)
            date = get_date(entry.published)

            cursor.execute ("""
                INSERT INTO feeditem (guid, feedurl, content, title, link, author,
                    comments, published)
                VALUES
                    ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')
            """ % (guid, feedurl, content, title, link, author, comments,
            date.strftime(date_format))
            )
    except MySQLdb.Error, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        exit(1)

    conn.commit()
    conn.close()


def store_solr(entries):
    print "store_solr"
    date_format = "%Y-%m-%dT%H:%M:%SZ"

    headers = {"Content-Type": "text/xml; charset=utf-8"};
    conn = httplib.HTTPConnection("localhost:8983");
    body = "<add>"
    template = """<doc><field name="guid">%s</field>
        <field name="title">%s</field>
        <field name="author">%s</field>
        <field name="link">%s</field>
        <field name="content">%s</field>
        <field name="published">%s</field>
        <field name="feedurl">%s</field>
        <field name="sentiment">%s</field>
        </doc>""".encode('utf-8')

    for entry in entries:
        guid = escape_string_xml(entry.guid)
        feedurl = escape_string_xml(entry.feedurl)
        title = escape_string_xml(entry.title)
        author = escape_string_xml(entry.author)
        link = escape_string_xml(entry.link)
        comments = escape_string_xml(entry.comments)
        content = escape_string_xml(entry.content)

        date = get_date(entry.published)
        date = date.strftime(date_format).encode('utf-8')

        # this is where sentiment is decided
        sent = get_sentiment(entry)

        body += template % (guid, title, author, link, content, date, feedurl, sent)

    body += "</add>"

    conn.request("POST", "/solr/update?commit=true", body, headers)
    resp = conn.getresponse()
    if resp.status != 200:
        print "Error while storing in Solr"
        f = open("badlog", "r+")
        f.write(body)
        print resp.status
        print resp.read()
    conn.close()

# transform a raw date string into a datetime object; if the string is null or
# empty, choose now as the time
def get_date(raw):
    if not raw:
        return datetime.now(tzutc())
    else:
        return parser.parse(raw)

# takes a string and makes it safe for entry into Solr XML
# replaces nulls with empty strings, encodes it into utf-8, unescapes any HTML
# entities and escapes XML ones
def escape_string_xml(str):
    if not str:
        return "".encode('utf-8')

    str = str.encode('utf-8')

    # unescape html
    str = unescape_html(str)
    # escape XML
    str = escape(str)
    return str

# unescapes the HTML entities, and then re-escapes XML entities
# for Solr, since the request is supposed to be in XML, and since HTML
# entities will cause Solr to choke.
def unescape_html(s):
    p = htmllib.HTMLParser(None)
    p.save_bgn()
    p.feed(s)
    return p.save_end()

def get_sentiment(entry):
  return "true".encode('utf-8')
