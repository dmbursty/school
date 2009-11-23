TARGET=for2while
SOURCES=$(wildcard *.cpp)
CXX=g++
FLAGS=-Wall -Wextra -g

OBJECTS=$(SOURCES:.cpp=.o)

$(TARGET): $(OBJECTS)
	$(CXX) -o $@ $^ $(FLAGS)

%.o: %.cpp %.h
	$(CXX) $(FLAGS) -o $@ -c $<

clean:
	rm -f $(TARGET) $(OBJECTS)
