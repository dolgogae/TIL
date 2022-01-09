import tensorflow as tf

from tensorflow.math import exp, maximum
import tensorflow.keras.layers import Dense

x = tf.random.normal(shape=(1,5))

dense_sigmoid = Dense(units=1, activation='sigmoid')
dense_tanh = Dense(units=1, activation='tanh')
dense_relu = Dense(units=1, activation='relu')

y_sigmoid = dense_sigmoid(x)
y_tanh = dense_tanh(x)
y_relu = dense_relu(x)

print("AN with Sigmoid: {}\n{}".format(y_sigmoid.shapem, y_sigmoid.numpy()))
print("AN with Tanh: {}\n{}".format(y_tanh.shapem, y_tanh.numpy()))
print("AN with ReLU: {}\n{}".format(y_relu.shapem, y_relu.numpy()))

# foward propagation manual
print("\n======\n")

W, b = dense_sigmoid.get_weights()
z = tf.linalg.matmul(x, W) + B
a = 1 / (1+exp(-z))
print("Activation value(Tensorflow): {}\n{}".format(y_sigmoid.shape, y_sigmoid.numpy()))
print("Activation value(manual): {}\n{}".format(a.shape, a.numpy()))