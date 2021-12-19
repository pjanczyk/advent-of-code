from __future__ import annotations
from itertools import combinations
from dataclasses import dataclass
from typing import Callable
from collections import Counter
import numpy as np


@dataclass(frozen=True)
class Vec:
    x: int
    y: int
    z: int


def squared_distance(a: Vec, b: Vec) -> int:
    return ((a.x - b.x) ** 2 + (a.y - b.y) ** 2 + (a.z - b.z) ** 2)


def manhattan_distance(a: Vec, b: Vec) -> int:
    return abs(a.x - b.x) + abs(a.y - b.y) + abs(a.z - b.z)


@dataclass(eq=False)
class Beacon:
    pos: Vec
    distances: Counter = None  # distances to other beacons in this scanner


class Scanner:
    pos: Vec
    beacons: list[Beacon]
    distances: Counter  # distances between beacons
    transformed: bool

    def __init__(self, beacons: list[Beacon]) -> None:
        self.pos = Vec(0, 0, 0)
        self.beacons = beacons
        self.distances = Counter(squared_distance(b1.pos, b2.pos)
                                 for b1, b2 in combinations(beacons, 2))
        self.transformed = False
        for beacon in beacons:
            beacon.distances = Counter(squared_distance(b.pos, beacon.pos)
                                       for b in beacons
                                       if b is not beacon)

    def transform(self, transform: Transform) -> None:
        self.pos = transform(self.pos)
        for beacon in self.beacons:
            beacon.pos = transform(beacon.pos)
        self.transformed = True


def read_input() -> list[Scanner]:
    def parse_beacon(line: str) -> Beacon:
        x, y, z = line.split(',')
        pos = Vec(int(x), int(y), int(z))
        return Beacon(pos)

    def parse_scanner(scanner: str) -> Scanner:
        lines = scanner.splitlines()
        beacons = [parse_beacon(line) for line in lines[1:]]
        return Scanner(beacons)

    input_ = open('input.txt').read().strip()
    return [parse_scanner(scanner) for scanner in input_.split('\n\n')]


def have_common_beacons(s1: Scanner, s2: Scanner) -> bool:
    return (s1.distances & s2.distances).total() >= 66


def get_common_beacons(s1: Scanner, s2: Scanner) -> list[tuple[Beacon, Beacon]]:
    common_beacons = []
    for b1 in s1.beacons:
        for b2 in s2.beacons:
            if (b1.distances & b2.distances).total() >= 11:
                common_beacons.append((b1, b2))
    return common_beacons


TransformFn = Callable[[Vec], Vec]


def calculate_transform(points: list[tuple[Vec, Vec]]) -> TransformFn:
    A = np.zeros((4, 4), dtype=int)
    B = np.zeros((4, 4), dtype=int)

    for i in range(4):
        A[i, 0] = points[i][0].x
        A[i, 1] = points[i][0].y
        A[i, 2] = points[i][0].z
        A[i, 3] = 1

        B[i, 0] = points[i][1].x
        B[i, 1] = points[i][1].y
        B[i, 2] = points[i][1].z
        B[i, 3] = 1

    M = np.linalg.solve(A, B)

    def transform(point: Vec) -> Vec:
        a = np.array([point.x, point.y, point.z, 1])
        b = np.round(a.T @ M).astype(int)
        return Vec(b[0], b[1], b[2])

    for p0, p1 in points:
        assert transform(p0) == p1

    return transform


Transform = Callable[[Vec], Vec]


def get_transform_between(s1: Scanner, s2: Scanner) -> Transform:
    common_beacons = get_common_beacons(s1, s2)
    common_points = [(b1.pos, b2.pos) for b1, b2 in common_beacons]
    return calculate_transform(common_points)


def traverse_and_transform_scanners(scanners: list[Scanner]) -> None:
    def dfs(s: Scanner):
        for s2 in scanners:
            if not s2.transformed and have_common_beacons(s, s2):
                transform = get_transform_between(s2, s)
                s2.transform(transform)
                dfs(s2)

    dfs(scanners[0])


def solution():
    scanners = read_input()

    traverse_and_transform_scanners(scanners)

    beacon_count = len({beacon.pos
                        for scanner in scanners
                        for beacon in scanner.beacons})

    max_scanner_distance = max(manhattan_distance(s1.pos, s2.pos)
                               for s1, s2 in combinations(scanners, 2))

    return beacon_count, max_scanner_distance


print(solution())
