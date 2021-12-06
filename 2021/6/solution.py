from collections import Counter


def part1():
    input_ = open('input.txt').read().strip()
    state = [int(number) for number in input_.split(',')]

    for _ in range(80):
        for i in range(len(state)):
            if state[i] == 0:
                state[i] = 6
                state.append(8)
            else:
                state[i] -= 1

    return len(state)


def part2():
    input_ = open('input.txt').read().strip()
    state = [int(number) for number in input_.split(',')]
    state = Counter(state)

    for _ in range(256):
        state = Counter({(k - 1): v for k, v in state.items()})
        state[6] += state[-1]
        state[8] += state[-1]
        del state[-1]

    return state.total()


print(part1())
print(part2())
