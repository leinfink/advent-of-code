#include <regex.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FILENAME "input.txt"
#define MAXLINE 1024 * 10
#define MATCH_GROUPS 3

int solve(bool);

FILE *fp;
regex_t regex;
regmatch_t pmatch[MATCH_GROUPS];
int operands[MATCH_GROUPS - 1];

int main(void) {
  int result;

  if (regcomp(&regex, "mul\\(([1-9][0-9]*),([1-9][0-9]*)\\)", REG_EXTENDED)) {
    return EXIT_FAILURE;
  }

  fp = fopen(FILENAME, "r");

  result = solve(false);
  printf("Result part 1: %d\n", result);

  result = solve(true);
  printf("Result part 2: %d\n", result);

  fclose(fp);

  return EXIT_SUCCESS;
}

int solve(bool check_dos) {
  char line[MAXLINE];
  int pos;
  int result = 0;
  bool active = true;

  while (!feof(fp)) {
    if (fgets(line, sizeof(line), fp) != NULL) {
      pos = 0;
      while (regexec(&regex, line + pos, MATCH_GROUPS, pmatch, 0) == 0) {
        if (check_dos) {
          char shortened[MAXLINE];
          strncpy(shortened, line, pos + pmatch[2].rm_so);
          shortened[pos + pmatch[2].rm_so] = '\0';
          char *tmp = shortened;
          char *last_do = tmp;
          int count = 0;
          while ((tmp = strstr(tmp, "do()")) != NULL) {
            ++tmp;
            ++count;
            last_do = tmp;
          }
          char *tmp2 = shortened;
          char *last_dont = tmp2;
          while ((tmp2 = strstr(tmp2, "don't()")) != NULL) {
            ++tmp2;
            ++count;
            last_dont = tmp2;
          }
          active = count == 0 ? active : (strlen(last_do) < strlen(last_dont));
        }
        
        int matches = regex.re_nsub;
        
        if (active) {
          for (int i = 1; i <= matches; ++i) {
            char *p = line + pos + pmatch[i].rm_so;
            operands[i - 1] = atoi(p);
          }
          int j = 1;
          for (int i = 0; i < MATCH_GROUPS - 1; ++i) {
            j *= operands[i];
          }

          result += j;
        }
        pos += pmatch[matches].rm_eo;
      }
    }
  }
  rewind(fp);
  return result;
}
