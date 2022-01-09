import math
import numpy as np
import tensorflow as tf

from tensorflow.keras.layers import MaxPooling2D

N, n_H, n_W, n_C = 1, 5, 5, 3
f,s = 2, 2

x = tf.random.normal(shape=(N, n_H, n_W, n_C))
print("x: {}\n{}".format(x.shape,
                         np.transpose(x.numpy().squeeze(), (2, 0, 1))))

pool_max = MaxPooling2D(pool_size=f, strides=s)
pooled_max = pool_max(x)

pooled_max_t = np.transpose(pooled_max.numpy().squeeze(), (2, 0, 1))
print("pooled_max(TF): {}\n{}".format(pooled_max.shape,
                                      pooled_max_t))


x = x.numpy().squeeze()
n_H = math.floor((n_H - f)/s + 1)
n_W = math.floor((n_W - f)/s + 1)
pooled_max_man = np.zeros(shape=(n_H, n_W_, n_C))

for c in range(n_C):
    c_image = x[:, :, c]

    for h in range(0, n_H - f + 1, s):
