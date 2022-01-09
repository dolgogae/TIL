import tensorflow as tf
import numpy as np

from tensorflow.keras.layers import MaxPooling1D

L, f, s = 10, 2, 1

x = tf.random.normal(shape=(1, L, 1))
pool_max = MaxPooling1D(pool_size=f, stride=s)
pooled_max = pool_max(x)

print("x: {}\n{}".format(x.shape, x.numpy().flatten()))
print("pooled_max(TF): {}\n{}".format(pooled_max.shape, pooled_max.numpy().flatten()))

x = x.numpy().flatten()
pool_max_man = np.zeros((L - f + 1, ))
for i in range(L-f+1):
    window = x[i:i+f]
    pooled_max_man[i] = np.max(window)

print("pooled_max(Manual): {}\n{}".format(pooled_max_max.shape, pooled_max_man))