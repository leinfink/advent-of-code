#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#define FILENAME "input.txt"
#define LENGTH 140
#define DIR_LEN 8

FILE *fp;
char grid[LENGTH][LENGTH + 2]; /* +2 because of \n and \0 */
struct vec {
  int x;
  int y;
};
struct vec point;
struct vec dir[DIR_LEN] = {{1, 1},  {1, 0},  {-1, 0}, {-1, -1},
                           {-1, 1}, {1, -1}, {0, 1},  {0, -1}};

int count_mas(struct vec);
bool find_xmas(struct vec);
bool find(char*, struct vec, struct vec);

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

int count_mas(struct vec point) {
  char mas[4] = "MAS";
  int count = 0;
  for (int k = 0; k < DIR_LEN; ++k) {
    count += find(mas, point, dir[k]);
  }
  return count;
}

bool find_xmas(struct vec point) {
  char c;
  c = grid[point.y - 1][point.x - 1];
  if (c == 'S') {
    if (grid[point.y + 1][point.x + 1] != 'M') {
      return 0;
    }
  } else if (c == 'M') {
    if (grid[point.y + 1][point.x + 1] != 'S') {
      return 0;
    }
  } else {
    return 0;
  }
  c = grid[point.y + 1][point.x - 1];
  if (c == 'S') {
    if (grid[point.y - 1][point.x + 1] != 'M') {
      return 0;
    }
  } else if (c == 'M') {
    if (grid[point.y - 1][point.x + 1] != 'S') {
      return 0;
    }
  } else {
    return 0;
  }
  return 1;
}

bool find(char* mas, struct vec point, struct vec dir) {
  for (int i = 1; i < 4; ++i) {
    char c = grid[point.y + dir.y * i][point.x + dir.x * i];
    if (mas[i - 1] != c) {
      return 0;
    }
  }
  return 1;
}
