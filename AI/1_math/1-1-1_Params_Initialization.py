import tensorflow as tf

from tensorflow.keras.layers import Dense
from tensorflow.keras.initializers import Constant

x = tf.constant([[10.]])    

# weight/bias setting
w, b = tf.constant(10.), tf.constant(20.)   # 기본이 floating point
w_init, b_init = Constant(w), Constant(b)

# imp. an affine function
dense = Dense(units=1,
              activation='linear',
              kernel_initializer=w_init,
              bias_initializer=b_init)

y_tf = dense(x)

W, B = dense.get_weights()

print("W: {}\n{}\n".format(W.shape, W))
print("B: {}\n{}\n".format(B.shape, B))