from functools import reduce

with open('input.txt') as f:
    input_ = f.read()

groups = input_.split("\n\n")

unique_questions_per_group = [
    len(set(group.replace("\n", "")))
    for group in groups
]

print(sum(unique_questions_per_group))

def count_common_questions(group):
    lines = [set(line) for line in group.split("\n")]
    common = reduce(lambda a, b: a.intersection(b), lines)
    return len(common)

common_questions_per_group = list(map(count_common_questions, groups))

print(sum(common_questions_per_group))
