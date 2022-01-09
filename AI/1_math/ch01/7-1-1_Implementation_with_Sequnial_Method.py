import tensorflow as tf
import numpy as np

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense

N, n_H, n_W, n_C = 4, 28, 28, 3
n_conv_neurons = [10, 20, 30]
n_dense_neurons = [50, 30, 10]
k_size, padding = 2, 'same'
pool_size, pool_strides = 2, 2

x = tf.random.normal(shape=(N, n_H, n_W, n_C))
print(x.shape)

model = Sequential()

model.add(Conv2D(filters=n_conv_neurons[0], kernel_size=k_size, padding=padding,
                 activation='relu'))
model.add(Conv2D(filters=n_conv_neurons[1], kernel_size=k_size, padding=padding,
                 activation='relu'))
model.add(Conv2D(filters=n_conv_neurons[2], kernel_size=k_size, padding=padding,
                 activation='relu'))                 
model.add(MaxPooling2D(pool_size=pool_size, strides=pool_strides))
model.add(Flatten())

model.add(Dense(units=n_dense_neurons[0], activation='relu'))
model.add(Dense(units=n_dense_neurons[1], activation='relu'))
model.add(Dense(units=n_dense_neurons[2], activation='softmax'))

predictions = model(x)
print(predictions.shape)