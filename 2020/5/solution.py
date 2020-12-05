with open('input.txt') as f:
    seats = f.read().splitlines()

seat_ids = [
    int(seat.replace('F', '0').replace('B', '1').replace('L', '0').replace('R', '1'), 2)
    for seat in seats
]

print(max(seat_ids))

min_possible_id = 0
max_possible_id = 127 * 8 + 7

for id_ in range(min_possible_id, max_possible_id + 1):
    if id_ not in seat_ids and (id_ - 1) in seat_ids and (id_ + 1) in seat_ids:
        print(id_)
