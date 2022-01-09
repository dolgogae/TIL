from tensorflow.keras.layers import Dense

n_neurons = [50,25,10]

dense1 = Dense(units=n_neurons[0], activation='relu')
dense2 = Dense(units=n_neurons[1], activation='relu')
dense3 = Dense(units=n_neurons[2], activation='softmax')

x = dense1(x)
print("After dense1: {}".format(x.shape))

x = dense2(x)
x = dense3(x)