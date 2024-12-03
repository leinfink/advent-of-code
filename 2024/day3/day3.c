#include <regex.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FILENAME "input.txt"
#define MAXLINE 1024 * 10

int countString(const char *, const char *, int max);

int main(void) {
  FILE *fp;
  char line[MAXLINE];
  regex_t regex;
  int match_groups = 3;
  regmatch_t pmatch[match_groups];
  int operands[match_groups - 1];
  unsigned int result = 0;
  int pos = 0;

  if (regcomp(&regex, "mul\\(([1-9][0-9]*),([1-9][0-9]*)\\)", REG_EXTENDED)) {
    return EXIT_FAILURE;
  }

  fp = fopen(FILENAME, "r");

  while (!feof(fp)) {
    if (fgets(line, sizeof(line), fp) != NULL) {
      pos = 0;
      while (regexec(&regex, line + pos, match_groups, pmatch, 0) == 0) {
        int matches = regex.re_nsub;
        for (int i = 1; i <= matches; ++i) {
          char *p = line + pos + pmatch[i].rm_so;
          operands[i - 1] = atoi(p);
        }
        int j = 1;
        for (int i = 0; i < match_groups - 1; ++i) {
          j *= operands[i];
        }
        result += j;
        pos += pmatch[matches].rm_eo;
      }
    }
  }
  regfree(&regex);
  fclose(fp);
  printf("Result part 1: %d\n", result);

  int active = 1;
  result = 0;

  if (regcomp(&regex, "mul\\(([1-9][0-9]*),([1-9][0-9]*)\\)", REG_EXTENDED)) {
    return EXIT_FAILURE;
  }

  fp = fopen(FILENAME, "r");

  while (!feof(fp)) {
    if (fgets(line, sizeof(line), fp) != NULL) {
      pos = 0;
      while (regexec(&regex, line + pos, match_groups, pmatch, 0) == 0) {
        char shortened[MAXLINE];
        strncpy(shortened, line, pos+pmatch[2].rm_so);
        shortened[pos+pmatch[2].rm_so] = '\0';
        char *tmp = shortened;
        char *last_do = tmp;
        int count = 0;
        while ((tmp = strstr(tmp, "do()")) != NULL) {
          tmp++;
          last_do = tmp;
          count++;
        }
        char *tmp2 = shortened;
        char *last_dont = tmp2;
        while ((tmp2 = strstr(tmp2, "don't()")) != NULL) {
          tmp2++;
          last_dont = tmp2;
          count++;
        }
        active = count == 0 ? active : (strlen(last_do) < strlen(last_dont));

        int matches = regex.re_nsub;
        for (int i = 1; i <= matches; ++i) {
          char *p = line + pos + pmatch[i].rm_so;
          operands[i - 1] = atoi(p);
        }
        int j = 1;
        for (int i = 0; i < match_groups - 1; ++i) {
          j *= operands[i];
        }
        if (active) {
          result += j;
        }
        pos += pmatch[matches].rm_eo;
      }
    }
  }
  regfree(&regex);
  fclose(fp);
  printf("Result part 2: %d\n", result);
}
int countString(const char *haystack, const char *needle, int max) {
  int count = 0;
  char shortened[MAXLINE];

  strncpy(shortened, haystack, max);
  char *tmp = shortened;
  while (tmp = strstr(tmp, needle)) {
    count++;
    tmp++;
  }
  return count;
}
