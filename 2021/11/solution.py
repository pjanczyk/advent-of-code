from dataclasses import dataclass
from itertools import product, count
from typing import Iterable


@dataclass(frozen=True)
class Pos:
    x: int
    y: int


@dataclass
class Octopus:
    energy: int
    flashed: bool = False


Grid = dict[Pos, Octopus]


def read_input() -> Grid:
    input_ = open('input.txt').read().strip()
    return {Pos(x, y): Octopus(energy=int(energy))
            for y, row in enumerate(input_.splitlines())
            for x, energy in enumerate(row)}


def get_neighbours(pos: Pos, grid: Grid) -> Iterable[Pos]:
    for dx, dy in product([-1, 0, 1], [-1, 0, 1]):
        neighbour = Pos(pos.x + dx, pos.y + dy)
        if neighbour != pos and neighbour in grid.keys():
            yield neighbour


def flash_if_necessary(pos: Pos, grid: Grid) -> None:
    if not grid[pos].flashed and grid[pos].energy > 9:
        grid[pos].flashed = True
        for neighbour in get_neighbours(pos, grid):
            grid[neighbour].energy += 1
            flash_if_necessary(neighbour, grid)


def execute_step(grid: Grid) -> int:
    for pos in grid.keys():
        grid[pos].energy += 1

    for pos in grid.keys():
        flash_if_necessary(pos, grid)

    flash_count = 0
    for pos in grid.keys():
        if grid[pos].flashed:
            grid[pos].flashed = False
            grid[pos].energy = 0
            flash_count += 1

    return flash_count


def part1():
    grid = read_input()
    flash_counts = [execute_step(grid) for _ in range(100)]
    return sum(flash_counts)


def part2():
    grid = read_input()
    for step in count(start=1):
        if execute_step(grid) == len(grid):
            return step


print(part1())
print(part2())
