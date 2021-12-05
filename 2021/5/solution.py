from typing import Counter
import re


def sign(x):
    if x < 0:
        return -1
    if x > 0:
        return 1
    else:
        return 0


def solution(include_diagonal):
    input_ = open('input.txt').read()
    lines = [[int(number) for number in re.split(',| -> ', line)]
             for line in input_.splitlines()]

    counter = Counter()

    for x1, y1, x2, y2 in lines:
        if include_diagonal or (x1 == x2 or y1 == y2):
            dx = sign(x2 - x1)
            dy = sign(y2 - y1)
            ix = x1
            iy = y1
            while True:
                counter[(ix, iy)] += 1
                if ix == x2 and iy == y2:
                    break
                ix += dx
                iy += dy

    return sum(1 for _, count in counter.items() if count >= 2)


print(solution(include_diagonal=False))
print(solution(include_diagonal=True))
