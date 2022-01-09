import tensorflow as tf

from tensorflow.keras.layers import layers
from tensorflow.math import exp, maximum

activation = 'sigmoid'
# activation = 'tanh'
# activation = 'relu'

x = tf.random.uniform(shape=(1,10))

dense = Dense(units=1, activation=activation)

y_tf = dense(x)
W, B = dense.get_weights()

y_man = tf.linalg.matmul(x, W) + B
if activation == 'sigmoid':
    y_man = 1/(1+exp(-y_man))
elif activation == 'tanh':
    y_man = (exp(y_man)-exp(-y_man))/(exp(y_man)+exp(-y_man))
elif activation == 'relu':
    y_man = maximum(0, y_man)


print("Activation: ", activation)
print("y_tf: {}\n{}\n".format(y_tf.shape, y_tf.numpy()))
print("y_man: {}\n{}\n".format(y_man.shape, y_man.numpy()))