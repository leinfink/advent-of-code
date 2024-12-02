#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FILENAME "input.txt"
#define LINEMAX 10124

int check_record(char *, int *);

int main(void) {
  FILE *fp;
  char line[LINEMAX];
  int safe_records = 0;

  /* part 1 */

  fp = fopen(FILENAME, "r");

  while (fgets(line, sizeof line, fp) != NULL) {
    safe_records += check_record(line, NULL);
  }

  fclose(fp);

  printf("Result part 1: %d\n", safe_records);

  /* part 2 */

  char linecp[LINEMAX], newrec[LINEMAX];
  int numcount, slen, safe, k, m, len;

  safe_records = 0;

  fp = fopen(FILENAME, "r");

  while (fgets(line, sizeof line, fp) != NULL) {
    strcpy(linecp, line);
    slen = strlen(linecp);
    safe = check_record(line, &numcount);
    if (!safe) {
      for (int i = 0; i < numcount; ++i) {
        k = 0, m = 0;
        for (int j = 0; j < slen; ++j) {
          if (linecp[j] == ' ') {
            if (k != i) {
              newrec[j - m] = linecp[j];
            } else {
              ++m;
            }
            ++k;
            continue;
          }
          if (k != i) {
            newrec[j - m] = linecp[j];
          } else {
            ++m;
          }
        }
        newrec[slen - m] = '\0';
        safe = check_record(newrec, NULL);
        if (safe) {
          break;
        }
      }
    }
    safe_records += safe;
  }

  printf("Result part 2: %d\n", safe_records);

  fclose(fp);

  return EXIT_SUCCESS;
}

int check_record(char *record, int *numcount) {
  char *num;
  int last, cur, is_safe, dt;
  int dir, cur_dir; /* 0: no direction yet, 1: increasing, -1: decreasing */

  last = atoi(strtok(record, " "));
  num = strtok(NULL, " ");

  if (numcount != NULL) {
    *numcount = 1;
  }

  is_safe = 1;
  dir = 0;

  while (num != NULL) {
    if (numcount != NULL) {
      ++*numcount;
    }
    cur = atoi(num);
    dt = abs(cur - last);
    cur_dir = (cur > last) ? 1 : -1; /* if cur == last, then dt is 0 anyway */
    if (dt > 0 && dt <= 3 && (dir == 0 || dir * cur_dir == 1)) {
      dir = cur_dir;
    } else {
      is_safe = 0;
      break;
    }
    last = cur;
    num = strtok(NULL, " ");
  }

  return is_safe;
}
