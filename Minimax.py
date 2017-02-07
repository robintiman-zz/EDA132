
class Minimax:


    def __init__(self):

        """def alpha_beta_decision(self):
        If we have reached the final state
        if board.terminal(self):
        return board.final_value(self, state,)
        v  = float("-inf")"""

        global WHITE
        WHITE = 0


def minimax_decision(game):

    all_inital_moves = game.find_all_moves(WHITE)
    result = []

    for a in all_inital_moves:
        result.append(max_value(game, a[0], a[1]))

    if len(result) > 0:
        return result
    else:
        return "pass"


def max_value(game, x, y):

    if game.terminal(x, y):
        return game.score()
    v = float("-inf")

    for b in game.find_all_moves(WHITE):
        v = max(v, min_value(b[0], b[1]))

    return v


def min_value(game, x, y):

    if game.terminal():
        return game.score()
    v = float("inf")
    for c in game.find_all_moves(WHITE):
        v = min(v, min_value(c[0], c[1]))
    return v


