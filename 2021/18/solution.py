import re
from functools import reduce
from math import floor, ceil
from itertools import permutations


def parse_line(line):
    return [element if element in {'[', ']'} else int(element)
            for element in re.findall(r'\[|\]|\d+', line)]


def read_input():
    input_ = open('input.txt').read().strip()
    return [parse_line(line) for line in input_.splitlines()]


def explode_number(x):
    depth = 0
    for i in range(len(x)):
        if x[i] == '[':
            depth += 1
        elif x[i] == ']':
            depth -= 1

        if depth == 5:
            left = x[i + 1]
            right = x[i + 2]
            before = x[:i]
            after = x[i + 4:]

            for j in range(len(before) - 1, -1, -1):
                if isinstance(before[j], int):
                    before[j] += left
                    break

            for j in range(len(after)):
                if isinstance(after[j], int):
                    after[j] += right
                    break

            return True, [*before, 0, *after]

    return False, x


def split_number(x):
    for i in range(len(x)):
        if isinstance(x[i], int) and x[i] >= 10:
            before = x[:i]
            after = x[i + 1:]
            left = floor(x[i] / 2)
            right = ceil(x[i] / 2)
            return True, [*before, '[', left, right, ']', *after]
    return False, x


def reduce_number(x):
    while True:
        exploded, x = explode_number(x)
        if exploded:
            continue
        splitted, x = split_number(x)
        if splitted:
            continue
        break
    return x


def add_numbers(a, b):
    sum_ = ['[', *a, *b, ']']
    return reduce_number(sum_)


def calc_magnitude(x):
    def rec(x, i):
        if x[i] == '[':
            i += 1
            i, magnitude_left = rec(x, i)
            i, magnitude_right = rec(x, i)
            assert(x[i] == ']')
            i += 1
            magnitude = 3 * magnitude_left + 2 * magnitude_right
            return i, magnitude
        elif isinstance(x[i], int):
            magnitude = x[i]
            i += 1
            return i, magnitude

    _, magnitude = rec(x, 0)
    return magnitude


def part1():
    numbers = read_input()
    sum_ = reduce(add_numbers, numbers)
    return calc_magnitude(sum_)


def part2():
    numbers = read_input()
    sums_ = [add_numbers(a, b) for a, b in permutations(numbers, 2)]
    magnitudes = [calc_magnitude(x) for x in sums_]
    return max(magnitudes)


print(part1())
print(part2())
