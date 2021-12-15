from collections import Counter


def pairwise(lst):
    return zip(lst[:-1], lst[1:])


def read_input():
    input_ = open('input.txt').read().strip()
    template, rules = input_.split('\n\n')
    rules = [rule.split(' -> ') for rule in rules.splitlines()]
    rules = {tuple(pair): insertion for pair, insertion in rules}
    return template, rules


def solution(step_count):
    template, rules = read_input()

    elements = Counter(template)
    pairs = Counter(pairwise(template))

    for _ in range(step_count):
        new_pairs = Counter()

        for pair, cnt in pairs.items():
            if pair in rules:
                inserted_element = rules[pair]
                new_pairs[(pair[0], inserted_element)] += cnt
                new_pairs[(inserted_element, pair[1])] += cnt
                elements[inserted_element] += cnt
            else:
                new_pairs[pair] += cnt

        pairs = new_pairs

    most_common = elements.most_common()[0][1]
    least_common = elements.most_common()[-1][1]

    return most_common - least_common


print(solution(10))
print(solution(40))
