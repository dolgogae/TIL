import tensorflow as tf

from tensorflow.keras.models import Model
from tensorflow.keras.layers import Conv2D


n_neurons = [10, 20, 30]

class TestModel(Model):
    def __init__(self):
        super(TestModel, self).__init__()
        global n_neurons

        self.conv_layers = []
        for n_neuron in n_neurons:
            self.conv_layers.append(Conv2D(filters=n_neuron, kernel_size=3, activation='relu'))

    def call(self, x):
        print("Input: ", x.shape)
        print("\n==== Conv Layers ====\n")
        for conv_layer in self.conv_layers:
            x = conv_layer(x)
            W, B = conv_layer.get_weights()
            print("W/B: {}/{}".format(W.shape, B.shape))
            print("X: {}\n".format(x.shape))
        return x

model = TestModel()

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


class TestModel(Model):
    def __init__(self):
        super(TestModel, self).__init__()
        global n_neurons

        self.conv1 = Conv2D(filters=n_neuron[0], kernel_size=3, activation='relu')
        self.conv2 = Conv2D(filters=n_neuron[1], kernel_size=3, activation='relu')
        self.conv3 = Conv2D(filters=n_neuron[2], kernel_size=3, activation='relu')

    def call(self, x):
        x = self.conv1(x)
        x = self.conv2(x)
        x = self.conv3(x)

        return x