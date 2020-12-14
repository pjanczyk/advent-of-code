import itertools
import re
from itertools import product

with open('input.txt') as f:
    lines = f.read().splitlines()

INSTR_SET_MASK = 'SET_MASK'
INSTR_SET_MEM = 'SET_MEM'

def parse_instr(line):
    if (m := re.fullmatch("mask = (\S+)", line)):
        mask = m.group(1)
        return INSTR_SET_MASK, mask
    elif (m := re.fullmatch("mem\[(\d+)\] = (\d+)", line)):
        addr = int(m.group(1))
        value = int(m.group(2))
        return INSTR_SET_MEM, addr, value

instructions = [parse_instr(line) for line in lines]

def apply_mask(mask, addr):
    addr = format(addr, '#038b')[2:]
    addr = ['1' if m == '1' else a for a, m in zip(addr, mask)]

    x_indices = [i for i, c in enumerate(mask) if c == 'X']
    for x_bits in product(['0', '1'], repeat=len(x_indices)):
        for bit, index in zip(x_bits, x_indices):
            addr[index] = bit
        
        yield int(''.join(addr), 2)

memory = dict()
mask = None

for instr in instructions:
    if instr[0] == INSTR_SET_MASK:
        mask = instr[1]
    elif instr[0] == INSTR_SET_MEM:
        _, addr, value = instr
        addreses = list(apply_mask(mask, addr))
        for address in addreses:
            memory[address] = value

print(sum(memory.values()))