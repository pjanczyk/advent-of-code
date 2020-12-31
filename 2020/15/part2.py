from collections import defaultdict
from tqdm import tqdm

with open('input.txt') as f:
    input_ = f.read().strip()

numbers = [int(number) for number in input_.split(',')]

timestamp = 0
previous_number = None
number_timestamps = defaultdict(list)

for number in numbers:
    number_timestamps[number].append(timestamp)
    timestamp += 1
    previous_number = number

for _ in tqdm(range(30000000 - timestamp)):
    if len(number_timestamps[previous_number]) > 1:
        number = number_timestamps[previous_number][-1] - number_timestamps[previous_number][-2]
    else:
        number = 0
    
    number_timestamps[number].append(timestamp)
    timestamp += 1
    previous_number = number

print(timestamp, previous_number)
