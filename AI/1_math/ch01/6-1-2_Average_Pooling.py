import tensorflow as tf
import numpy as np

from tensorflow.keras.layers import AveragePooling1D

L, f, s = 10, 2, 1

x = tf.random.normal(shape=(1, L, 1))
pool_avg = AveragePooling1D(pool_size=f, stride=s)
pooled_avg = pool_max(x)

print("x: {}\n{}".format(x.shape, x.numpy().flatten()))
print("pooled_avg(TF): {}\n{}".format(pooled_avg.shape, pooled_avg.numpy().flatten()))

x = x.numpy().flatten()
pool_avg_man = np.zeros((L - f + 1, ))
for i in range(L-f+1):
    window = x[i:i+f]
    pooled_avg_man[i] = np.mean(window)

print("pooled_avg(Manual): {}\n{}".format(pooled_avg_man.shape, pooled_avg_man))