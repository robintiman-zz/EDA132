import random
import sys
import numpy as np

class Minimax:

    def __init__(self, game, level):
        """
        def alpha_beta_decision(self):
        If we have reached the final state
        if board.terminal(self):
        return board.final_value(self, state,)
        v  = float("-inf")
        """
        global BLACK, WHITE, LEGAL, VERTICAL, HORIZONTAL, DIAG_EAST, DIAG_WEST
        WHITE = 0
        BLACK = 1
        VERTICAL = 2
        HORIZONTAL = 3
        DIAG_EAST = 4
        DIAG_WEST = 5
        LEGAL = 6
        self.game = game
        self.level = level

    """
    Finds the value of the best move
    """
    def alpha_beta(self, board, node, depth, alpha, beta, player_is_max):
        """
        alpha is the best score for max along the path to state.
        beta is the best score for min along the path to state.
        """
        self.game.color_move(board, WHITE, node)
        legal_moves = self.game.find_all_moves(board, WHITE)

        if depth == 0 or len(legal_moves) == 0: # terminal state
            return node[2]

        if player_is_max:
            value = -float('inf')
            for move in legal_moves:
                value = max(value, self.alpha_beta(board, move, depth - 1, alpha, beta, False))
                alpha = max(alpha, value)
                if beta <= alpha:
                    break
            return value
        else:
            value = float('inf')
            for move in legal_moves:
                value = min(value, self.alpha_beta(board, move, depth - 1, alpha, beta, True))
                beta = min(beta, value)
                if beta <= alpha:
                    break
            return value

    """
    Finds and returns the best move
    """
    def decision(self):
        board = np.copy(self.game.get_board())
        all_moves = self.game.find_all_moves(board, WHITE)
        max_move = 0
        max_val = 0
        if len(all_moves) == 0:
            return max_move

        for node in all_moves:
            score = self.alpha_beta(board, node, self.level, -float('inf'), float('inf'), True)
            if max(score, max_val) == score:
                max_val = score
                max_move = node
        return max_move
