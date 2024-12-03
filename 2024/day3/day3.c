#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <regex.h>

#define FILENAME "input.txt"
#define MAXLINE 1024*10
int main(void) {
  FILE *fp;
  char line[MAXLINE];
  regex_t regex;
  int match_groups = 3;
  regmatch_t pmatch[match_groups];
  int operands[match_groups-1];
  unsigned int result = 0;
  int pos = 0;
  
  if (regcomp(&regex, "mul\\(([1-9][0-9]*),([1-9][0-9]*)\\)", REG_EXTENDED)) {
    return EXIT_FAILURE;
  }

  fp = fopen(FILENAME, "r");

  while (!feof(fp)) {
    if (fgets(line, sizeof(line), fp) != NULL) {
      pos = 0;
      printf("new line, cur result: %d\n", result);
      while(regexec(&regex, line + pos, match_groups, pmatch, 0) == 0){
        int matches = regex.re_nsub;
        for (int i = 1; i <= matches; ++i) {
          char *p = line + pos + pmatch[i].rm_so;
          operands[i-1] = atoi(p);
        }
        int j = 1;
        for (int i = 0; i < match_groups-1; ++i) {
          /* printf("Operand %d: %d\n", i, operands[i]); */
          j *= operands[i];
        }
        result += j;
        pos += pmatch[matches].rm_eo;
      }
    printf("last operand: %d\n", operands[1]);

    }
  }
    regfree(&regex);
    fclose(fp);
    printf("Result part 1: %d\n", result);
  }



