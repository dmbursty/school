#include <iostream>
using namespace std;
const int MAX = 5;

int main() {
  int num = 11;

  // Replace for loop initializations
  int i = 1;
  int j = 1;
  // Put conditionals into while
  while (i <= MAX) {
    while (j <= MAX) {
      cout << num << "  ";
      num += i;
      j += 1;     // increment j
    }
    j = 1;        // Reset j
    cout << endl;
    i += 1;       // increment i
  }
}
