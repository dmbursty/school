#ifndef QUEUE_HPP
#define QUEUE_HPP

#include <queue>
#include <pthread.h>

// Thread safe queue of ints
class Queue {
 public:
  Queue();
  ~Queue();
  int pop();
  void push(int i);
  int size();

  void acquire();
  void release();

 private:
  std::queue<int> q;
  pthread_mutex_t mutex;
};

#endif
