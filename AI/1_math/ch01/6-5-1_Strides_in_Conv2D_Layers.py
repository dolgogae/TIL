import tensorflow as tf

from tensorflow.keras.layers import Conv2D

images = tf.random.normal(shape=(1,28,28,3))
conv = Conv2D(filters=1, kernel_size=3, padding='valid', strides=2)
y = conv(images)

print(y.shape)