import numpy as np
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

    def reader(self):
        # Martin gör detta om du vill
        french = open('french.txt', 'r')
        classes = []
        for line in french:
            tmp = line.split(" ")
            classes[0] = int(tmp[0])

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



def main():
    Perceptron(0.5)

if __name__=="__main__":
    main()