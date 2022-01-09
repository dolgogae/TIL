import tensorflow as tf

from tensorflow.keras.layers import MaxPooling2D

images = tf.random.normal(shape=(1,28,28,3))
conv = MaxPooling2D(pool_size=3, strides=2)
y = conv(images)

print(y.shape)