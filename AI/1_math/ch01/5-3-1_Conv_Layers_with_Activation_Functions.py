import numpy as np
import tensorflow as tf

from tensorflow.keras.layers import Conv2D

N, n_H, n_W, n_C = 1, 5, 5, 1
n_filter = 4
k_size = 4
images = tf.random.uniform(minval=0, maxval=1,
                          shape=(N, n_H, n_W, n_C))

# Forward Propagation(TF)
conv = Conv2D(filters=n_filter, kernel_size=k_size, activation='sigmoid')
Y = conv(images)
Y = np.transpose(Y.numpy().squeeze(), (2, 0, 1))

print(Y.shape)
print("Y(TF): \n", Y)

W, B = conv.get_weights()

# Foward Propagation(Manual)
images = images.numpy().squeeze()

Y_man = np.zeros(shape=(n_H - f_size + 1, n_W - f_size + 1, n_filter))

for c in range(n_filter):
    c_W = W[:, :, :, c]
    c_b = B[c]

    for h in range(n_H - f + 1):
        for w in range(n_W - f + 1):
            window = images[h:h+f, w:w+f, :]
            z = np.sum(window*c_W) + c_b
            z = 1 / (1 + np.exp(-z))

            Y_man[h,w,c] = z

print("Y(Man): \n", np.transpose(Y_man, (2,0,1)))
