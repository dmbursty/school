CC=g++
CFLAGS=-g -O0 -Wall
LDFLAGS=
SOURCES=ClientStub.cpp ServerStub.cpp Common.cpp
OBJECTS=$(SOURCES:.cpp=.o)
B_SOURCES=Binder.cpp Common.cpp
B_OBJECTS=$(B_SOURCES:.cpp=.o)
LIB=librpc.a
EXEC=binder

all: $(SOURCES) $(B_SOURCES) $(LIB) $(EXEC)

$(EXEC): $(B_OBJECTS)
	$(CC) $(B_OBJECTS) $(LDFLAGS) -o $@

$(LIB): $(OBJECTS)
	ar rs $(LIB) $(OBJECTS)

.cpp.o:
	$(CC) $(CFLAGS) -c $< -o $@
	
clean:
	rm -rf *.o *.a $(LIB) $(EXEC)

