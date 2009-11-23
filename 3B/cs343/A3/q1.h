#include <uC++.h>
#include <queue>
#include <vector>

using std::vector;
using std::priority_queue;

#define uint unsigned int

_Coroutine Station {
private:
  struct Frame {
    enum {Token, Data, Ack} type;
    uint src;
    uint dst;
    uint prio;
  } frame ;  // Frame

  void data(Frame frame) {curr = frame; resume();}   // pass frame
  void main();                                       // coroutine main

public:
  Station(uint id) : id(id), now(-1), requests(), savedPriority(-1) {}
  void setup(Station* nexthop) {this->nexthop = nexthop;}  // supply next hop
  void sendreq(uint round, uint dst, uint prio);           // record send request
  void start();                                            // inject token and start

private:
  struct Request {
    uint round;
    uint dst;
    uint prio;
  };

  class RCompare {
   public:
    bool operator() (const Request& lhs, const Request& rhs) {
      return lhs.round > rhs.round;
    }
  };

  void print(Frame f);
  void send(Frame f);

  uint id;
  Station* nexthop;
  Frame curr;
  int now;
  priority_queue<Request, vector<Request>, RCompare> requests;
  int savedPriority;

  // Number of requests remaining
  static int outstandingRequests;
};  // Station
