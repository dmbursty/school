CLASS = S_StreamSocket.class
SOURCES = $(filter %.java, $(shell find -type f))
JC = javac
JCFLAGS = -g -classpath . -source 1.5

default: $(CLASS)

$(CLASS): $(SOURCES)

%.class : %.java
	$(JC) $(JCFLAGS) $<

clean:
	rm $(patsubst %.java, %.class, $(SOURCES))
