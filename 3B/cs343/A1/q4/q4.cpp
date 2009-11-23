#include <cstdlib>          // atoi

class Stack {
 public:
  static const int MaxSize = 1000;
 private:
  unsigned int size;
  int data[MaxSize];
 public:
  Stack() : size(0) {}
  void push( int elem ) { data[size] = elem; size += 1; }
  int pop() { size -= 1; return data[size]; }
  int top() const { return data[size - 1]; }
};

int main( int argc, char *argv[] ) {
  unsigned int times = 100000;
  unsigned int nopop = 0;
  switch ( argc ) {
    case 3:
    nopop = atoi( argv[2] );
    case 2:
    times = atoi( argv[1] );
  } // switch

  int x = 0;
  for ( int i = 0; i < times; i += 1 ) {
    Stack s;
    for ( int j = 0; j < Stack::MaxSize; j += 1 ) s.push( j );
    for ( int j = nopop; j < Stack::MaxSize; j += 1 ) s.pop();
    if ( nopop > 0 ) x = s.top();
  }
  return x;
}
