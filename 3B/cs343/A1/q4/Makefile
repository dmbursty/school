FLAGS=-g -O

all: q4.cpp
	g++ $^ -o a1 $(FLAGS)
	g++ $^ -o a2 -fno-inline $(FLAGS)

clean:
	rm -f a1 a2
