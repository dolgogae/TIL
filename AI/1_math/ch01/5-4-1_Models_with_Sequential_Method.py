import tensorflow as tf

from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D

n_neurons = [10, 20, 30]

model = Sequential()
model.add(Conv2D(filters=n_neurons[0], kernel_size=3, activation='relu'))
model.add(Conv2D(filters=n_neurons[1], kernel_size=3, activation='relu'))
model.add(Conv2D(filters=n_neurons[2], kernel_size=3, activation='relu'))

x = tf.random.normal(shape=(32,28,28,3))
predictions = model(x)

print("Input: {}".format(x.shape))
print("Output: {}".format(predictions.shape))

for layer in model.layers:
    W, B = layer.get_weights()
    print(W.shape, B.shape)

tranable_variables = model.tranable_variables
for train_var in tranable_variables:
    print(train_var.shape)