SOURCES=scanner.py

all:
	chmod +x joosc

scanner:
	python test.py -t html -o out.html -s scanner $(TEST)

archive:
	zip a1.zip Makefile joosc $(SOURCES)
