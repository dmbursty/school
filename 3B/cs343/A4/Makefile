all: smokers

q1: q1.cc
	u++ -o q1_1 -DV1 q1.cc
	u++ -o q1_2 -DV2 q1.cc
	u++ -o q1_3 -DV3 q1.cc
	u++ -o q1_1o -O2 -DV1 q1.cc
	u++ -o q1_2o -O2 -DV2 q1.cc
	u++ -o q1_3o -O2 -DV3 q1.cc

q2: q2main.cc producer.o consumer.o
	u++ -o q2 q2main.cc producer.o consumer.o

smokers: q3main.cc q3table.h q3smoker.o q3printer.o
	u++ -o $@ q3main.cc q3smoker.o q3printer.o

%.o: %.cc %.h
	u++ -o $@ -c $<

clean:
	rm -f *.o
