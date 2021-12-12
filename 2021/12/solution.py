from collections import defaultdict, Counter


def read_input():
    input_ = open('input.txt').read().strip()
    edge_list = [edge.split('-') for edge in input_.splitlines()]

    adjacency_list = defaultdict(list)
    for u, v in edge_list:
        adjacency_list[u].append(v)
        adjacency_list[v].append(u)

    return adjacency_list


def part1():
    def dfs(adjacency_list, path):
        last = path[-1]
        for adjacent in adjacency_list[last]:
            if adjacent == 'end':
                yield path
            elif adjacent.isupper() or adjacent not in path:
                yield from dfs(adjacency_list, path + [adjacent])

    adjacency_list = read_input()

    return sum(1 for _ in dfs(adjacency_list, ['start']))


def part2():
    def dfs(adjacency_list, path, visited_twice=False):
        last = path[-1]
        for adjacent in adjacency_list[last]:
            if adjacent == 'start':
                pass
            elif adjacent == 'end':
                yield path
            elif adjacent.isupper() or adjacent not in path:
                yield from dfs(adjacency_list, path + [adjacent], visited_twice)
            elif not visited_twice:
                yield from dfs(adjacency_list, path + [adjacent], True)

    adjacency_list = read_input()

    return sum(1 for _ in dfs(adjacency_list, ['start']))


print(part1())
print(part2())
