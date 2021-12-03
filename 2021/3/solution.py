from typing import Counter


def part1():
    numbers = open('input.txt').read().splitlines()
    n_bits = len(numbers[0])

    counters = [Counter() for _ in range(n_bits)]

    for number in numbers:
        for i, bit in enumerate(number):
            counters[i][bit] += 1

    gamma_rate = ''.join(counter.most_common()[0][0]
                         for counter in counters)
    epsilon_rate = ''.join(counter.most_common()[-1][0]
                           for counter in counters)

    return int(gamma_rate, 2) * int(epsilon_rate, 2)


def part2():
    def compute_rating(numbers, n_bits, type):
        for i in range(n_bits):
            counter = Counter()

            for number in numbers:
                bit = number[i]
                counter[bit] += 1

            if type == 'oxygen':
                if counter['0'] != counter['1']:
                    bit_criteria = counter.most_common()[0][0]
                else:
                    bit_criteria = '1'
            elif type == 'co2':
                if counter['0'] != counter['1']:
                    bit_criteria = counter.most_common()[-1][0]
                else:
                    bit_criteria = '0'

            numbers = [number for number in numbers
                       if number[i] == bit_criteria]

            if len(numbers) == 1:
                return int(numbers[0], 2)

    numbers = open('input.txt').read().splitlines()
    n_bits = len(numbers[0])

    oxygen_rating = compute_rating(numbers, n_bits, 'oxygen')
    co2_rating = compute_rating(numbers, n_bits, 'co2')

    return oxygen_rating * co2_rating


print(part1())
print(part2())
