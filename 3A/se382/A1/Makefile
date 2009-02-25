TARGET=Application
SOURCES=$(wildcard *.cpp lsystem/*.cpp)
CXX=g++
CFLAGS=-Wall -Wextra -g
LDFLAGS=-Wall -Wextra -g -lX11 -lrt

OBJECTS=$(SOURCES:.cpp=.o)

$(TARGET): $(OBJECTS)
	$(CXX) -o $@ $^ $(LDFLAGS)
	#./$(TARGET)

%.o: %.cpp %.h
	$(CXX) $(CFLAGS) -o $@ -c $<

clean:
	rm -f $(TARGET) $(OBJECTS)
