#ifndef __buffer_h
#define __buffer_h

#include <uC++.h>
#include <vector>
#include <iostream>

using std::vector;
using std::cout;
using std::endl;

// Class Definition
template<typename T> class BoundedBuffer {
 public:
  BoundedBuffer(const int size);
  void insert(T elem, const int id);
  T remove(const int id);

 private:
  const int size;
  vector<T> buffer;

  uOwnerLock mutex;
  uCondLock full, empty;
};


// Implementation
template<typename T>
BoundedBuffer<T>::BoundedBuffer(const int size) : size(size) {}

template<typename T>
void BoundedBuffer<T>::insert(T elem, const int id) {
  // Acquire the mutex for safety for the vector and cout
  mutex.acquire();
  // If the buffer is full, wait for the consumer's signal
  while (buffer.size() == size) {
    full.wait(mutex);
  }
  // Insert the element
  buffer.push_back(elem);
  // Print
  cout << " " << elem << "/" << id;
  // Release and signal
  mutex.release();
  empty.signal();
}

template<typename T>
T BoundedBuffer<T>::remove(const int id) {
  // Acquire the mutex for safety for the vector and cout
  mutex.acquire();
  // If the buffer is empty wait for the producer's signal
  while (buffer.size() == 0) {
    empty.wait(mutex);
  }
  // Remove the element
  T ret = buffer.front();
  buffer.erase(buffer.begin());
  // Print
  cout << " -> " << ret << "/" << id << endl;
  for (int i = 0; i < buffer.size(); i++) {
    cout << " " << buffer[i];
  }
  // Release and signal
  mutex.release();
  full.signal();
  return ret;
}

#endif
