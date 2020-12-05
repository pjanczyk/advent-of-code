from itertools import product

with open('input.txt') as input_file:
    lines = input_file.readlines()

numbers = list(map(int, lines))

for a, b in product(numbers, numbers):
    if a + b == 2020:
        print(a * b)
        break

for a, b, c in product(numbers, numbers, numbers):
    if a + b + c == 2020:
        print(a * b * c)
        break
