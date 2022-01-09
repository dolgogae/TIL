import tensorflow as tf
import matplotlib.pyplot as plt

from tensorflow.keras.layers import Dense

plt.style.use('seaborn')

X = tf.random.normal(shape=(100,5))
dense = Dense(units=1, activation='sigmoid')

Y = dense(X)
print(Y.shape)