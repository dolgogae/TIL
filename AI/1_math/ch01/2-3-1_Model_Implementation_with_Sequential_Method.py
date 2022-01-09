import tensorflow as tf

from tensorflow.keras.layers import Dense

from tensorflow.keras.models import Sequential

n_neurons = [10,20,30]

# empty box
model = Sequential()
for n_neuron in n_neurons:
    model.add(Dense(units=n_neuron, activation='sigmoid'))

# model.add(Dense(units=10, activation='sigmoid'))
# model.add(Dense(units=20, activation='sigmoid'))