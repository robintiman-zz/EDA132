import numpy as np
import matplotlib.pyplot as plt

STOCH = 1
BATCH = 2

class GradientDescent:

    def __init__(self, mode, learning_rate, eps):
        # English
        self.total_letters = np.array([35680, 42514, 15162, 35298, 29800, 40255, 74532, 37464, 31030, 24843, 36172,
                                       39552, 72545, 75352, 18031])
        self.total_a = np.array([2217, 2761, 990, 2274, 1865, 2606, 4805, 2396, 1993, 1627,
                                 2375, 2560, 4597, 4871, 1119])

        self.learning_rate = learning_rate
        self.scale_values()
        self.eps = eps
        self.mode = mode
        self.w0, self.w1 = self.regression()

    def func(self, x):
        return self.w1*x + self.w0

    def scale_values(self):
        divider = 100000
        self.total_letters = self.total_letters / divider
        self.total_a = self.total_a / divider
        print(self.total_letters)

    def regression(self):
        w0 = np.random.rand()
        w1 = np.random.rand()
        converged = False
        index = 0
        loss0_Old = 0
        loss1_Old = 0
        while not converged:
            loss0 = self.grad_loss_w0(w0, w1, self.mode, index)
            loss1 = self.grad_loss_w1(w0, w1, self.mode, index)

            w0 = w0 - self.learning_rate * loss0
            w1 = w1 - self.learning_rate * loss1

            if self.mode == BATCH:
                converged = self.check_convergence(loss0, loss1)
            else:
                index += 1
                if index == len(self.total_letters):

                    loss0 = self.grad_loss_w0(w0, w1, BATCH)
                    loss1 = self.grad_loss_w1(w0, w1, BATCH)
                    converged = self.check_convergence(loss0, loss1, loss0_Old, loss1_Old)
                    loss0_Old = loss0
                    loss1_Old = loss1
                    index = 0

        return w0, w1

    def check_convergence(self, loss0, loss1, loss0_old = None, loss1_old = None):
        if self.mode == BATCH:
            return np.abs(loss0) < self.eps and np.abs(loss1) < self.eps
        else:
            return np.abs((loss0 - loss0_old)) < self.eps**2 and np.abs((loss1 - loss1_old)) < self.eps**2

    def grad_loss_w0(self, w0, w1, mode, index = None):
        loss_sum = 0
        if mode == BATCH:
            nbr_samples = len(self.total_letters)
            tmp = np.subtract(self.total_letters, w1*self.total_a + w0)
            loss_sum = -2*np.sum(tmp)
            return loss_sum/nbr_samples
        else:
            loss_sum += -2 * (self.total_letters[index] - (w1 * self.total_a[index] + w0))
            return loss_sum

    def grad_loss_w1(self, w0, w1, mode, index = None):
        loss_sum = 0
        if mode == BATCH:
            nbr_samples = len(self.total_letters)
            tmp = np.subtract(self.total_letters, w1*self.total_a + w0)
            tmp = np.multiply(tmp, self.total_a)
            loss_sum = -2*np.sum(tmp)
            return loss_sum/nbr_samples
        else:
            x = self.total_a[index]
            loss_sum += -2 * (self.total_letters[index] - (w1 * x + w0)) * x
            return loss_sum

def main():
    gd = GradientDescent(STOCH, 0.1, 0.00001)
    x = np.linspace(0, 0.05, 100)
    plt.plot(x, gd.func(x))
    plt.scatter(gd.total_a, gd.total_letters)
    plt.show()

if __name__ == "__main__":
    main()

















