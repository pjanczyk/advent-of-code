import re

with open('input.txt') as f:
    lines = f.read().splitlines()

INSTR_SET_MASK = 'SET_MASK'
INSTR_SET_MEM = 'SET_MEM'

def parse_instr(line):
    if (m := re.fullmatch("mask = (\S+)", line)):
        mask = int(m.group(1).replace('0', '1').replace('X', '0'), 2)
        value = int(m.group(1).replace('X', '0'), 2)
        return INSTR_SET_MASK, mask, value
    elif (m := re.fullmatch("mem\[(\d+)\] = (\d+)", line)):
        addr = int(m.group(1))
        value = int(m.group(2))
        return INSTR_SET_MEM, addr, value

instructions = [parse_instr(line) for line in lines]

memory = dict()
mask = 0

for instr in instructions:
    if instr[0] == INSTR_SET_MASK:
        mask = (instr[1], instr[2])
    elif instr[0] == INSTR_SET_MEM:
        memory[instr[1]] = (instr[2] & ~mask[0]) | mask[1]

print(sum(memory.values()))