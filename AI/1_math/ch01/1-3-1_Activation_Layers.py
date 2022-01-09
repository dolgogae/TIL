import tensorflow as tf

from tensorflow.math import exp, maximum
from tensorflow.keras.layers import Activation

x = tf.random.normal(shape=(1,5))   # input setting

# imp. activation func
sigmoid = Activation('sigmoid')
tanh = Activation('tanh')
relu = Activation('relu')

# foward propagation
y_sigmoid_tf = sigmoid(x)
y_tanh_tf = tanh(x)
y_relu_tf = relu(x)

# forward propagation(manual)
y_sigmoid_man = 1 / (1 + exp(-x))
y_tanh_man = (exp(x)-exp(-x))/(exp(x)+exp(-x))
y_relu_man = maximum(x,0)


print("Sigmoid(Tensorflow): {}\n{}".format(y_sigmoid_tf.shape, y_sigmoid_tf.numpy()))
print("Sigmoid(manual): {}\n{}".format(y_sigmoid_man.shape, y_sigmoid_man.numpy()))

print("tanh(Tensorflow): {}\n{}".format(y_tanh_tf.shape, y_tanh_tf.numpy()))
print("tanh(manual): {}\n{}".format(y_tanh_man.shape, y_tanh_man.numpy()))

print("ReLu(Tensorflow: {}\n{}".format(y_relu_tf.shape, y_relu_tf.numpy()))
print("ReLu(manual): {}\n{}".format(y_relu_man.shape, y_relu_man.numpy()))