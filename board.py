import numpy as np


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
        global BLACK, WHITE, VERTICAL, HORIZONTAL, DIAG_EAST, DIAG_WEST
        BLACK = 1
        WHITE = 0
        VERTICAL = 2
        HORIZONTAL = 3
        DIAG_EAST = 4
        DIAG_WEST = 5

        self.board = np.array([[-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1,  1, -1, -1, -1, -1, -1],
                               [-1, -1, -1,  0,  1, -1, -1, -1],
                               [-1, -1, -1,  0,  0, -1, -1, -1],
                               [-1, -1,  0, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1],
                               [-1, -1, -1, -1, -1, -1, -1, -1]])

    def place_tile(self, x, y, color):
        # Är nog enklare att bara göra detta i ett antal for-loopar.
        # En loop för varje riktning

        # Check row
        row = self.board[x, :]

        # Check column
        col = self.board[:, y]

        # Check diagonally in both directions
        offset1 = y - x
        offset2 = y - 7 + x
        diag1 = np.diagonal(self.board, offset1)
        diag2 = np.diagonal(self.board, offset2, axis1=1, axis2=0)

        # print(row, col, diag1, diag2)

        """
        Move is legal if the tiles are of the opposite color until the
        first tile of same color is reached
        """
        if offset1 > 0:
            line1, line2 = self.eval_line(diag1, x, BLACK)
        else:
            line1, line2 = self.eval_line(diag1, y, BLACK)
        # self.color_tile(y, line2, VERTICAL)
        print(line1, line2)
        self.color_tile(line1, DIAG_EAST, BLACK, x=0, offset=offset1)
        self.color_tile(line2, DIAG_EAST, BLACK, x=0, offset=offset1)

    def eval_line(self, arr, x, color):
        """
        :param arr: Array to evaluate taken from the board
        :param x: Position in the array
        :param color: The color of the player
        :return: True if the move is legal, False otherwise
        """
        size = arr.size
        legal = True
        if color == BLACK:
            other_col = WHITE
        else:
            other_col = BLACK

        # Check first half of the array up til x
        i = 0
        line1 = 0
        while i < x:
            if arr[i] == color:
                start = i
                while arr[i] == other_col:
                    i += 1
                if arr[i] == -1:
                    line1 = (start, x)
            i += 1

        # Other half after x
        i = x + 1
        line2 = 0
        while i < size:
            if arr[i] == other_col:
                while arr[i] == other_col:
                    i += 1
                if arr[i] == color:
                    line2 = (x, i)
            i += 1

        return line1, line2

    def color_tile(self, line, dir, color, x = 0, offset = 0):
        if line != 0:
            if dir == VERTICAL:
                self.board[line[0]:line[1], x] = color
            elif dir == HORIZONTAL:
                self.board[x, line[0]:line[1]] = color
            elif dir == DIAG_EAST:
                n = self.board[0]
                diag = np.zeros([n - np.abs(offset)], dtype=np.int8)
                diag[line[0]:line[1]] = color
                diag_matrix = np.diag(diag, offset)
                print(diag_matrix)
                self.board[range(n - offset), range(n - offset)] = diag
                self.print_board()


    def print_board(self):
        str_board = "  0 1 2 3 4 5 6 7\n"
        for x in range(self.board.shape[0]):
            str_board += str(x) + " "
            for y in range(self.board.shape[1]):
                numb = self.board[x, y]
                str_board += self.to_char(numb) + " "
            str_board += "\n"
        print(str_board)

    def to_char(self, numb):
        if numb == WHITE:
            return chr(9675)
        elif numb == BLACK:
            return chr(9679)
        else:
            return '·'


def main():
    game = Board()
    game.print_board()
    game.place_tile(5, 5, BLACK)
    game.print_board()


if __name__ == '__main__':
    main()
