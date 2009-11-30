#ifndef _PRINTER_H_
#define _PRINTER_H_

#include <uC++.h>
#include <uSemaphore.h>
#include <string>
#include <iostream>

// This is a convenience class that synchronizes printing.
// The init method should be called at the very beginning, 
// because I'm too lazy to make this a thread safe singleton.
// Btw, this is a singleton because static methods apparently aren't
// synchronized in a monitor.
_Monitor Printer {
   public:
     // This method should be called first thing in uMain::main
     _Nomutex static void init() {
       _prt = new Printer();
     }

     // This method should be called first thing in uMain::main
     _Nomutex static void end() {
       delete _prt;
     }

     // returns an instance of this Printer class
     _Nomutex static Printer* inst() {
       return _prt;
     }

     // prints the string to standard out
     void print(std::string str) {
       std::cout << str;
     }

     // prints the string with a newline at the end to standard out
     void println(std::string str) {
       std::cout << str << std::endl;
     }

     // prints string to standard error prepended with "ERROR: " and with a
     // newline at the end
     void error(std::string str) {
       std::cerr << "ERROR: " << str << std::endl;
     }

  private:
     static Printer* _prt;
};

#endif

