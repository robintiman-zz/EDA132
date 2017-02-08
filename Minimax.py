
class Minimax:


    def __init__(self, game):

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
    game_eval = game;
    for a in all_inital_moves:
        result.append(max_value(game, a[0], a[1]))

    if len(result) > 0:
        return result
    else:
        return "pass"


def max_value(game,depth, x, y):
    game_eval.color_tile(game_eval, )
    if game.terminal(x, y):
        return game_eval.evaluate()
    v = float("-inf")
    for b in range(0, len(game_eval.find_all_moves(WHITE))):
        all_moves, corner_moves = game_eval.find_all_movesw(WHITE)
        move = all_moves[b]
        v = max(v, min_value(depth+1, move[0], move[1]))

    return v


def min_value(game, depth,x, y):

    if game.terminal() or depth > 4:
        return game_eval.evaluate()
    v = float("inf")
    for b in range(0, len(game_eval.find_all_moves(WHITE))):
        all_moves, corner_moves = game_eval.find_all_movesw(WHITE)
        move = all_moves[b]
        v = min(v, min_value(depth+1, move[0], move[1]))
    return v
