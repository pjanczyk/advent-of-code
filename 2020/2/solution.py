import re

with open('input.txt') as f:
    lines = f.readlines()

def parse_line(line):
    match = re.match(r"(\d+)-(\d+) ([a-z]): ([a-z]+)", line)
    return int(match[1]), int(match[2]), match[3], match[4]

passwords = list(map(parse_line, lines))

valid_passwords = 0
for (min_count, max_count, letter, password) in passwords:
    count = len([l for l in password if l == letter])
    if count >= min_count and count <= max_count:
        valid_passwords += 1

print(valid_passwords)


valid_passwords_2 = 0

for (pos1, pos2, letter, password) in passwords:
    indices = [pos1 - 1, pos2 - 1]
    letters = [password[index] for index in indices]
    if len([l for l in letters if l == letter]) == 1:
        valid_passwords_2 += 1

print(valid_passwords_2)