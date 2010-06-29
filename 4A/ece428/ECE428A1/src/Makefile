JC = javac
JCFLAGS = -g -source 1.6
J = java
JFLAGS = -cp .


SOURCES = \
	ece428/Player.java \
	ece428/PortPasser.java \
	ece428/FileIO.java \
	ece428/tcp/clientTCP.java \
	ece428/tcp/serverTCP.java \
	ece428/tcp/TCPSoccerClient.java \
	ece428/tcp/TCPSoccerServer.java \
	ece428/udp/clientUDP.java \
	ece428/udp/serverUDP.java \

CLASSES = $(patsubst %.java, %.class, $(SOURCES))


default: compile

compile:
	$(JC) $(JCFLAGS) $(SOURCES)

runTCP: compile
	$J $(JFLAGS) ece428/tcp/serverTCP &
	@sleep 1
	$J $(JFLAGS) ece428/tcp/clientTCP $F $C

runUDP: compile
	$J $(JFLAGS) ece428/udp/serverUDP &
	@sleep 1
	$J $(JFLAGS) ece428/udp/clientUDP $F $C

clean:
	rm -f $(CLASSES) outTCP.dat outUDP.dat
