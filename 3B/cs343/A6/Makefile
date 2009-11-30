TARGET=webBrowser
SOURCES=$(wildcard *.cpp)
CXX=u++
CFLAGS=-Wall -g
LDFLAGS=-Wall -g

OBJECTS=$(SOURCES:.cpp=.o)

$(TARGET): $(OBJECTS)
	$(CXX) -o $@ $^ $(LDFLAGS)

%.o: %.cpp %.h
	$(CXX) $(CFLAGS) -o $@ -c $<

clean:
	rm -f $(TARGET) $(OBJECTS)
