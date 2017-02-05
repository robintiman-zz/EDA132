from ensurepip import __main__
import numpy as np


class Board:

    def __init__(self):
        """
        The board is an 8x8 matrix.
        0 - The tile is white
        1 - The tile is black
        -1 - The tile is undefined
        Notice that the black rings will appear to be white if you're using
        a dark theme, and vice versa.
        """
        global BLACK, WHITE
        BLACK = 1
        WHITE = 0
        self.board = np.array([[-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1,  0,  1, -1, -1, -1],
                               [-1, -1, -1,  1,  0, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1]])

    def place_tile(self, x, y, color):
        # Är nog enklare att bara göra detta i ett antal for-loopar.
        # En loop för varje riktning

        # Check row
        row = np.where(self.board[x, :] == color)

        # Check column
        col = np.where(self.board[:, y] == color)

        # Check diagonally in both directions
        offset1 = y - x
        offset2 = y - 7 + x
        diag1_values = np.diagonal(self.board, offset1)
        diag2_values = np.diagonal(self.board, offset2, axis1=1, axis2=0)
        diag1 = np.where(diag1_values == color)
        diag2 = np.where(diag2_values == color)

        print(row, col, diag1, diag2)

        """
        Move is legal if the tiles are of the opposite color until the
        first tile of same color is reached
        """
        if row:
            # x = 2, y = 3
            search_row = self.board[]
            value, count = np.unique(self[2:4, 3], return_counts=True)

        if col:
            pass

        if diag1:
            pass

        if diag2:
            pass

        self.board[x, y] = color

    def color_tile(self, x, y):
        # TODO implement support to flip multiple tiles at once
        curr = self.board[x, y]
        self.board[x, y] = BLACK if curr == WHITE else WHITE

    def print_board(self):
        str = ""
        for x in range(self.board.shape[0]):
            str += "|"
            for y in range(self.board.shape[1]):
                numb = self.board[x, y]
                str += self.to_char(numb) + "|"
            str += "\n"
        print(str)

    def to_char(self, numb):
        if numb == WHITE:
            return chr(9675)
        elif numb == BLACK:
            return chr(9679)
        else:
            return ' '


def main():
    game = Board()
    game.print_board()
    game.place_tile(2, 3, BLACK)
    game.print_board()

if __name__ == '__main__':
    main()

