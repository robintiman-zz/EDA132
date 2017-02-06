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
        # Check row
        row = self.board[x, :]

        # Check column
        col = self.board[:, y]

        # Check diagonally in both directions
        offset1 = y - x # EAST
        offset2 = y - 7 + x # WEST
        diag1 = np.diagonal(self.board, offset1)
        diag2 = np.diagonal(np.fliplr(self.board), offset2)

        """
        Move is legal if the tiles are of the opposite color until the
        first tile of same color is reached
        """
        # Row and col first
        line1, line2 = self.eval_line(row, y, color)
        self.color_tile(line1, HORIZONTAL, color, y)
        self.color_tile(line2, HORIZONTAL, color, y)

        line1, line2 = self.eval_line(col, x, color)
        self.color_tile(line1, VERTICAL, color, x)
        self.color_tile(line2, VERTICAL, color, x)

        # Then diagonals
        if offset1 > 0:
            line1, line2 = self.eval_line(diag1, x, BLACK)
        else:
            line1, line2 = self.eval_line(diag1, y, BLACK)

        self.color_tile(line1, DIAG_EAST, BLACK, x=0, offset=offset1)
        self.color_tile(line2, DIAG_EAST, BLACK, x=0, offset=offset1)

        if offset2 > 0:
            line1, line2 = self.eval_line(diag2, y, BLACK)
        else:
            line1, line2 = self.eval_line(diag2, x, BLACK)

        self.color_tile(line1, DIAG_WEST, BLACK, x=0, offset=offset2)
        self.color_tile(line2, DIAG_WEST, BLACK, x=0, offset=offset2)


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

        # Check first half of the array up til x
        i = 0
        line1 = 0
        while i < x:
            if arr[i] == color:
                start = i
                i += 1
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
                i += 1
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

            else:
                if dir == DIAG_WEST:
                    # The flip function is O(1) so it's cool performance wise
                    tmp_board = np.fliplr(self.board)
                else:
                    tmp_board = self.board

                if offset > 0:
                    x_range = range(line[0], line[1] + 1)
                    y_range = range(line[0] + offset, line[1] + offset + 1)
                else:
                    x_range = range(line[0] - offset, line[1] + 1)
                    y_range = range(line[0], line[1] + 1)
                tmp_board[x_range, y_range] = 1

                if dir == DIAG_WEST:
                    self.board = np.fliplr(tmp_board)
                else:
                    self.board = tmp_board


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
    game.place_tile(6, 1, BLACK)
    game.print_board()


if __name__ == '__main__':
    main()
