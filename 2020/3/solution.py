from functools import reduce


def count_trees(terrain, slope):
    slope_x, slope_y = slope
    terrain_width = len(terrain[0])
    terrain_height = len(terrain)

    n_trees = 0
    position_x = 0
    position_y = 0

    while position_y < terrain_height:
        if terrain[position_y][position_x % terrain_width] == '#':
            n_trees += 1

        position_x += slope_x
        position_y += slope_y

    return n_trees


with open('input.txt') as f:
    terrain = f.read().splitlines()

print(count_trees(terrain, (3, 1)))

slopes = [
    (1, 1),
    (3, 1),
    (5, 1),
    (7, 1),
    (1, 2),
]

trees_by_slope = [count_trees(terrain, slope) for slope in slopes]
total_trees = reduce(lambda a, b: a * b, trees_by_slope, 1)

print(total_trees)
