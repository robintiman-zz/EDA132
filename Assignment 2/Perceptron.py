import numpy as np
import math
import matplotlib.pyplot as plt


class Perceptron:
    # Stochastic learning is used

    def __init__(self, alpha, tol):
        arrays = self.reader()
        self.alpha = alpha
        self.tol = tol

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
            y_hat = self.logistic(weights, x_vector)
            x = x_vector[index]
            classification = (y - y_hat)*y_hat*(1-y_hat)
            print(classification)
            weights[index] = weights[index] + alpha * classification * x

            t += 1
            self.update_alpha(t)

            index += 1 if index < 2 else 0

            if y_hat -  > 0:
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
    Perceptron(0.5, 2)

if __name__=="__main__":
    main()