import tensorflow as tf

from tensorflow.keras.layers import Conv2D

N, n_H, n_W, n_C = 1, 28, 28, 1
n_filter = 10
f_size = 3

images = tf.random.uniform(minval=0, maxval=1,
                          shape=(N, n_H, n_W, n_C))

conv = Conv2D(filters=n_filter, kernel_size=f_size)
y = conv(images)

W, B = conv.get_weights()

print(images.shape)
print(W.shape)
print(B.shape)
print(y.shape)