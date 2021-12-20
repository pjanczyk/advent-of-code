from collections import Counter, defaultdict
from tqdm import tqdm


def read_input():
    input_ = open('input.txt').read().strip()
    mapping, image = input_.split("\n\n")
    image = {(x, y): pixel
             for y, row in enumerate(image.splitlines())
             for x, pixel in enumerate(row)}
    image = defaultdict(lambda: '.', image)
    return mapping, image


def pixels_to_int(pixels):
    return int(''.join(pixels).replace('#', '1').replace('.', '0'),
               base=2)


def enhance(image, mapping):
    new_image = {}
    min_x, min_y = min(image.keys())
    max_x, max_y = max(image.keys())
    for y in range(min_y - 1, max_y + 2):
        for x in range(min_x - 1, max_x + 2):
            neighbours = [image[(ix, iy)]
                          for iy in range(y - 1, y + 2)
                          for ix in range(x - 1, x + 2)]
            new_image[(x, y)] = mapping[pixels_to_int(neighbours)]

    default_pixel = image.default_factory()
    new_default_pixel = mapping[pixels_to_int(9 * default_pixel)]

    return defaultdict(lambda: new_default_pixel, new_image)


def solution(repetitions):
    mapping, image = read_input()

    for _ in tqdm(range(repetitions)):
        image = enhance(image, mapping)

    return Counter(image.values())['#']


print(solution(2))
print(solution(50))
