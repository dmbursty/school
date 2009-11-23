#include <uC++.h>
#include <iostream>
#include <fstream>
#include <string>
#include "q2.h"

using std::string;

void StringLiteral::main() {
  // Parse string-literal
  if (ch == '"') {
    stat = CONT;
    suspend();
  } else {
    stat = ERROR;
    suspend();
  }

  // Parse s-char-sequence (optional)
  while(true) {
    // Parse s-char (or " to finish)
    if (ch == '"') {
      // Ending "
      stat = MATCH;
    } else if (ch == '\n') {
      // New-line is unexpected
      stat = ERROR;
    } else if (ch == '\\') {
      // Parse escape-sequence
      suspend();  // Wait for next char
      // Check for simple-escape-sequence
      if (ch == '\'' || ch == '"' || ch == '?' || ch == '\\' || ch == 'a' ||
          ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r' || ch == 't' || ch == 'v') {
        // Do nothing
      } else if (ch == 'x') {
        // Parse hexadecimal-escape-sequence
        suspend();
        if (!isxdigit(ch)) stat = ERROR;
        suspend();
        if (!isxdigit(ch)) continue;
      } else if (ch >= '0' && ch <= '7') {
        // Parse two more octal digits
        suspend();
        if (ch < '0' || ch > '7') continue;
        suspend();
        if (ch < '0' || ch > '7') continue;
      } else {
        stat = ERROR;
      }
    }
    suspend();
  }
}

void uMain::main() {
  std::istream* in = &std::cin;
  if (argc >= 2) {
    // Open file if specified
    std::ifstream* file = new std::ifstream();
    file->open(argv[1]);
    in = file;
  }
  string line;
  // Loop through each line in the input stream
  while (getline(*in, line)) {
    if (line == "") {
      // Blank line encountered
      std::cout << "'' : Warning! Blank line." << std::endl;
      continue;
    } else {
      std::cout << "'" << line << "' : '";
    }
    int status;
    // Create coroutine
    StringLiteral checker;
    int i;
    for (i = 0; i < line.length(); i++) {
      // Check each character until end of line or non-CONT status
      status = checker.next(line[i]);
      std::cout << line[i];
      if (status != StringLiteral::CONT) break;
    }
    i++;
    std::cout << "' ";

    if (status == StringLiteral::MATCH and i == line.length()) {
      // Got a match
      std::cout << "yes";
    } else {
      // Either reached end of line, or got an error, or had extra chars
      std::cout << "no";
    }

    // If there are extra characters at the end, print them
    if (i < line.length()) {
      std::cout << " -- extraneous characters '";
      for (; i < line.length(); i++) {
        std::cout << line[i];
      }
      std::cout << "'";
    }

    std::cout << std::endl;
  }  // while getline

  // Delete the input file ifstream
  if (argc >= 2) delete in;
}
