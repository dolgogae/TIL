import tensorflow as tf

from tensorflow.keras.layers import Dense
from tensorflow.keras.models import Sequential, Model

X = tf.random.normal(shape=(4, 10))

# sequential method
model = Sequential()
model.add(Dense(units=10, activation='sigmoid'))
model.add(Dense(units=20, activation='sigmoid'))

Y = model(X)
print(Y.numpy())

# Model-subclassing
class TestModel(Model):
    def __init__(self):
        super(TestModel, self).__init__()
    
        self.dense1 = Dense(units=10, activation='sigmoid')
        self.dense2 = Dense(units=20, activation='sigmoid')
    
    def call(self, x):
        x = self.dense1(x)
        x = self.dense2(x)
        return x

model1 = TestModel()
Y = model1(X)

class TestModel2(Model):
    def __init__(self, n_neurons):
        super(TestModel, self).__init__()
        self.n_neurons = n_neurons

        self.dense_layers = []
        for n_neuron in self.n_neurons:
            self.dense_layers.append(Dense(units=n_neuron, activation='sigmoid'))
    
    def call(self, x):
        for dense in self.dense_layers:
            x = dense(x)
        return x

n_neurons = [10,20,30]
model2 = TestModel2(n_neurons)
Y = model2(X)
