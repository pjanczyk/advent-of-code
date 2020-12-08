import re
from collections import defaultdict


with open('input.txt') as f:
    lines = f.read().splitlines()

edges = defaultdict(list)
reverse_edges = defaultdict(list)

for line in lines:
    parent_bag, child_bags = line.strip(" .").split(" contain ")
    parent_bag = re.fullmatch(r"(.+) bags", parent_bag).group(1)
    if child_bags == "no other bags":
        child_bags = []
    else:
        child_bags = [(m := re.fullmatch(r"(\d+) (.+) bags?", bag)) and (int(m.group(1)), m.group(2))
                      for bag in child_bags.split(", ")]
    
    for count, child_bag in child_bags:
        edges[child_bag].append(parent_bag)
        reverse_edges[parent_bag].append((child_bag, count))

def dfs_collect(edges, v, visited):
    result = set()
    visited.add(v)
    for u in edges[v]:
        if u not in visited:
            result |= {u}
            result |= dfs_collect(edges, u, visited)
    return result

print(len(dfs_collect(edges, "shiny gold", set())))


def dfs_count_nested(edges, v):
    result = 0
    for u, count in edges[v]:
        result += count
        result += count * dfs_count_nested(edges, u)
    return result

print(dfs_count_nested(reverse_edges, "shiny gold"))
