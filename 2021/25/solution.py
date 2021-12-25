from itertools import count


def read_input():
    lines = open('input.txt').read().strip().splitlines()
    size = len(lines[0]), len(lines)
    map_ = {(x, y): value
            for y, line in enumerate(lines)
            for x, value in enumerate(line)
            if value != '.'}
    return map_, size


def move_east(map_, size):
    return {
        ((x + 1) % size[0], y) if value == '>' and ((x + 1) % size[0], y) not in map_ else (x, y): value
        for (x, y), value in map_.items()
    }


def move_south(map_, size):
    return {
        (x, (y + 1) % size[1]) if value == 'v' and (x, (y + 1) % size[1]) not in map_ else (x, y): value
        for (x, y), value in map_.items()
    }


def part1():
    map_, size = read_input()

    for step in count(start=1):
        updated_map = move_east(map_, size)
        updated_map = move_south(updated_map, size)

        if updated_map == map_:
            return step

        map_ = updated_map


print(part1())
