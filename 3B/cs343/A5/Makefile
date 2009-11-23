-include ImplType

CXX = u++
CXXFLAGS = -g -Wall -Wno-unused-label -MMD -O2 -DTABLETYPE_${TYPE}
MAKEFLAGS = --no-print-directory
MAKEFILE_NAME = ${firstword ${MAKEFILE_LIST}}

OBJECTS = printer.o philosopher.o table${TYPE}.o driver.o
DEPENDS = ${OBJECTS:.o=.d}
EXEC = phil

ifeq (${IMPLTYPE},${TYPE})
${EXEC} : ${OBJECTS}
	${CXX} $^ -o $@
else
ifeq (${TYPE},)
TYPE=$(IMPLTYPE)
${EXEC} : ${OBJECTS}
	${CXX} $^ -o $@
else
.PHONY : ${EXEC}
${EXEC} :
	rm -f ImplType
	touch table.h
	${MAKE} TYPE=${TYPE}
endif
endif

ImplType :
	echo "IMPLTYPE=${TYPE}" > ImplType

${OBJECTS} : ${MAKEFILE_NAME}

%.class : %.java ${MAKEFILE_NAME}
	javac $<

${EXEC}Java : Printer.class Table.class Philosopher.class Driver.class

clean :
	rm -f *.d *.o ${EXEC} *.class ${EXEC}Java ImplType

-include ${DEPENDS}
