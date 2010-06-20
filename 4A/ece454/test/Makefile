CC=g++
CFLAGS=-g -O0 -Wall
LDFLAGS=-lrpc -L../src
C_SOURCES=sampleClient.cpp
C_OBJECTS=$(C_SOURCES:.cpp=.o)
C_EXEC=client

S_SOURCES=sampleServer.cpp
S_OBJECTS=$(S_SOURCES:.cpp=.o)
S_EXEC=server

all: clean $(C_SOURCES) $(C_EXEC) $(S_SOURCES) $(S_EXEC)

$(C_EXEC): $(C_OBJECTS)
	$(CC) $(C_OBJECTS) $(LDFLAGS) -o $@

$(S_EXEC): $(S_OBJECTS)
	$(CC) $(S_OBJECTS) $(LDFLAGS) -o $@

.cpp.o:
	$(CC) $(CFLAGS) -c $< -o $@
	
clean:
	rm -rf *.o $(C_EXEC) $(S_EXEC)

