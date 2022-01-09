import numpy as np
import tensorflow as tf

from tensorflow.keras.layers import Conv2D

N, n_H, n_W, n_C = 32, 28, 28, 1
n_filter = 5
k_size = 3

images = tf.random.uniform(minval=0, maxval=1,
                          shape=(N, n_H, n_W, n_C))

conv = Conv2D(filters=n_filter, kernel_size=k_size)
Y = conv(images)

W, B = conv.get_weights()

print("Input Image: {}".format(images.shape))
print("W/B: {}".format(W.shape, B.shape))
print("Output Image: {}".format(Y.shape))