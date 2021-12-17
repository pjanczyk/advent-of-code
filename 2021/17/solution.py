import re
from dataclasses import dataclass
from typing import Tuple
import sys


@dataclass
class Vec:
    x: int
    y: int


@dataclass
class Area:
    x_min: int
    x_max: int
    y_min: int
    y_max: int


def read_input() -> Area:
    input_ = open('input.txt').read().strip()
    x_min, x_max, y_min, y_max = re.compile('[-\d]+').findall(input_)
    return Area(int(x_min), int(x_max), int(y_min), int(y_max))


def sign(x):
    if x < 0:
        return -1
    if x > 0:
        return 1
    else:
        return 0


def simulate(initial_velocity: Vec, target: Area) -> Tuple[bool, int]:
    pos = Vec(0, 0)
    velocity = initial_velocity
    highest_y_pos = 0
    reached_target = False

    while True:
        if (target.x_min <= pos.x <= target.x_max and
                target.y_min <= pos.y <= target.y_max):
            reached_target = True

        if velocity.x <= 0 and pos.x < target.x_min:
            break
        if velocity.x >= 0 and pos.x > target.x_max:
            break
        if (velocity.y <= 0 and pos.y < target.y_min):
            break

        pos = Vec(x=pos.x + velocity.x,
                  y=pos.y + velocity.y)
        velocity = Vec(x=velocity.x - 1 if velocity.x > 0 else 0,
                       y=velocity.y - 1)
        highest_y_pos = max(highest_y_pos, pos.y)

    return reached_target, highest_y_pos


def solution() -> Tuple[int, int]:
    target = read_input()

    highest_y_pos = -sys.maxsize
    velocities_reaching_target = 0

    for x_velocity in range(1, 201):
        for y_velocity in range(-200, 201):
            reached_target, y_pos = simulate(
                Vec(x_velocity, y_velocity), target)
            if reached_target:
                highest_y_pos = max(highest_y_pos, y_pos)
                velocities_reaching_target += 1

    return highest_y_pos, velocities_reaching_target


print(solution())
