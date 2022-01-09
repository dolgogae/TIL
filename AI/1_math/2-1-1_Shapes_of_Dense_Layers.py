import tensorflow as tf

from tensorflow.keras.layers import Dense

N, n_feature = 8, 10
x = tf.random.normal(shape=(N, n_feature))

n_neuron = 3
dense = Dense(units=n_neuron, activation='sigmoid')

y = dense(x)

W, B = dense.get_weights()

print("=== Input/Weight/Bias ===")
print("x: ", x.shape)
print("W: ", W.shape)
print("B: ", B.shape)
print("y: ", y.shape)