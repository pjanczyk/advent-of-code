with open('input.txt') as f:
    lines = f.read().splitlines()

commands = [(line[0], int(line[1:])) for line in lines]

position = (0, 0, 90) # x, y, angle

for (command, arg) in commands:
    if command in ['N', 'E', 'S', 'W']:
        delta = {
            'N': (0, arg),
            'E': (arg, 0),
            'S': (0, -arg),
            'W': (-arg, 0),
        }[command]
        position = (position[0] + delta[0], position[1] + delta[1], position[2])

    elif command in ['L', 'R']:
        delta = {
            'L': -arg,
            'R': arg,
        }[command]
        position = (position[0], position[1], (position[2] + delta) % 360)

    elif command == 'F':
        delta = {
            0: (0, arg),
            90: (arg, 0),
            180: (0, -arg),
            270: (-arg, 0),
        }[position[2]]
        position = (position[0] + delta[0], position[1] + delta[1], position[2])
    
print(abs(position[0]) + abs(position[1]))
