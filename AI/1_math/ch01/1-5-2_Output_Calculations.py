import tensorflow as tf

from tensorflow.keras.layers import Dense
from tensorflow.math import exp

N, n_feature = 8, 10
x = tf.random.normal(shape=(N, n_feature))  # generate minibatch

dense = Dense(units=1, activation='sigmoid')
y_tf = dense(x)

W, B = dense.get_weights()

y_man = tf.linalg.matmul(x, W) + B
y_man = 1/(1+exp(-y_man))

print("Output(TF): \n", y_tf.numpy())
print("Output(manual): \n", y_man.numpy())