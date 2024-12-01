#include <stdio.h>
#include <stdbool.h>

#define LINES 1000

void sort(int* list){
  bool sorted;
  int i, j;
  do {
    sorted = true;
    for (i = 0; i < LINES-1; ++i) {
      if (list[i] > list[i+1]) {
        sorted = false;
        j = list[i];
        list[i] = list[i+1];
        list[i+1] = j;
      }
    }
  } while (sorted == false);
  return;
}

int main () {
  FILE *fp;
  int list1[LINES];
  int list2[LINES];
  int i, j, k;
  int res_1 = 0;
  int res_2 = 0;

  fp = fopen("input.txt", "r");

  for (i = 0; i<LINES; ++i) {
    fscanf(fp, "%d %d\n", &list1[i], &list2[i]);
  }

  fclose(fp);

  sort(list1);
  sort(list2);

  for (i = 0; i<LINES; ++i) {
    j = list1[i] - list2[i];
    if (j < 0) {j = -j;}
    res_1 += j;
  }

  printf("%d\n", res_1);

  for (i = 0; i<LINES; ++i) {
    k = 0;
    for (j = 0; j<LINES; ++j) {
      if (list2[j] == list1[i]) {
        ++k;
      }
    }
    res_2 += k*list1[i];
  }

  printf("%d\n", res_2);
}
