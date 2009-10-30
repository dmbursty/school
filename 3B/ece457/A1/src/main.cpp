#include <fstream>
#include <iostream>
#include <string>
#include <vector>
using std::cerr;
using std::endl;
using std::ifstream;
using std::string;
using std::vector;

#include "grid.hpp"
#include "point.hpp"
#include "smart.hpp"

int main(int argc, char** argv) {
  string fileName;

  if (argc < 2) {
    cerr << "Input file required" << endl;
    return 1;
  } else {
    fileName = argv[1];
  }

  ifstream file;
  file.open(fileName.c_str());
  if (!file) {
    cerr << "Could not open file" << endl;
    return 1;
  }

  string line;
  char net_name[10];
  int x, y, xx, yy, n, num_nets, num_obstacles, net_id;

  // Grid size
  std::getline(file, line);
  sscanf(line.c_str(), "grid %d %d", &x, &y);
  Grid* grid = new Grid(x, y);

  // capacities
  std::getline(file, line);
  std::getline(file, line);

  // parameters
  std::getline(file, line);
  sscanf(line.c_str(), "num net %d", &num_nets);
  if (num_nets > 1) {
    cerr << "Cannot handle more than one net" << endl;
    return 1;
  }
  std::getline(file, line);
  sscanf(line.c_str(), "num obstacles %d", &num_obstacles);

  // The net
  std::getline(file, line);
  sscanf(line.c_str(), "net%s %d %d", net_name, &net_id, &n);
  for (int i = 0; i < n; i++) {
    std::getline(file, line);
    sscanf(line.c_str(), "  %d %d", &x, &y);
    grid->set_net(x, y);
  }

  // The obstacles
  for (int i = 0; i < num_obstacles; i++) {
    std::getline(file, line);
    sscanf(line.c_str(), "obstacle%d %d %d %d %d", &n, &x, &y, &xx, &yy);
    grid->set_obstacle(x, y, xx, yy);
  }

  // Optimize the net order to start from the middle of the graph

  Smart solver(grid);

  solver.solve();
  //grid->print();
  grid->print_path(net_name, net_id);
}
