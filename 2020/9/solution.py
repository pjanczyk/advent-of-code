from itertools import combinations, accumulate
import operator

with open('input.txt') as f:
    lines = f.read().splitlines()

numbers = [int(line) for line in lines]

def contain_numbers_with_sum(list_, sum_):
    for a, b in combinations(list_, 2):
        if a + b == sum_:
            return True
    return False

def find_invalid_number(numbers):
    for i in range(25, len(numbers)):
        previous_numbers = numbers[i - 25:i]
        current_number = numbers[i]
        if not contain_numbers_with_sum(previous_numbers, current_number):
            return current_number

invalid_number = find_invalid_number(numbers)
print(invalid_number)

def find_slice_with_sum(numbers, sum_):
    prefix_sums = list(accumulate(numbers, operator.add))

    for i in range(len(numbers)):
        for j in range(i + 2, len(numbers)):
            sum_ = prefix_sums[j] - prefix_sums[i]
            if sum_ == invalid_number:
                return numbers[i:j]

slice_ = find_slice_with_sum(numbers, sum_=invalid_number)
print(min(slice_) + max(slice_))

