#!/usr/bin/env python

import numpy as np
from time import sleep
import os
import Minimax


class Board:
    def __init__(self):
        """
        The board is an 8x8 matrix.
        0 - The tile is white
        1 - The tile is black
        -1 - The tile is undefined
        Notice that the black rings will appear to be white if you're using
        a dark theme and vice versa.
        """
        global BLACK, WHITE, LEGAL, VERTICAL, HORIZONTAL, DIAG_EAST, DIAG_WEST
        WHITE = 0
        BLACK = 1
        VERTICAL = 2
        HORIZONTAL = 3
        DIAG_EAST = 4
        DIAG_WEST = 5
        LEGAL = 6

        self.board = np.array([[-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1,  1,  0, -1, -1, -1],
                               [-1, -1, -1,  0,  1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1]])

    def get_dir_arrays(self, x, y):
        # Check row
        row = self.board[x, :]
        # Check column
        col = self.board[:, y]
        # Check diagonally in both directions
        offset_east = y - x  # EAST
        offset_west = 7 - y - x  # WEST
        diag_east = np.diagonal(self.board, offset_east)
        diag_west = np.diagonal(np.fliplr(self.board), offset_west, axis1=1, axis2=0)
        return (col, diag_east, diag_west, offset_east, offset_west, row)

    def eval_line(self, arr, x, color):
        """
        :param arr: Array to evaluate taken from the board
        :param x: Position in the array
        :param color: The color of the player
        :return: True if the move is legal, False otherwise
        """
        size = arr.size
        if color == BLACK:
            other_col = WHITE
        else:
            other_col = BLACK

        # These may look complicated but running time is O(n) for both loops together
        # Check first half of the array up til x
        i = 0
        line1 = 0
        line2 = 0

        legal = False
        if arr[x] == -1 or arr[x] == LEGAL:
            while i < x:
                if i + 1 >= size:
                    break
                if arr[i] == color:
                    start = i
                    i += 1
                    while arr[i] == other_col:
                        i += 1
                        legal = True
                    if i == x and legal:
                        line1 = (start, x)
                        break
                i += 1

            # Other half after x
            i = x + 1
            legal = False
            while i < size:
                if arr[i] != other_col or arr[size - 1] == other_col:
                    break
                else:
                    while arr[i] == other_col:
                        i += 1
                        legal = True
                    if arr[i] == color and legal:
                        line2 = (x, i)
                        break
                i += 1

        return line1, line2

    def color_tile(self, board, line, dir, color, x = 0, offset = None):
        if line != 0:

            if dir == VERTICAL:
                board[line[0]:line[1] + 1, x] = color

            elif dir == HORIZONTAL:
                board[x, line[0]:line[1] + 1] = color

            else:
                if dir == DIAG_WEST:
                    # The flip function is O(1) so it's cool performance wise
                    tmp_board = np.fliplr(board)
                else:
                    tmp_board = board

                if offset > 0:
                    x_range = range(line[0], line[1] + 1)
                    y_range = range(line[0] + offset, line[1] + offset + 1)
                else:
                    x_range = range(line[0] - offset, line[1] + 1)
                    y_range = range(line[0], line[1] + 1)
                tmp_board[x_range, y_range] = color

                if dir == DIAG_WEST:
                    board = np.fliplr(tmp_board)
                else:
                    board = tmp_board


    def print_board(self):
        str_board = "  0 1 2 3 4 5 6 7\n"
        for x in range(self.board.shape[0]):
            str_board += str(x) + " "
            for y in range(self.board.shape[1]):
                numb = self.board[x, y]
                str_board += self.to_char(numb) + " "
            str_board += "\n"
        return str_board

    def to_char(self, numb):
        if numb == WHITE:
            return chr(9675)
        elif numb == BLACK:
            return chr(9679)
        elif numb == LEGAL:
            return chr(9633)
        else:
            return '·'


    def find_all_moves(self, board, color):
        """
        Finds all legal moves.
        :param color: The color of the player
        :return: Array with all legal moves. First element is the position, second element is the line
                 it colors, and the third is the score.
        """
        all_moves = []
        corner_move = []
        lines = []
        for x in range(self.board.shape[0]):
            for y in range(self.board.shape[1]):
                col, diag_east, diag_west, offset_east, offset_west, row = self.get_dir_arrays(x, y)

                # Row and col first
                lines.append((self.eval_line(row, y, color), HORIZONTAL))

                lines.append((self.eval_line(col, x, color), VERTICAL))

                # Then diagonals
                if offset_east > 0:
                    lines.append((self.eval_line(diag_east, x, color), DIAG_EAST))
                else:
                    lines.append((self.eval_line(diag_east, y, color), DIAG_EAST))

                if offset_west > 0:
                    lines.append((self.eval_line(diag_west, x, color), DIAG_WEST))
                else:
                    lines.append((self.eval_line(diag_west, 7 - y, color), DIAG_WEST))


                found_legal = False
                for i in range(0, len(lines)):
                    el = lines[i]
                    line_tuple = el[0]
                    dir = el[1]
                    if dir == HORIZONTAL or dir == VERTICAL:
                        offset = None
                    elif dir == DIAG_EAST:
                        offset = offset_east
                    else:
                        offset = offset_west

                    for j in range(0, 2):
                        line = line_tuple[j]
                        if line != 0:
                            found_legal = True
                            score = line[1] - line[0]
                            self.board[x, y] = LEGAL
                            all_moves.append(((x, y), line, score, dir, offset))

                if not found_legal:
                    tile = self.board[x, y]
                    if tile == LEGAL:
                        self.board[x, y] = -1

                lines = []

                # Corner move
                if (x==0 or x==7) and (y==0 or y ==7):
                    corner_move.append((x, y))

        return all_moves, corner_move


    """Evaluate the score of a particular placement of tile. The score is based on the metric of mobility
    Mobility tells us the difference between the number of moves the player can perform and the number the
    bot can perform"""

    def evaluate(self):
        player = self.find_all_moves(self, BLACK)
        bot = self.find_all_moves(self, WHITE)
        return len(bot) - len(player)

    def terminal(self):
        if (len(self.find_all_moves(BLACK)) == 0 and len(self.find_all_moves(WHITE)) ==0):
            return True
        else:
            return False


def main():
    """
    To run: type "python3 board.py" in your terminal.
    It has to be a real terminal. os.system('clear') may not work in virtual ones.
    """
    game = Board()
    player1s_turn = True
    # minimax = Minimax(game)
    while True:
        os.system('clear')
        print("Hello and welcome to Martin and Robin's game of Reversi!\n"
              "To play, enter the coordinates of your move separated by a space.\n"
              "Possible moves are denoted with " + chr(9633) + ".\n"
              "To quit, enter \"quit\".\n")

        color = BLACK if player1s_turn else WHITE

        all_moves, corner_move = game.find_all_moves(color)
        print(game.print_board())

        if (len(all_moves) == 0):
            pos = input("No moves available, press Enter to pass: ")
            continue
        else:
            pos = input("Your move: ")
        legal_move = False
        try:
            x = int(pos[0])
            y = int(pos[2])
            for i in range(0, len(all_moves)):
                move = all_moves[i]
                if move[0][0] == x and move[0][1] == y:
                    legal_move = True
                    line = move[1]
                    dir = move[3]
                    offset = move[4]
                    if dir == HORIZONTAL:
                        pos_in_line = x
                    else:
                        pos_in_line = y
                    game.color_tile(game, line, dir, color, pos_in_line, offset)
                    break
        except:
            if pos == "quit":
                print("\nGame Over!\n")
                break
            else:
                print("\nInvalid input")
                sleep(1)
                continue

        if legal_move:
            player1s_turn = not player1s_turn
        # result = Minimax(game)
        # if(result != "pass"):
        #     game.place_title(result[0], result[1], WHITE)
        #
        # if(pos == "pass" and result == "pass"):
        #     print("\nNo more moves available for either player, Game over! Winner is" + game.winner())
        #     break

if __name__ == '__main__':
    main()
