SOURCES = $(wildcard *.cpp)
OBJECTS = $(SOURCES:.cpp=.o)
DEPENDS = $(SOURCES:.cpp=.d)
FLAGS = -W -Wall -g
CXX = g++
MAIN = search

all: $(MAIN)

depend: $(DEPENDS)

clean:
	rm -f *.o *.d $(MAIN)

$(MAIN): $(OBJECTS)
	@echo Creating $@...
	@$(CXX) -o $@ $(OBJECTS) $(FLAGS)

%.o: %.cpp
	@echo Compiling $<...
	@$(CXX) -o $@ -c $(FLAGS) $<

%.d: %.cpp
	@echo Building $@...
	@set -e; $(CC) -M $(FLAGS) $< \
                  | sed 's/\($*\)\.o[ :]*/\1.o $@ : /g' > $@; \
                [ -s $@ ] || rm -f $@

include $(DEPENDS)
