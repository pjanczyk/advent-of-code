from dataclasses import dataclass
from itertools import product, pairwise
import re
import numpy as np


@dataclass
class Step:
    action: str
    xmin: int
    xmax: int
    ymin: int
    ymax: int
    zmin: int
    zmax: int


def read_input():
    input_ = open('input.txt').read().strip()
    steps = []
    for line in input_.splitlines():
        action = line.split(' ')[0]
        coords = [int(coord) for coord in re.compile('-?\d+').findall(line)]
        steps.append(Step(action, *coords))
    return steps


def part1():
    steps = read_input()
    cubes = set()

    for step in steps:
        for x, y, z in product(
            range(max(step.xmin, -50), min(step.xmax, 50) + 1),
            range(max(step.ymin, -50), min(step.ymax, 50) + 1),
            range(max(step.zmin, -50), min(step.zmax, 50) + 1)
        ):
            if step.action == 'on':
                cubes.add((x, y, z))
            elif step.action == 'off' and (x, y, z) in cubes:
                cubes.remove((x, y, z))

    return len(cubes)


def part2():
    steps = read_input()

    x_divisions = set()
    y_divisions = set()
    z_divisions = set()

    for step in steps:
        x_divisions.add(step.xmin)
        x_divisions.add(step.xmax + 1)
        y_divisions.add(step.ymin)
        y_divisions.add(step.ymax + 1)
        z_divisions.add(step.zmin)
        z_divisions.add(step.zmax + 1)

    x_divisions = sorted(x_divisions)
    y_divisions = sorted(y_divisions)
    z_divisions = sorted(z_divisions)

    shape = (len(x_divisions) - 1,
             len(y_divisions) - 1,
             len(z_divisions) - 1)

    cuboids = np.zeros(shape, bool)

    for step in steps:
        value = (step.action == 'on')
        cuboids[
            x_divisions.index(step.xmin):x_divisions.index(step.xmax + 1),
            y_divisions.index(step.ymin):y_divisions.index(step.ymax + 1),
            z_divisions.index(step.zmin):z_divisions.index(step.zmax + 1)
        ] = value

    x_weights = np.array([x2 - x1 for x1, x2 in pairwise(x_divisions)])
    y_weights = np.array([y2 - y1 for y1, y2 in pairwise(y_divisions)])
    z_weights = np.array([z2 - z1 for z1, z2 in pairwise(z_divisions)])

    return np.einsum('xyz,x,y,z->', cuboids, x_weights, y_weights, z_weights)


print(part2())
