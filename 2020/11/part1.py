from itertools import product
import copy
from dataclasses import dataclass
from typing import List

OCCUPIED_SEAT = '#'
EMPTY_SEAT = 'L'

with open('input.txt') as f:
    input_ = f.read().strip()

@dataclass
class Seats:
    grid: List[List[str]]

    @property
    def width(self):
        return len(self.grid[0])

    @property
    def height(self):
        return len(self.grid)

seats = Seats([list(row) for row in input_.splitlines()])

def count_adjacent_occupied_seats(seats, x, y):
    adjacent_seats = [
        seats.grid[yi][xi]
        for xi in [x - 1, x, x + 1]
        for yi in [y - 1, y, y + 1]
        if (xi, yi) != (x, y) and 0 <= xi < seats.width and 0 <= yi < seats.height
    ]
    return len([seat for seat in adjacent_seats if seat == OCCUPIED_SEAT])

def count_occupied_seats(seats):
    return len([
        None
        for xi in range(seats.width)
        for yi in range(seats.height)
        if seats.grid[yi][xi] == OCCUPIED_SEAT
    ])

def execute_rules(seats):
    any_change = False
    new_seats = copy.deepcopy(seats)
    for x, y in product(range(seats.width), range(seats.height)):
        if seats.grid[y][x] == EMPTY_SEAT and count_adjacent_occupied_seats(seats, x, y) == 0:
            new_seats.grid[y][x] = OCCUPIED_SEAT
            any_change = True
        elif seats.grid[y][x] == OCCUPIED_SEAT and count_adjacent_occupied_seats(seats, x, y) >= 4:
            new_seats.grid[y][x] = EMPTY_SEAT
            any_change = True
    
    return new_seats, any_change

while True:
    seats, any_change = execute_rules(seats)
    if not any_change:
        break

print(count_occupied_seats(seats))
