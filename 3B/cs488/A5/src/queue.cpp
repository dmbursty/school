#include "queue.hpp"
#include <iostream>

Queue::Queue() {
  pthread_mutex_init(&mutex, NULL);
}

Queue::~Queue() {
  pthread_mutex_destroy(&mutex);
}

void Queue::acquire() {
  pthread_mutex_lock(&mutex);
}

void Queue::release() {
  pthread_mutex_unlock(&mutex);
}

int Queue::pop() {
  int ret = q.front();
  q.pop();
  return ret;
}

void Queue::push(int i) {
  q.push(i);
}

int Queue::size() {
  int ret = q.size();
  return ret;
}
