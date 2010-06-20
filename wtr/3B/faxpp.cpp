#include <iostream>
#include <stdlib.h>

int main(int argc, char** argv) {
  FILE* file = fopen("doc-info.xml", "r");

  float version;
  int docs, terms, prox, partition, num, length;
  char name[30];
  char type[30];
  char stype[30];

  fscanf(file, "%f", &version);
  fscanf(file, "%d", &terms);
  fscanf(file, "%d", &docs);
  fscanf(file, "%d", &prox);
  fscanf(file, "%d", &partition);
  fscanf(file, "%d", &num);

  for (int i = 0; i < num; i++) {
    fscanf(file, "%s %s %i %s", name, type, &length, stype);
    printf("%s %s %i %s\n", name, type, length, stype);
  }

  fscanf(file, "%d", &num);
  for (int i = 0; i < num; i++) {
    fscanf(file, "%s %s", name, stype);
    printf("%s %s\n", name, stype);
  }

  fclose(file);
}
