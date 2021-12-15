import sys
from queue import PriorityQueue
from collections import defaultdict


def read_input():
    input_ = open('input.txt').read().strip()
    return {(x, y): int(cost)
            for y, row in enumerate(input_.splitlines())
            for x, cost in enumerate(row)}


def get_neighbours(u, grid):
    uy, ux = u
    for v in (uy-1, ux), (uy+1, ux), (uy, ux-1), (uy, ux+1):
        if v in grid:
            yield v


def dikstra(grid):
    queue = PriorityQueue()
    dist = defaultdict(lambda: sys.maxsize)
    prev = defaultdict(lambda: None)

    dist[(0, 0)] = 0
    queue.put((0, (0, 0)))

    while not queue.empty():
        _, current = queue.get()
        for neighbour in get_neighbours(current, grid):
            new_cost = dist[current] + grid[neighbour]
            if new_cost < dist[neighbour]:
                dist[neighbour] = new_cost
                prev[neighbour] = current
                queue.put((new_cost, neighbour))

    return dist[max(dist.keys())]


def part1():
    grid = read_input()
    return dikstra(grid)


def part2():
    grid = read_input()
    size_x, size_y = max(grid.keys())[0] + 1, max(grid.keys())[1] + 1
    big_grid = {(y + size_y * iy, x + size_x * ix): ((cost + iy + ix - 1) % 9) + 1
                for (y, x), cost in grid.items()
                for ix in range(5)
                for iy in range(5)}

    return dikstra(big_grid)


print(part1())
print(part2())
