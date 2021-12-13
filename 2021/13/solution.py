def read_input():
    input_ = open('input.txt').read().strip()
    dots, folds = input_.split('\n\n')
    dots = [dot.split(',') for dot in dots.splitlines()]
    dots = {(int(x), int(y)) for x, y in dots}
    folds = [fold.removeprefix('fold along ').split('=')
             for fold in folds.splitlines()]
    folds = [(axis, int(coord)) for axis, coord in folds]
    return dots, folds


def transform_dot(fold, dot):
    fold_axis, fold_coord = fold
    dot_x, dot_y = dot

    if fold_axis == 'x':
        if dot_x < fold_coord:
            return dot_x, dot_y
        else:
            return (fold_coord - (dot_x - fold_coord)), dot_y
    elif fold_axis == 'y':
        if dot_y < fold_coord:
            return dot_x, dot_y
        else:
            return dot_x, (fold_coord - (dot_y - fold_coord))


def visualize_dots(dots):
    xmax, ymax = max(dots)

    visualization = ''
    for y in range(ymax + 1):
        for x in range(xmax + 1):
            if (x, y) in dots:
                visualization += '#'
            else:
                visualization += ' '
        visualization += '\n'

    return visualization


def part1():
    dots, folds = read_input()
    dots = {transform_dot(folds[0], dot) for dot in dots}
    return len(dots)


def part2():
    dots, folds = read_input()
    for fold in folds:
        dots = {transform_dot(fold, dot) for dot in dots}
    return visualize_dots(dots)


print(part1())
print(part2())
