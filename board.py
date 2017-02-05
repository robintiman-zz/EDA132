from ensurepip import __main__
import numpy as np


class Board:
    # BLACK = 1
    # WHITE = 0
    # board = np.array

    def __init__(self):
        """
        The board is an 8x8 matrix.
        0 - The tile is white
        1 - The tile is black
        -1 - The tile is undefined
        """
        # global board
        self.board = np.array([[-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, 0, 1, -1, -1, -1],
                               [-1, -1, -1, 1, 0, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1]])

    def place_tile(self, x, y, color):
        self.board[x, y] = color

        pass

    def swap_tile(self, x, y):
        pass

    def print(self):
        str = ""
        for x in range(self.board.shape[0]):
            str += "|"
            for y in range(self.board.shape[1]):
                numb = self.board[x, y]
                str += self.to_char(numb) + "|"
            str += "\n"
        print(str)

    def to_char(self, numb):
        if numb == 0:
            # White
            return chr(9675)
        elif numb == 1:
            # Black
            return chr(9679)
        else:
            return ' '


class Tile:

    def __init__(self, x, y):
        self.x = x
        self.y = y


def main():
    game = Board()
    game.print()

if __name__ == '__main__':
    main()

