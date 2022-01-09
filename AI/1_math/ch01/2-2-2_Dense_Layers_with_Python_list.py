import tensorflow as tf

from tensorflow.keras.layers import Dense

N, n_feature = 4, 10
X = tf.random.normal(shape=(N, n_feature))

n_neurons = [10, 20, 30, 40, 50, 60, 70]

dense_layers = []
for n_neuron in n_neurons:
    dense = Dense(units=n_neuron, activation='sigmoid')
    dense_layers.append(dense)

print("Input: ", X.shape)
for dense_idx, dense in enumerate(dense_layers):
    X = dense(X)
    # print(X.shape)

Y = X