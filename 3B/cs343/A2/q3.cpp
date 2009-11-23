#include <uC++.h>
#include "q3.h"
#include <iostream>
#include <fstream>
#include <string>

using std::string;

#define SENTINEL 99999999

void merge::start(merge* partner, int sortValues[], int mergedValues[]) {
  this->partner = partner;
  this->list = sortValues;
  this->out = mergedValues;
}

void merge::mergeTo(int limit, int nextPos) {
  this->limit = limit;
  this->nextPos = nextPos;
  resume();
}

void merge::main() {
  // Loop until the end of the list
  while(*list != SENTINEL) {
    // Add all items up to limit
    while(*list <= limit && *list != SENTINEL) {
      out[nextPos] = *list;
      list++;
      nextPos++;
    }
    // Get partner to merge up until my next value
    partner->mergeTo(*list, this->nextPos);
  }
}

void uMain::main() {
  // Check arguments
  if (argc < 3) {
    std::cerr << "Two input files required" << std::endl;
    exit(1);
  }

  string s;
  // Grab the first list
  std::ifstream file;
  file.open(argv[1]);
  file >> s;
  int length_a = atoi(s.c_str());
  int a[length_a+1];
  for (int i = 0; i < length_a; i++) {
    file >> s;
    a[i] = atoi(s.c_str());
  }
  a[length_a] = SENTINEL;

  file.close();
  // Grab the second list
  file.open(argv[2]);
  file >> s;
  int length_b = atoi(s.c_str());
  int b[length_b+1];
  for (int i = 0; i < length_b; i++) {
    file >> s;
    b[i] = atoi(s.c_str());
  }
  b[length_b] = SENTINEL;
  file.close();

  // Make the result array
  int result[length_a + length_b];

  // Merge the lists
  merge m1;
  merge m2;

  m1.start(&m2, a, result);
  m2.start(&m1, b, result);

  if (a[0] < b[0]) {
    m1.mergeTo(b[0], 0);
  } else {
    m2.mergeTo(a[0], 0);
  }

  // Print the result list
  if (argc >= 4) {
    std::ofstream outfile;
    outfile.open(argv[3]);
    outfile << (length_a + length_b);
    for (int i = 0; i < length_a + length_b; i++) {
      outfile << " " << result[i];
    }
    outfile << std::endl;
  } else {
    std::cout << (length_a + length_b);
    for (int i = 0; i < length_a + length_b; i++) {
      std::cout << " " << result[i];
    }
    std::cout << std::endl;
  }
}
