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

bool find_mas(struct vec, struct vec);

int main(void) {
  fp = fopen(FILENAME, "r");

  int i;
  while (fgets(grid[i], LENGTH + 2, fp) != NULL) {
    ++i;
  }

  if (i != LENGTH) {
    return EXIT_FAILURE;
  }

  int count = 0;
  for (i = 0; i < LENGTH; ++i) {
    for (int j = 0; j < LENGTH; ++j) {
      point.x = i;
      point.y = j;
      if (grid[point.y][point.x] == 'X') {
        for (int k = 0; k < DIR_LEN; ++k) {
          count += find_mas(point, dir[k]);
        }
      }
    }
  }

  fclose(fp);
  printf("Result part 1: %d\n", count);
  return EXIT_SUCCESS;
}

bool find_mas(struct vec point, struct vec dir) {
  char mas[4] = "MAS";
  for (int i = 1; i < 4; ++i) {
    char c = grid[point.y + dir.y * i][point.x + dir.x * i];
    if (mas[i - 1] != c) {
      return 0;
    }
  }
  return 1;
}
