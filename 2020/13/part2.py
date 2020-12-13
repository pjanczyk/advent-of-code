with open('input.txt') as f:
    lines = f.read().splitlines()

buses = [(index, int(id_)) for index, id_ in enumerate(lines[1].split(',')) if id_ != 'x']

# 0  1      4    6  7
# 7,13,x,x,59,x,31,19

# t === 0 (mod 7)
# t === -1 (mod 13)
# t === -4 (mod 59)
# t === -6 (mod 31)
# t === -7 (mod 19)

def solve_congruences(rest1, modulus1, rest2, modulus2):
    """
    Finds smallest `x` that satisfies congruences:
    `x === rest1 (mod modulus1)`
    `x === rest2 (mod modulus2)`
    """
    print(f"x === {rest1} (mod {modulus1})  x === {rest2} (mod {modulus2})")

    rest1 %= modulus1
    rest2 %= modulus2

    i = 0
    while (rest1 + i * modulus1) % modulus2 != rest2:
        i += 1

    print("=>", rest1 + i * modulus1)
    return rest1 + i * modulus1

x, modulus = buses[0]
for bus_index, bus_id in buses[1:]:
    x = solve_congruences(rest1=x, modulus1=modulus, rest2=-bus_index, modulus2=bus_id)
    modulus *= bus_id

print(x)
