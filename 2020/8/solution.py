with open('input.txt') as f:
    lines = f.read().splitlines()

def parse_instruction(line):
    instr, arg = line.split(" ")
    return instr, int(arg)

instructions = list(map(parse_instruction, lines))

def execute_program(instructions):
    pc = 0
    acc = 0
    already_executed = set()

    while pc < len(instructions) and pc not in already_executed:
        already_executed.add(pc)
        instr, arg = instructions[pc]
        if instr == 'acc':
            acc += arg
            pc += 1
        elif instr == 'jmp':
            pc += arg
        elif instr == 'nop':
            pc += 1
    
    return pc, acc

_, acc = execute_program(instructions)
print(acc)

for idx, (instr, arg) in enumerate(instructions):
    if instr in ['jmp', 'nop']:
        fixed_instructions = instructions.copy()
        fixed_instructions[idx] = (
            {'jmp': 'nop', 'nop': 'jmp'}[instr],
            arg
        )
        pc, acc = execute_program(fixed_instructions)
        if pc == len(instructions):
            print(acc)
