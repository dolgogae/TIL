import tensorflow as tf

from tensorflow.keras.layers import Dense

x = tf.constant([[10.]])      # input setting(Note: input->matrix)

dense = Dense(units=1, activation='linear')        # w,b

y_tf = dense(x) # forward propagation + param initialization
# print(y_tf)
W, B = dense.get_weights()     # get weight, bias'

y_man = tf.linalg.matmul(x, W) + B  # forward propagation(manual)

print("===== Input/Weight/Bias =====")
print("x: {}\n{}\n".format(x.shape, x.numpy()))
print("W: {}\n{}\n".format(W.shape, W))
print("B: {}\n{}\n".format(B.shape, B))

print("===== output =====")
print("y(Tensorflow): {}\n{}\n".format(y_tf.shape, y_tf.numpy()))
print("y(Manual): {}\n{}\n".format(y_man.shape, y_man.numpy()))