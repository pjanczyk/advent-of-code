def part1():
    lines = open('input.txt').readlines()
    
    horizontal = 0
    depth = 0
    for line in lines:
        command, distance  = line.split(' ')
        distance = int(distance)
        if command == 'forward':
            horizontal += distance
        elif command == 'down':
            depth += distance
        elif command == 'up':
            depth -= distance
        else:
            raise
    
    return horizontal * depth


def part2():
    lines = open('input.txt').readlines()
    
    horizontal = 0
    aim = 0
    depth = 0
    for line in lines:
        command, distance  = line.split(' ')
        distance = int(distance)
        if command == 'forward':
            horizontal += distance
            depth += aim * distance
        elif command == 'down':
            aim += distance
        elif command == 'up':
            aim -= distance
        else:
            raise
    
    return horizontal * depth


print(part1())
print(part2())
