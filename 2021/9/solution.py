from functools import reduce
import operator


def solution():
    def get_adjacent_positions(pos):
        x, y = pos
        return [
            (x - 1, y),
            (x + 1, y),
            (x, y - 1),
            (x, y + 1)
        ]

    def is_low_point(heightmap, pos):
        return all(heightmap[pos] < heightmap[adjacent]
                   for adjacent in get_adjacent_positions(pos)
                   if adjacent in heightmap)

    def get_basin(heightmap, pos, visited=None):
        yield pos
        visited = visited or set()
        visited.add(pos)
        for adjacent in get_adjacent_positions(pos):
            if adjacent in heightmap and \
                    adjacent not in visited and \
                    heightmap[pos] < heightmap[adjacent] < 9:
                yield from get_basin(heightmap, adjacent, visited)

    def get_basin_size(heightmap, pos):
        return sum(1 for _ in get_basin(heightmap, pos))

    input_ = open('input.txt').read().strip()
    heightmap = {(x, y): int(height)
                 for y, row in enumerate(input_.splitlines())
                 for x, height in enumerate(row)}

    low_points = [pos for pos in heightmap.keys()
                  if is_low_point(heightmap, pos)]

    risk_levels = [heightmap[pos] + 1 for pos in low_points]

    part1 = sum(risk_levels)

    basin_sizes = [get_basin_size(heightmap, low_point)
                   for low_point in low_points]

    top_3_basins = sorted(basin_sizes, reverse=True)[:3]

    part2 = reduce(operator.mul, top_3_basins)

    return part1, part2


print(solution())
