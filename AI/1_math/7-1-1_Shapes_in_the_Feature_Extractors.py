import numpy as np
import tensorflow as tf

from tensorflow.keras.layers import Conv2D, MaxPooling2D, Flatten

N, n_H, n_W, n_c = 32, 28, 28, 3
n_conv_filter = 2
kernel_size = 3
batch_size = 32
pool_size, pool_strides = 2, 2

x = tf.random.normal(shape=(N, n_H, n_W, n_c))

conv1 = Conv2D(filters=n_conv_filter, kernel_size=kernel_size,
             padding='same', activation='relu')
conv_pool1 = MaxPooling2D(pool_size=pool_size, strides=pool_strides)

conv2 = Conv2D(filters=n_conv_filter, kernel_size=kernel_size,
             padding='same', activation='relu')
conv_pool2 = MaxPooling2D(pool_size=pool_size, strides=pool_strides)

flatten = Flatten()

print("Input: {}\n".format(x.shape))

x = conv1(x)
W, B = conv1.get_weights()
print("After conv1: {}".format(x.shape))
x = conv_pool1(x)
print("After conv_pool1: {}".format(x.shape))

x = conv2(x)
W, B = conv1.get_weights()
print("After conv2: {}".format(x.shape))
x = conv_pool2(x)
print("After conv_pool2: {}".format(x.shape))

x = flatten(x)
print("After flatten: {}".format(x.shape))
