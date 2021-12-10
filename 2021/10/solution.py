MATCHING_BRACKETS = {
    '(': ')',
    '[': ']',
    '{': '}',
    '<': '>',
}

ERROR_SCORES = {
    ')': 3,
    ']': 57,
    '}': 1197,
    '>': 25137,
}

AUTOCOMPLETION_SCORES = {
    ')': 1,
    ']': 2,
    '}': 3,
    '>': 4,
}


def process_line(line):
    opened_brackets = []
    for bracket in line:
        if bracket in MATCHING_BRACKETS.keys():
            opened_brackets.append(bracket)
        else:
            last_opened_bracket = opened_brackets.pop()
            if bracket != MATCHING_BRACKETS[last_opened_bracket]:
                invalid_bracket = bracket
                return invalid_bracket, None

    autocompleted_brackets = [MATCHING_BRACKETS[b]
                              for b in reversed(opened_brackets)]
    return None, autocompleted_brackets


def get_autocompletion_score(autocompleted_brackets):
    score = 0
    for bracket in autocompleted_brackets:
        score *= 5
        score += AUTOCOMPLETION_SCORES[bracket]
    return score


def solution():
    lines = open('input.txt').read().strip().splitlines()
    processed_lines = [process_line(line) for line in lines]

    error_scores = [ERROR_SCORES[invalid_bracket]
                    for invalid_bracket, _ in processed_lines
                    if invalid_bracket != None]

    part1 = sum(error_scores)

    autocompletion_scores = sorted(get_autocompletion_score(autocompleted_brackets)
                                   for _, autocompleted_brackets in processed_lines
                                   if autocompleted_brackets != None)

    part2 = autocompletion_scores[len(autocompletion_scores) // 2]

    return part1, part2


print(solution())
