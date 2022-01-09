import tensorflow as tf
import numpy as np

from tensorflow.keras.models import Model
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten, Dense

N, n_H, n_W, n_C = 4, 28, 28, 3
n_conv_neurons = [10, 20, 30]
n_dense_neurons = [50, 30, 10]
k_size, padding = 2, 'same'
pool_size, pool_strides = 2, 2

x = tf.random.normal(shape=(N, n_H, n_W, n_C))

class TestModel(Model):
    def __init__(self):
        super(TestModel, self).__init__()

        self.conv1 = Conv2D(filters=n_conv_neurons[0], kernel_size=k_size, padding=padding,
                 activation='relu')
        self.conv1_pool = MaxPooling2D(pool_size=pool_size, strides=pool_strides)

        self.conv2 = Conv2D(filters=n_conv_neurons[1], kernel_size=k_size, padding=padding,
                        activation='relu')
        self.conv2_pool = MaxPooling2D(pool_size=pool_size, strides=pool_strides)
        
        self.conv3 = Conv2D(filters=n_conv_neurons[2], kernel_size=k_size, padding=padding,
                        activation='relu')    
        self.conv3_pool = MaxPooling2D(pool_size=pool_size, strides=pool_strides)
        self.flatten = Flatten()

        self.dense1 = Dense(units=n_dense_neurons[0], activation='relu')
        self.dense2 = Dense(units=n_dense_neurons[1], activation='relu')
        self.dense3 = Dense(units=n_dense_neurons[2], activation='softmax')

    def call(self, x):
        x = self.conv1(x)
        x = self.conv1_pool(x)

        x = self.conv2(x)
        x = self.conv2_pool(x)

        x = self.conv3(x)
        x = self.conv3_pool(x)

        x = self.flatten(x)

        x = self.dense1(x)
        x = self.dense2(x)
        x = self.dense3(x)
        return x