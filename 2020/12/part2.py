from math import sin, cos

with open('input.txt') as f:
    lines = f.read().splitlines()

commands = [(line[0], int(line[1:])) for line in lines]

waypoint = (10, 1) # x, y
ship = (0, 0) # x, y

for (command, arg) in commands:
    if command in ['N', 'E', 'S', 'W']:
        waypoint_delta = {
            'N': (0, arg),
            'E': (arg, 0),
            'S': (0, -arg),
            'W': (-arg, 0),
        }[command]
        waypoint = (waypoint[0] + waypoint_delta[0], waypoint[1] + waypoint_delta[1])

    elif command in ['L', 'R']:
        angle_delta = {
            'L': arg,
            'R': -arg,
        }[command]
        waypoint = {
            0: waypoint,
            90: (-waypoint[1], waypoint[0]),
            180: (-waypoint[0], -waypoint[1]),
            270: (waypoint[1], -waypoint[0]),
        }[angle_delta % 360]

    elif command == 'F':
        ship = (ship[0] + arg * waypoint[0], ship[1] + arg * waypoint[1])

print(abs(ship[0]) + abs(ship[1]))
