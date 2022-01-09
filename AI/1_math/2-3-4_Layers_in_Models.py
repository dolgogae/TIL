import tensorflow as tf

from tensorflow.keras.layers import Dense
from tensorflow.keras.models import Sequential, Model

X = tf.random.normal(shape=(4, 10))

model = Sequential()
model.add(Dense(units=10, activation='sigmoid'))
model.add(Dense(units=20, activation='sigmoid'))

Y = model(X)

print(type(model.layers))   # <class 'list'>
print(model.layers)         # include two object

dense1 = model.layers[0]
for tmp in dir(dense1):     # print method name
    print(tmp)

for layer in model.layers:
    w, b = layer.get_weights()
    print(w, b)

