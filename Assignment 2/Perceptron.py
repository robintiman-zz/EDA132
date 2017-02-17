import numpy as np
import math


class Perceptron:
    # Stochastic learning is used

    def __init__(self, alpha, tol):
        # English = 0, French = 1
        self.total_en = np.array([35680, 42514, 15162, 35298, 29800, 40255, 74532, 37464, 31030, 24843, 36172,
                             39552, 72545, 75352, 18031])
        self.total_a_en = np.array([2217, 2761, 990, 2274, 1865, 2606, 4805, 2396, 1993, 1627,
                               2375, 2560, 4597, 4871, 1119])
        # self.reader()
        self.alpha = alpha
        self.y_values = [0, 1]
        self.tol = tol
        # self.x_vector = np.array([1, x1, x2])
        # self.weights = np.array([w0, w1, w2])

    def update(self, weights, alpha, y_vector, x_vector):
        t, i, misclassified = 0
        length = x_vector.shape[0]
        while True:
            # w i ← w i + α (y − h w (x)) × x i
            y_hat = self.threshold(weights, x_vector)
            y = y_vector[0]
            x = x_vector[i]
            classification = self.loss(y, y_hat)
            self.weights[i] = weights[i] + alpha * classification * x

            t += 1
            self.update_alpha(t)

            if classification != 0:
                misclassified += 1

            if i == length:
                i = 0
                if misclassified < self.tol:
                    break
                misclassified = 0
            else:
                i += 1

    def scale_values(self):
        """
        Scales the input so they are in the range [0, 1]
        """
        divider = 100000
        self.total_en = self.total_en / divider
        self.total_a_en = self.total_a_en / divider

    def update_alpha(self, t):
        self.alpha = 1000 / (1000 + t)

    def reader(self):
        french = open('french.txt', 'r')
        classes = []
        for line in french:
            tmp = line.split(" ")
            classes[0] = int(tmp[0])

    def threshold(self, weights, x_vector):
        if np.dot(weights, x_vector) >= 0:
            return 1
        else:
            return 0

    def loss(self, y, y_hat):
        """
        Loss function
        :param y: The real class
        :param y_hat: The predicted class
        :return: 0 if correct prediction, 1 or -1 otherwise
        """
        return y - y_hat

    def logic_regression(self, w, k, y, x, learning_rate):
        h = 1 / (1 + math.e ** (-w * k))
        lossw = learning_rate*(y - h) * h(1 - h) * x
        w = w + lossw
        return w

def main():
    Perceptron(0.5, 3)

if __name__=="__main__":
    main()