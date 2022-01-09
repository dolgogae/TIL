import tensorflow as tf
from tensorflow.keras.layers import Dense

logit = tf.random.uniform(shape=(8,5), minval=-10, maxval=10)
dense = Dense(units=8, activation='softmax')

Y = dense(logit)
print(tf.reduce_sum(Y, axis=1))