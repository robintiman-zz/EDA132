import numpy as np
import math

class Perceptron:
    # Stochastic learning is used

    def __init__(self):
        # English = 0, French = 1
        pass
        # self.x_vector = np.array([1, x1, x2])
        # self.weights = np.array([w0, w1, w2])

    def update_weights(self):
        pass


    def logic_regression(self, w, k, y, x, learning_rate):
        h = 1 / (1 + math.e ** (-w * k))
        lossw = learning_rate*(y - h) * h(1 - h) * x
        w = w + lossw
        return w

def main():
    Perceptron()

if __name__=="__main__":
    main()