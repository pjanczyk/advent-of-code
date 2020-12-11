from itertools import product
import copy
from dataclasses import dataclass
from typing import List
from tqdm import tqdm

OCCUPIED_SEAT = '#'
EMPTY_SEAT = 'L'

with open('input.txt') as f:
    input_ = f.read().strip()

class Seats:
    def __init__(self, input_):
        matrix = [row for row in input_.splitlines()]
        self.width = len(matrix[0])
        self.height = len(matrix)
        self.seats = {
            (x, y): matrix[y][x]
            for (x, y) in product(range(self.width), range(self.height))
        }
        self.adjacent_seat_pos = {
            pos: self._find_adjacent_seats(pos)
            for pos in product(range(self.width), range(self.height))
        }

    def _find_adjacent_seats(self, pos):
        directions = set(product([-1, 0, 1], [-1, 0, 1])) - set([(0, 0)])
        adjacent_seats = [
            self._find_adjacent_seat(pos, direction) 
            for direction in directions
        ]
        return list(filter(bool, adjacent_seats))
    
    def _find_adjacent_seat(self, pos, direction):
        x, y = pos
        xdelta, ydelta = direction
        while True:
            x += xdelta
            y += ydelta
            if not (0 <= x < self.width and 0 <= y < self.height):
                return None
            if self.seats[(x, y)] in [OCCUPIED_SEAT, EMPTY_SEAT]:
                return (x, y)

    def count_occupied_seats(self):
        return len([
            None
            for pos in product(range(self.width), range(self.height))
            if self.seats[pos] == OCCUPIED_SEAT
        ])

    def count_adjacent_occupied_seats(self, pos):
        adjacent_seats = [self.seats[p] for p in self.adjacent_seat_pos[pos]]
        return len([seat for seat in adjacent_seats if seat == OCCUPIED_SEAT])

    def execute_rules(self):
        any_change = False
        new_seats = copy.deepcopy(self.seats)
        for pos in product(range(self.width), range(self.height)):
            if self.seats[pos] == EMPTY_SEAT and self.count_adjacent_occupied_seats(pos) == 0:
                new_seats[pos] = OCCUPIED_SEAT
                any_change = True
            elif self.seats[pos] == OCCUPIED_SEAT and self.count_adjacent_occupied_seats(pos) >= 5:
                new_seats[pos] = EMPTY_SEAT
                any_change = True
        self.seats = new_seats
        return any_change

seats = Seats(input_)

while seats.execute_rules():
    pass

print(seats.count_occupied_seats())
