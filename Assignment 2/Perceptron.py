import numpy as np
import math


class Perceptron:
    # Stochastic learning is used

    def __init__(self, alpha):
        arrays = self.reader()
        self.total_en = arrays[0]
        self.total_a_en = arrays[1]
        self.total_fr = arrays[2]
        self.total_a_fr = arrays[3]
        self.alpha = alpha
        self.tol = tol

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
        self.total_fr = self.total_fr / divider
        self.total_a_fr = self.total_a_fr / divider

    def update_alpha(self, t):
        self.alpha = 1000 / (1000 + t)


    def reader(self):
        """
        Reads the input files according to the LIBSVM format.
        :return: Arrays with the parsed values
        """
        english_words = np.zeros(16, dtype=np.int)
        english_a = np.zeros(16, dtype=np.int)
        french_words = np.ones(16, dtype=np.int)
        french_a = np.ones(16, dtype=np.int)
        # Could take this in as an argument instead.
        files = ["english.txt", "french.txt"]
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
                    english_words[i] = int(temp.split(":", 1)[-1])
                    temp_a = a[i].strip()
                    english_a[i] = int(temp_a.split(":", 1)[-1])

            else:
                word = words.split(" ")
                a = nbr_of_a.split(" ")
                for i in range(1, len(word)):
                    temp = word[i].strip()
                    french_words[i] = int(temp.split(":", 1)[-1])
                    temp_a = a[i].strip()
                    french_a[i] = int(temp_a.split(":", 1)[-1])
        return english_words, english_a, french_words, french_a

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

    def logic_regression(self, w, k, y, x, alpha):
        h = 1 / (1 + math.e ** (-w * k))
        lossw = alpha * (y - h) * h(1 - h) * x
        w = w + lossw
        return w

def main():
    Perceptron(0.5, 3)

if __name__=="__main__":
    main()