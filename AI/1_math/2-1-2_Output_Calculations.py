import tensorflow as tf
import numpy as np

from tensorflow.math import exp
from tensorflow.linalg import matmul
from tensorflow.keras.layers import Dense

N, n_feature = 8, 10
X = tf.random.normal(shape=(N, n_feature))

n_neuron = 3
dense = Dense(units=n_neuron, activation='sigmoid')

y_tf = dense(X)

W, B = dense.get_weights()
print("y(tensorflow): \n", y_tf.numpy())

Z = matmul(X, W) + B
y_man_matmul = 1 / (1 + exp(-Z))
print("y(manual): \n", y_man_matmul.numpy())

# calculation with dot products
y_man_vec = np.zeros(shape=(N, n_neuron))
for x_idx in range(N):
    x = X[x_idx]

    for nu_idx in range(n_neuron):
        w, b = W[:, nu_idx], B[nu_idx]

        z = tf.reduce_sum(x * w) + b
        a = 1 / (1 + np.exp(-z))
        y_man_vec[x_idx, nu_idx] = a

print("y(with dot product: ", y_man_vec)