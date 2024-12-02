#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FILENAME "input.txt"

int check_record(char *, int *);

int main(void) {
  FILE *fp;
  fp = fopen(FILENAME, "r");
  char line[1024];
  char *num;
  int last, cur, is_safe, dt;
  int dir, cur_dir; /* 0: no direction yet, 1: increasing, -1: decreasing */
  int safe_records = 0;

  /* part 1 */

  while (fgets(line, sizeof line, fp) != NULL) {
    last = atoi(strtok(line, " "));
    num = strtok(NULL, " ");
    is_safe = 1, dir = 0;
    while (num != NULL) {
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
    safe_records += is_safe;
  }

  fclose(fp);

  printf("Result part 1: %d\n", safe_records);

  /* part 2 */

  char linecp[1024];
  char newrec[1024];
  char newreccp[1024];
  int numlen, slen, safe, k, m, len;
  safe_records = 0;

  fp = fopen(FILENAME, "r");

  while (fgets(line, sizeof line, fp) != NULL) {
    strcpy(linecp, line);
    slen = strlen(linecp);
    safe = check_record(line, &numlen);
    if (!safe) {
      for (int i = 0; i < numlen; ++i) {
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
      newrec[slen-m] = '\0';
      strcpy(newreccp, newrec);
      safe = check_record(newrec, &len);
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

int check_record(char *record, int *len) {
  char *num;
  int last, cur, dt, is_safe, dir, cur_dir;
  /* printf("--- checking %s\n", record); */
  last = atoi(strtok(record, " "));
  num = strtok(NULL, " ");
  *len = 1;
  is_safe = 1, dir = 0;
  while (num != NULL) {
    ++*len;
    cur = atoi(num);
    dt = abs(cur - last);
    /* printf("abs of %d\n", dt); */
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
  /* printf("TRUTH: %d OHA\n", is_safe); */

  return is_safe;
}
