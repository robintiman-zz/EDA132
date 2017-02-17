import numpy as np
import math
from builtins import print


class Perceptron:
    # Stochastic learning is used

    def __init__(self, alpha):
        # English = 0, French = 1
        self.total_en = np.array([35680, 42514, 15162, 35298, 29800, 40255, 74532, 37464, 31030, 24843, 36172,
                                  39552, 72545, 75352, 18031])
        self.total_a_en = np.array([2217, 2761, 990, 2274, 1865, 2606, 4805, 2396, 1993, 1627,
                                    2375, 2560, 4597, 4871, 1119])
        # self.reader()
        self.alpha = alpha
        # self.x_vector = np.array([1, x1, x2])
        # self.weights = np.array([w0, w1, w2])

    def update(self):
        # Use np.dot
        pass

    def scale_values(self):
        """
        :return: Scaled input array
        """
        divider = 100000
        self.total_en = self.total_en / divider
        self.total_a_en = self.total_a_en / divider

    def update_alpha(self, t):
        self.alpha = 1000 / (1000 + t)

        """
                reads a file and creates arrays with the information
                It not pretty though, not at all
                """

    def reader(self):
        english_words = []
        english_a = []
        french_words = []
        french_a = []
        file = open('french.txt')

        file.readline()
        words = file.readline()
        nbr_of_a = file.readline()
        if words.next() == 0:
            words.split(" ")

            while True:
                if words.split(" "):
                    temp = words.split(" ")
                    english_words.append(temp[2:])
                else:
                    break

            while True:
                if nbr_of_a.split(" "):
                    temp = nbr_of_a.split(" ")
                    english_a.append(temp[2:])
                else:
                    break
            return english_words, english_a
        else:
            words.split(" ")
            while True:
                if words.split(" "):
                    temp = words.split(" ")
                    french_words.append(temp[2:])
                else:
                    break

            while True:
                if nbr_of_a.split(" "):
                    temp = nbr_of_a.split(" ")
                    french_a.append(temp[2:])
                else:
                    break
            return french_words, french_a




        def predict(self, weights, x_vector):
            if weights * x_vector >= 0:
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
        Perceptron(0.5)

    if __name__=="__main__":
        main()