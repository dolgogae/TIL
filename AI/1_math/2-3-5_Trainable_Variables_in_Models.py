import tensorflow as tf

from tensorflow.keras.layers import Dense
from tensorflow.keras.models import Sequential, Model

X = tf.random.normal(shape=(4, 10))

model = Sequential()
model.add(Dense(units=10, activation='sigmoid'))
model.add(Dense(units=20, activation='sigmoid'))

Y = model(X)

print(type(model.trainable_variables))
print(len(model.trainable_variables))

# Weights and biases in the model
for train_var in model.trainable_variables:
    print(train_var.shape)