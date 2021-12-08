import itertools


def part1():
    input_ = open('input.txt').read().strip()
    outputs = [line.split(' | ')[1].split() for line in input_.splitlines()]

    return sum(1 for output in outputs
               for digit in output
               if len(digit) in {2, 4, 3, 7})


def part2():
    DIGITS = [
        set('abcefg'),
        set('cf'),
        set('acdeg'),
        set('acdfg'),
        set('bcdf'),
        set('abdfg'),
        set('abdefg'),
        set('acf'),
        set('abcdefg'),
        set('abcdfg'),
    ]

    def generate_mappings():
        for permutation in itertools.permutations(list('abcdefg')):
            yield dict(zip(list('abcdefg'), permutation))

    def decode_digit(mapping, digit):
        decoded_segments = {mapping[s] for s in digit}
        if decoded_segments in DIGITS:
            return DIGITS.index(decoded_segments)
        else:
            return None

    def find_valid_mapping(digits):
        for mapping in generate_mappings():
            if all(decode_digit(mapping, digit) != None for digit in digits):
                return mapping
        raise Exception('No valid mapping exists')

    def decode_entry(entry):
        patterns, output = entry.split(' | ')
        patterns = [set(digit) for digit in patterns.split()]
        output = [set(digit) for digit in output.split()]

        mapping = find_valid_mapping(patterns)
        decoded_output = int(''.join(str(decode_digit(mapping, digit))
                                     for digit in output))
        return decoded_output

    entries = open('input.txt').read().strip().splitlines()
    return sum(decode_entry(entry) for entry in entries)


print(part1())
print(part2())
