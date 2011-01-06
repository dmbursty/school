CLASS = p3server.class
SOURCES = $(filter %.java, $(shell find -type f))
JC = javac
JCFLAGS = -g -classpath . -source 1.6

default: $(CLASS)

$(CLASS): $(SOURCES)

%.class : %.java
	$(JC) $(JCFLAGS) $<

clean:
	rm $(patsubst %.java, %.class, $(SOURCES))

run1: default
	java p3server 2 4

run2: default
	java p3server $(F) $(N)
