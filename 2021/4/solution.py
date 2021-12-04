import itertools

BOARD_SIZE = 5


def parse_input():
    drawn_numbers, boards = open('input.txt').read().split('\n\n', 1)
    drawn_numbers = [int(number) for number in drawn_numbers.split(',')]
    boards = [[[int(number)
                for number in row.split()]
               for row in board.split('\n')]
              for board in boards.split('\n\n')]
    return drawn_numbers, boards


def remove_drawn_number_from_board(board, drawn_number):
    for i, j in itertools.product(range(BOARD_SIZE), range(BOARD_SIZE)):
        if board[i][j] == drawn_number:
            board[i][j] = None


def does_board_win(board):
    return any(all(board[i][j] == None for j in range(BOARD_SIZE)) or
               all(board[j][i] == None for j in range(BOARD_SIZE))
               for i in range(BOARD_SIZE))


def get_final_score(board, drawn_number):
    unmarked_numbers_sum = sum(board[i][j]
                               for i, j in itertools.product(range(BOARD_SIZE), range(BOARD_SIZE))
                               if board[i][j] != None)
    return unmarked_numbers_sum * drawn_number


def part_1_and_2():
    drawn_numbers, boards = parse_input()

    final_scores = []

    for drawn_number in drawn_numbers:
        for board in boards.copy():
            remove_drawn_number_from_board(board, drawn_number)

            if does_board_win(board):
                final_scores.append(get_final_score(board, drawn_number))
                boards.remove(board)

    return final_scores[0], final_scores[-1]


print(part_1_and_2())
