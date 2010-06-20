import time, FeedEntry
import feedparser
import re
import urllib

class Feed:
    """Class representing a Feed that needs to be periodically updated."""
    def __init__(self, url, lastEntry = None, updateInterval = 15 * 60 * 4, lastUpdated = 0, saved = False):
        self.url = url
        self.feedEntries = None
        self.__saved__ = saved
        self.__lastEntry__ = lastEntry
        self.updateInterval = updateInterval
        self.lastUpdated = lastUpdated

    def update(self):
        print 'updating url ' + self.url
        feed = feedparser.parse( self.url )
        self.feedEntries = []
        self.lastUpdated = time.time()
        if len(feed.entries) == 0:
            return
        firstEntry = feed['entries'][0].link
        for entry in feed.entries:
            author = None
            comments = None
            guid = None
            updated = None
            summary = ""

            if entry.link == self.__lastEntry__:
                self.__lastEntry__ = firstEntry
                return;
            try:
                author = entry.author
            except AttributeError:
                pass

            try:
                comments = entry.comments
            except AttributeError:
                pass

            try:
                updated = entry.updated
            except AttributeError:
                pass

            try:
                summary = entry.summary
            except AttributeError:
                pass

            content = self.__retrieve_content__(summary, entry.link)
            re.sub( "<[^>]*?>", "", content)
            self.feedEntries.append(
                FeedEntry.FeedEntry(entry.link,
                                    feed.url,
                                    re.sub( "<[^>]*?>", "", content),
                                    content,
                                    entry.title,
                                    entry.link,
                                    author,
                                    comments,
                                    updated))
        self.__lastEntry__ = firstEntry;

    def __retrieve_content__(self, str, link):
        filehandle = urllib.urlopen(link)
        content = filehandle.read()
        contentTypeSplit = filehandle.headers['content-type'].split('charset=');
        encoding = 'ISO-8859-1'
        if len( contentTypeSplit ) == 2:
          encoding = contentTypeSplit[-1]
        ucontent = unicode(content, encoding)
        return str + ucontent

    def save(self, c):
        print 'saving feed ' + self.url
        if not self.__saved__:
            c.execute( "INSERT INTO feeds VALUES (?, ?, ?, ?)", (self.url,
                                                                 self.updateInterval,
                                                                 self.lastUpdated,
                                                                 self.__lastEntry__) )
            self.__saved__ = True
        else:
            c.execute( "UPDATE feeds SET last_updated= ?, last_entry = ? WHERE feed_url=?", (self.lastUpdated,
                                                                                             self.__lastEntry__,
                                                                                             self.url) )
        c.commit()

    #
    # Methods defined so that UpdateQueue sorts properly
    #
    def __eq__(self, other):
        return self.lastUpdated + self.updateInterval == other.lastUpdated + other.updateInterval

    def __ne__(self, other):
        return self.lastUpdated + self.updateInterval != other.lastUpdated + other.updateInterval

    def __lt__(self, other):
        return self.lastUpdated + self.updateInterval < other.lastUpdated + other.updateInterval

    def __gt__(self, other):
        return self.lastUpdated + self.updateInterval > other.lastUpdated + other.updateInterval

    def __le__(self, other):
        return self.lastUpdated + self.updateInterval <= other.lastUpdated + other.updateInterval

    def __ge__(self, other):
        return self.lastUpdated + self.updateInterval >= other.lastUpdated + other.updateInterval
