import numpy as np
import tensorflow as tf

from tensorflow.keras.layers import Conv2D

N, n_H, n_W, n_C = 1, 28, 28, 1
n_filter = 1
f_size = 3

images = tf.random.uniform(minval=0, maxval=1,
                          shape=(N, n_H, n_W, n_C))

conv = Conv2D(filters=n_filter, kernel_size=f_size)
y = conv(images)
print("Y(TF): ", y.numpy().squeeze())
W, B = conv.get_weights()

####
images = images.numpy().squeeze()
W = W.squeeze()

y_man = np.zeros(shape=(n_H - f_size + 1, n_W - f_size +1))

for i in range(n_H - f_size + 1):
    for j in range(n_W - f_size + 1):
        window = image[i : i+f_size, j : j+f_size, : ]
        y_man[i, j] = np.sum(window*W) + B

# print("Y(Man): ", y_man)
