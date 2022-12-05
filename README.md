# Advent of Code 2022 in Clojure

Clojure solutions for [Advent of Code 2022](https://adventofcode.com/2022/). **Careful, may contain spoilers!**

Includes a tiny automated testing tool using macros. Solutions (called `dayX.clj`) provide two functions `part1 [str]` and `part2 [str]` that take the input string and return the answer, both of which are supplied in the `inputs` folder (after initial solve). Once all entries are complete, the tests for the whole 24 days can be generated with `(generate-daytests 24)`.

