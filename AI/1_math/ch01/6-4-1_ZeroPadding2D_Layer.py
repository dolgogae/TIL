import tensorflow as tf
import numpy as np

from tensorflow.keras.layers import ZeroPadding2D

images = tf.random.normal(shape=(1, 28, 28, 3))
# print(np.transpose(images.numpy().squeeze(), (2,0,1)))
print(images.shape)

zero_padding = ZeroPadding2D(padding=2)
y = zero_padding(images)
# print(np.transpose(y.numpy().squeeze(), (2,0,1)))
print(y.shape)
