#include <stdbool.h>
#include <stdio.h>

#define LINES 1000

void sort(int *list) {
  bool sorted;
  int i, j;

  do {
    sorted = true;
    for (i = 0; i < LINES - 1; ++i) {
      if (list[i] > list[i + 1]) {
        sorted = false;
        j = list[i];
        list[i] = list[i + 1];
        list[i + 1] = j;
      }
    }
  } while (sorted == false);
}

int main(void) {
  FILE *fp;
  int list1[LINES];
  int list2[LINES];
  int i, j;
  int res = 0;

  fp = fopen("input.txt", "r");
  for (i = 0; i < LINES; ++i) {
    fscanf(fp, "%d %d\n", &list1[i], &list2[i]);
  }
  fclose(fp);

  /* part 1 */
  sort(list1);
  sort(list2);

  int dt = 0;
  for (i = 0; i < LINES; ++i) {
    dt = list1[i] - list2[i];
    if (dt < 0) {
      dt = -dt;
    }
    res += dt;
  }
  printf("%d\n", res);

  /* part 2 */

  res = 0;
  int count = 0;
  for (i = 0; i < LINES; ++i) {
    count = 0;
    for (j = 0; j < LINES; ++j) {
      if (list2[j] == list1[i]) {
        ++count;
      }
    }
    res += count * list1[i];
  }
  printf("%d\n", res);
  return 0;
}
