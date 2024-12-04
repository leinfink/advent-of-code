#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#define FILENAME "input.txt"
#define LENGTH 140
#define DIR_LEN 8

FILE *fp;
char grid[LENGTH][LENGTH + 2]; /* +2 because of \n and \0 */

typedef struct Vec {
  int x;
  int y;
} Vec;

int count_mas(Vec);
bool find_xmas(Vec);
bool find_diag(Vec, Vec);
bool find(char *, int, Vec, Vec);

int main(void) {
  int i = 0;
  fp = fopen(FILENAME, "r");
  while (fgets(grid[i], LENGTH + 2, fp) != NULL) {
    ++i;
  }
  fclose(fp);

  if (i != LENGTH) {
    return EXIT_FAILURE;
  }

  /* part 1 */
  Vec point;
  int count = 0;
  for (int i = 0; i < LENGTH; ++i) {
    for (int j = 0; j < LENGTH; ++j) {
      point.x = i;
      point.y = j;
      if (grid[point.y][point.x] == 'X') {
        count += count_mas(point);
      }
    }
  }

  printf("Result part 1: %d\n", count);

  /* part 2 */

  count = 0;
  for (int i = 0; i < LENGTH; ++i) {
    for (int j = 0; j < LENGTH; ++j) {
      point.x = i;
      point.y = j;
      if (grid[point.y][point.x] == 'A') {
        count += find_xmas(point);
      }
    }
  }

  printf("Result part 2: %d\n", count);

  return EXIT_SUCCESS;
}

int count_mas(Vec point) {
  Vec dir[8] = {{1, 0},  {1, 1},  {1, -1}, {0, 1},
                {0, -1}, {-1, 0}, {-1, 1}, {-1, -1}};
  char mas[4] = "MAS";
  int count = 0;
  for (int k = 0; k < 8; ++k) {
    count += find(mas, 4, point, dir[k]);
  }
  return count;
}

bool find_xmas(Vec point) {
  Vec diags[2] = {{1, 1}, {1, -1}};
  Vec p1 = {point.x - 1, point.y - 1};
  Vec p2 = {point.x - 1, point.y + 1};
  return find_diag(p1, diags[0]) && find_diag(p2, diags[1]);
}

bool find_diag(Vec p, Vec diag) {
  char c = grid[p.y][p.x];
  if (c == 'S') {
    if (!find("AM", 3, p, diag)) {
      return 0;
    }
  } else if (c == 'M') {
    if (!find("AS", 3, p, diag)) {
      return 0;
    }
  } else {
    return 0;
  }
  return 1;
}

bool find(char *mas, int maslen, Vec point, Vec dir) {
  for (int i = 1; i < maslen; ++i) {
    char c = grid[point.y + dir.y * i][point.x + dir.x * i];
    if (mas[i - 1] != c) {
      return 0;
    }
  }
  return 1;
}
