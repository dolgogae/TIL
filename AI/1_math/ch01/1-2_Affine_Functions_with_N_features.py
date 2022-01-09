import tensorflow as tf

from tensorflow.keras.layers import Dense

x = tf.random.uniform(shape=(1,10), minval=0, maxval=10)

dense = Dense(units=1)

y_tf = dense(x)

W, B = dense.get_weights()

y_man = tf.linalg.matmul(x, W) + B 

print("===== Input/Weight/Bias =====")
print("x: {}\n{}\n".format(x.shape, x.numpy()))
print("W: {}\n{}\n".format(W.shape, W))
print("B: {}\n{}\n".format(B.shape, B))

print("===== output =====")
print("y(Tensorflow): {}\n{}\n".format(y_tf.shape, y_tf.numpy()))
print("y(Manual): {}\n{}\n".format(y_man.shape, y_man.numpy()))