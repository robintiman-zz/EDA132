import numpy as np
import math
from builtins import print


class Perceptron:
    # Stochastic learning is used

    def __init__(self, alpha):
        arrays = self.reader()
        self.total_en = arrays[0]
        self.total_a_en = arrays[1]
        self.total_fr = arrays[2]
        self.total_a_fr = arrays[3]
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
        self.total_fr = self.total_fr / divider
        self.total_a_fr = self.total_a_fr / divider

    def update_alpha(self, t):
        self.alpha = 1000 / (1000 + t)


    def reader(self):
        english_words = []
        english_a = []
        french_words = []
        french_a = []
        input_files = input("Files to read, seperate different files with a whitespace: ")
        files = input_files.split(" ")
        for i in range(0, len(files)):
            current_file = open(files[i])
            first = current_file.readline()
            if (first[0] == '#'):
                words = current_file.readline()
            else:
                words = first
            nbr_of_a = current_file.readline()
            if words[0] == '0':
                word = words.split(" ")
                a = nbr_of_a.split(" ")
                for i in range(1, len(word)):
                    temp = word[i].strip()
                    english_words.append(temp.split(":", 1)[-1])
                    temp_a = a[i].strip()
                    english_a.append(temp_a.split(":", 1)[-1])

            else:
                word = words.split(" ")
                a = nbr_of_a.split(" ")
                for i in range(1, len(word)):
                    temp = word[i].strip()
                    french_words.append(temp.split(":", 1)[-1])
                    temp_a = a[i].strip()
                    french_a.append(temp_a.split(":", 1)[-1])
        return english_words, english_a, french_words, french_a

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



        def logic_regression(self, w, k, y, x, alpha):
            h = 1 / (1 + math.e ** (-w * k))
            lossw = alpha * (y - h) * h(1 - h) * x
            w = w + lossw
            return w

def main():
    Perceptron(0.5)

if __name__=="__main__":
    main()