#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#define FILENAME "input3.txt"
#define LINEMAXLEN 1024
#define DIGITSLEN 12

unsigned long sum_joltages(unsigned long (*) (char *));
unsigned long max_joltage_part1(char *);
unsigned long max_joltage_part2(char *);
int get_line_length(char *);

int main (void) {
  unsigned long part1 = sum_joltages(&max_joltage_part1);
  unsigned long part2 = sum_joltages(&max_joltage_part2);

  printf("Result of part 1: %lu\n", part1);
  printf("Result of part 2: %lu\n", part2);
  
  return EXIT_SUCCESS;
}

unsigned long sum_joltages(unsigned long (*max_joltage_fun)(char *)) {
  FILE *fp;
  char line[LINEMAXLEN];
  unsigned long total_joltage = 0;
  
  fp = fopen(FILENAME, "r");
  while (fgets(line, sizeof line, fp) != NULL) {
    total_joltage += (*max_joltage_fun)(line);
  }
  fclose(fp);
  
  return total_joltage;
}

unsigned long max_joltage_part1(char *line) {
  int first, second = 0;
  for (int i = 0; line[i] != '\0' && line[i] != '\n'; i++) {
    int cur = line[i] - '0';
    if (cur > first && line[i+1] != '\0' && line[i+1] != '\n') {
      first = cur;
      second = 0;
    } else if (cur > second) {
      second = cur;
    }
  }
  return first * 10 + second;
}

int get_line_length(char *line) {
  int i = 0;
  while (line[i] != '\0' && line[i] != '\n') {
    i++;
  }
  return i;
}

unsigned long max_joltage_part2(char *line) {
  int digits[DIGITSLEN] = {0};
  
  int line_len = get_line_length(line);

  for (int i = 0; i < line_len; i++) {
    int cur = line[i] - '0';
    for (int j = 0; j < DIGITSLEN; j++) {
      if (cur > digits[j] && line_len - i >= DIGITSLEN - j) {
        digits[j] = cur;
        for (int k = j+1; k < DIGITSLEN; k++) {
          digits[k] = 0;
        }
        break;
      }
    }
  }

  unsigned long sum = 0;
  for (int i = 0; i < DIGITSLEN; i++) {
    unsigned long pow10 = 1;
    for (int j = 1; j < DIGITSLEN - i; j++) {
      pow10 *= 10;
    }
    sum += digits[i] * pow10;
  }
  return sum;
}
