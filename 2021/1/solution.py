def part1():
    lines = open('input.txt').readlines()
    depths = [int(line) for line in lines]

    increases = 0
    for i in range(0, len(depths) - 1):
        if depths[i + 1] > depths[i]:
            increases += 1

    return increases


def part2():
    lines = open('input.txt').readlines()
    depths = [int(line) for line in lines]

    windows = [sum(depths[i:i + 3]) for i in range(0, len(depths) - 2)]

    increases = 0
    for i in range(0, len(windows) - 1):
        if windows[i + 1] > windows[i]:
            increases += 1

    return increases


print(part1())
print(part2())
