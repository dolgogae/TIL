import tensorflow as tf

from tensorflow.keras.layers import Dense

N, n_feature = 4, 10
X = tf.random.normal(shape=(N, n_feature))

n_neuron = [3, 5]
dense1 = Dense(units=n_neuron[0], activation='sigmoid')
dense2 = Dense(units=n_neuron[1], activation='sigmoid')

# forward propagation
A1 = dense1(X)
Y = dense2(A1)

print("X: {}\n".format(X.shape))
print("A1: {}\n".format(A1.shape))
print("Y: {}\n".format(Y.shape))

# get weight/bias
W1, B1 = dense1.get_weights()
W2, B2 = dense2.get_weights()

print("W1: {}\n".format(W1.shape))
print("B1: {}\n".format(B1.shape))

print("W2: {}\n".format(W2.shape))
print("B2: {}\n".format(B2.shape))