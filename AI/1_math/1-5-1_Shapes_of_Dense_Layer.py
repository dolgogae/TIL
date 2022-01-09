import tensorflow as tf

from tensorflow.keras.layers import Dense

N, n_feature = 8, 10
x = tf.random.normal(shape=(N, n_feature))  # generate minibatch

dense = Dense(units=1, activation='relu')
y = dense(x)

W, B = dense.get_weights()

print("Shape of x: ", x.shape)
print("Shape of W: ", W.shape)
print("Shape of B: ", B.shape)