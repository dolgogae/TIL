import tensorflow as tf

from tensorflow.linalg import matmul
from tensorflow.math import exp

from tensorflow.keras.layers import Dense

N, n_feature = 4, 10
X = tf.random.normal(shape=(N, n_feature))
X_cp = tf.identity(X)

n_neurons = [3, 4, 5]

dense_layers = []
for n_neuron in n_neurons:
    dense = Dense(units=n_neuron, activation='sigmoid')
    dense_layers.append(dense)

# forward propagation(Tensorflow)
W, B = [], []
for dense_idx, dense in enumerate(dense_layers):
    X = dense(X)
    w, b = dense.get_weights()

    W.append(w)
    B.append(b)

print("Y(tensroflow): ", X.numpy())

# forward propagation(Manaul)
for layer_idx in range(len(n_neurons)):
    w, b = W[layer_idx], B[layer_idx]

    X_cp = matmul(X_cp, w) + b
    X_cp = 1 / (1 + exp(-X_cp))
print("Y(Manaul): ", X_cp.numpy())
