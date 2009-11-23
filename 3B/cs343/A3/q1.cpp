#include <uC++.h>
#include <vector>
#include "q1.h"
#include <iostream>
#include <fstream>
#include <string>

using std::vector;
using std::string;
using std::cout;
using std::cerr;
using std::endl;

int Station::outstandingRequests = 0;

void uMain::main() {
  if (argc == 1 || argc > 3) {
    cerr << "Usage: ./q1 stations [<input file>]" << endl;
    exit(-1);
  }

  std::istream* in = &std::cin;
  if (argc == 3) {
    // Open file if specified
    std::ifstream* file = new std::ifstream();
    try {
      file->open(argv[2]);
    } catch (...) {
      cerr << "Error - cannot open: " << argv[2] << endl;
      cerr << "Usage: ./q1 stations [<input file>]" << endl;
      exit(-1);
    }
    in = file;
  }

  // Check number of stations
  int n = atoi(argv[1]);

  if (n < 1 || n > 100) {
    cerr << "Please specify 1-100 stations" << endl;
    exit(-1);
  }

  // Construct
  vector<Station*> stations;
  for (uint i = 0; i < n; i++) {
    stations.push_back(new Station(i));
  }

  // Setup
  for (uint i = 0; i < n-1; i++) {
    stations[i]->setup(stations[i+1]);
  }
  stations[n-1]->setup(stations[0]);

  // Setup Requests
  string line;
  while (getline(*in, line)) {
    if (line == "") {
      // Ignore blank line
    } else {
      // Formatting should be correct
      int round, src, dst, prio;
      sscanf(line.c_str(), "%d %d %d %d", &round, &src, &dst, &prio);
      if (round < 0 || src < 0 || dst < 0 || prio < 0 ||
          src >= n || dst >= n || src == dst) {
        // Invalid input, ignore
        continue;
      }
      stations[src]->sendreq(round, dst, prio);
    }
  }

  // Start
  stations[0]->start();

  // Delete
  for (uint i = 0; i < n; i++) {
    delete stations[i];
  }
}

void Station::main() {
  while(Station::outstandingRequests > 0 || id != 0) {
    // Increment round
    now++;

    // Handle frame
    if (curr.type == Frame::Token) {
      if (!requests.empty()
          && requests.top().round <= now
          && requests.top().prio >= curr.prio ) {
        // Send data
        Frame f;
        f.type = Frame::Data;
        f.src = id;
        f.dst = requests.top().dst;
        if (savedPriority == -1) {
          f.prio = curr.prio;
        } else {
          // Use saved priority
          f.prio = savedPriority;
          savedPriority = -1;
        }
        requests.pop();
        send(f);
        // Expect Ack, then pass token
        now++;
        Station::outstandingRequests--;
        curr.type = Frame::Token;
        send(curr);
      } else {
        // Pass along token
        send(curr);
      }
    } else {
      // Check if the frame is for me
      if (curr.dst == id) {
        // Return ack
        curr.dst = curr.src;
        curr.src = id;
        curr.type = Frame::Ack;
      }
      // Do priority switch and pass along
      if (!requests.empty()
          && requests.top().round <= now
          && requests.top().prio > curr.prio ) {
        savedPriority = curr.prio;
        curr.prio = requests.top().prio;
      }
      send(curr);
    }
  }
}

void Station::sendreq(uint round, uint dst, uint prio) {
  // Enqueue a request
  Request r;
  r.round = round;
  r.dst = dst;
  r.prio = prio;
  requests.push(r);
  Station::outstandingRequests++;
}

void Station::start() {
  Frame f;
  f.type = Frame::Token;
  f.prio = 0;
  data(f);
}

void Station::send(Frame f) {
  // Print frame, and send it to the next station
  print(f);
  nexthop->data(f);
}

void Station::print(Frame f) {
  // Print frame info
  cout << id << " ";
  switch(f.type) {
    case Frame::Token:
      cout << "token "; break;
    case Frame::Data:
      cout << f.src << " " << f.dst << " data "; break;
      case Frame::Ack:
      cout << f.src << " " << f.dst << " ack "; break;
  }
  cout << f.prio << " " << now << endl;
}
