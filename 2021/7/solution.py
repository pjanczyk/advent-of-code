def solution(fuel_cost):
    input_ = open('input.txt').read().strip()
    positions = [int(number) for number in input_.split(',')]

    return min(
        sum(fuel_cost(abs(pos - align_pos)) for pos in positions)
        for align_pos in range(min(positions), max(positions) + 1)
    )

def linear_fuel_cost(distance):
    return distance

def increasing_fuel_cost(distance):
    return int((1 + distance) / 2 * distance)

print(solution(linear_fuel_cost))
print(solution(increasing_fuel_cost))
