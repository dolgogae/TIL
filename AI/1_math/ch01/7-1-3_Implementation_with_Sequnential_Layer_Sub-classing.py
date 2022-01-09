import tensorflow as tf
import numpy as np

from tensorflow.keras.models import Model
from tensorflow.keras.layers import Layer, Conv2D, MaxPooling2D, Flatten, Dense

N, n_H, n_W, n_C = 4, 28, 28, 3
n_conv_neurons = [10, 20, 30]
n_dense_neurons = [50, 30, 10]
k_size, padding = 2, 'same'
pool_size, pool_strides = 2, 2

x = tf.random.normal(shape=(N, n_H, n_W, n_C))

class MyConv(Layer):
    def __init__(self, n_neuron):
        super(MyConv, self).__init__()

        self.conv = Conv2D(filters=n_neuron, kernel_size=k_size, padding=padding,
                 activation='relu')

        self.conv_pool = MaxPooling2D(pool_size=pool_size, strides=pool_strides))

    def call(self, x):
        x = self.conv(x)
        x = self.conv_pool(x)
        return x

model = Sequential()
model.add(MyConv(n_conv_neurons[0]))
model.add(MyConv(n_conv_neurons[1]))
model.add(MyConv(n_conv_neurons[2]))
model.add(Flatten())

model.add(Dense(units=n_dense_neurons[0], activation='relu'))
model.add(Dense(units=n_dense_neurons[1], activation='relu'))
model.add(Dense(units=n_dense_neurons[2], activation='softmax'))

predictions = model(x)