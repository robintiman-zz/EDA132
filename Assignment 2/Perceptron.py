import numpy as np
import math
import matplotlib.pyplot as plt


class Perceptron:
    # Stochastic learning is used

    def __init__(self, mode, alpha, tol):
        arrays = self.reader()
        self.alpha = alpha
        self.tol = tol
        self.mode = mode

        weights = np.random.rand(3)
        weights = self.update(arrays, weights, alpha)
        print(weights)

    def update(self, arrays, weights, alpha):
        t = 0
        index = 0
        misclassified = 0
        length = arrays[0].shape[0] - 1
        while True:
            # Pick a random sample. 1 to 16 because the first value is the class
            i = np.random.randint(1, 16)
            # Also random class
            y = np.random.randint(0, 2)
            if y == 0:
                x_vector = np.array([1, arrays[1][i], arrays[0][i]])
            else:
                x_vector = np.array([1, arrays[3][i], arrays[2][i]])

            x = x_vector[index]
            if self.mode==2:
                y_hat = self.logistic(weights, x_vector)
                classification = (y - y_hat)*y_hat*(1-y_hat)
            else:
                y_hat = self.threshold(weights, x_vector)
                classification = self.loss(y, y_hat)

            weights[index] = weights[index] + alpha * classification * x

            t += 1
            self.update_alpha(t)

            index += 1 if index < 2 else 0

            if self.mode == 2:
                if y_hat - y < 0.5:
                    misclassified += 1
            else:
                if classification != 0:
                    misclassified += 1

            if t % length == 0:
                if misclassified < self.tol:
                    break
                misclassified = 0
        return weights

    def scale_values(self, arr1 ,arr2, arr3, arr4):
        """
        Scales the input so they are in the range [0, 1]
        """
        divider = 100000
        return arr1/divider, arr2/divider, arr3/divider, arr4/divider

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
        files = ["data.txt"]
        for i in range(0, len(files)):
            current_file = open(files[i])
            lines = current_file.readlines()

        # First number is a label telling which sort of data the specific line is,
        # automatically ignores the explanatory row.

            for j in range(0, len(lines)):
                line = lines[j]

                if line[0] == '0':
                    word = line.split(" ")
                    for i in range(1, len(word)):
                        temp = word[i].strip()
                        english_words[i] = int(temp.split(":", 1)[-1])

                if line[0] == '1':
                    word = line.split(" ")
                    for i in range(1, len(word)):
                        temp = word[i].strip()
                        english_a[i] = int(temp.split(":", 1)[-1])

                if line[0] == '2':
                    word = line.split(" ")
                    for i in range(1, len(word)):
                        temp = word[i].strip()
                        french_words[i] = int(temp.split(":", 1)[-1])

                if line[0] == '3':
                    word = line.split(" ")
                    for i in range(1, len(word)):
                        temp = word[i].strip()
                        french_a[i] = int(temp.split(":", 1)[-1])


        return self.scale_values(english_words, english_a, french_words, french_a)

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

    def logistic(self, w, x):
        return 1 / (1 + math.e ** -np.dot(w, x))

def main():
    mode = input("Chose perceptron (1) or logistic (2): ")
    Perceptron(int(mode), 0.5, 2)

if __name__=="__main__":
    main()