from collections import Counter


with open('input.txt') as f:
    lines = f.read().splitlines()

jolts = [int(line) for line in lines]
jolts = sorted(jolts)
jolts = [0] + jolts + [max(jolts) + 3]

diffs = [b - a for a, b in zip(jolts[:-1], jolts[1:])]
diff_stats = Counter(diffs)

print(diff_stats[1] * diff_stats[3])


arrengment_count_prefixes = [0] * len(jolts)
arrengment_count_prefixes[0] = 1

for i in range(1, len(jolts)):
    for j in range(i - 3, i):
        if j >= 0 and jolts[i] - jolts[j] <= 3:
            arrengment_count_prefixes[i] += arrengment_count_prefixes[j]

print(arrengment_count_prefixes[-1])
