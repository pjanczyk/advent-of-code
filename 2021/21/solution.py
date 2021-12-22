from collections import Counter
from itertools import product


def read_input():
    input_ = open('input.txt').read().strip()
    return [int(line.split(': ')[1]) for line in input_.splitlines()]


class DeterministicDice:
    roll_count = 0

    def roll(self):
        result = (self.roll_count % 100) + 1
        self.roll_count += 1
        return result


def move_position(pos, move):
    return (pos + move - 1) % 10 + 1


def part1():
    dice = DeterministicDice()
    positions = read_input()
    scores = [0, 0]

    while True:
        for player in range(2):
            roll = sum(dice.roll() for _ in range(3))
            positions[player] = move_position(positions[player], roll)
            scores[player] += positions[player]
            if scores[player] >= 1000:
                return min(scores) * dice.roll_count


def part2():
    possible_rolls = Counter(sum(rolls)
                             for rolls in product([1, 2, 3], repeat=3))

    def count_single_player_outcomes(position, score=0, turn=0, multiplier=1) -> Counter:
        counter = Counter()
        for roll, repetitions in possible_rolls.items():
            new_position = move_position(position, roll)
            new_score = score + new_position
            new_turn = turn + 1
            new_multiplier = multiplier * repetitions

            if new_score >= 21:
                counter += Counter({('win', new_turn): new_multiplier})
            else:
                counter += Counter({('not_win', new_turn): new_multiplier})
                counter += count_single_player_outcomes(new_position, new_score, new_turn, new_multiplier)
        return counter

    starting_positions = read_input()

    player_1_outcomes = count_single_player_outcomes(starting_positions[0])
    player_2_outcomes = count_single_player_outcomes(starting_positions[1])

    player_1_won_universes = sum(
        player_2_outcomes[('not_win', turn-1)] * player_1_outcomes[('win', turn)]
        for turn in range(11)
    )
    player_2_won_universes = sum(
        player_1_outcomes[('not_win', turn)] * player_2_outcomes[('win', turn)]
        for turn in range(11)
    )

    return max(player_1_won_universes, player_2_won_universes)


print(part1())
print(part2())
