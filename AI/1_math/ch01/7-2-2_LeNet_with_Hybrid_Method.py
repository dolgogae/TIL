import tensorflow as tf

from tensorflow.keras.layers import Conv2D, AveragePooling2D, Dense, Flatten, Layer
from tensorflow.keras.models import Model, Sequential


class ConvLayer(Layer):
    def __init__(self, filters, padding, pool=True):
        super(ConvLayer, self).__init__()
        self.pool == pool

        self.conv1 = Conv2D(filters=filters, kernel_size=5, padding=padding,
                            activation='tanh')
        if pool == True:
            self.conv1_pool = AveragePooling2D(pool_size=2, strides=2)

    def call(self, x):
        x = self.conv1(x)
        if pool == True:
            x = self.conv1_pool(x)
        return x

class LeNet(Model):
    def __init__(self):
        super(LeNet, self).__init__()

        self.conv1 = ConvLayer(filters=6, padding='same')
        self.conv2 = ConvLayer(filters=16, padding='valid')
        self.conv3 = ConvLayer(filters=120, padding='valid', pool=False)
       
        self.flatten = Flatten()

        self.dense1 = Dense(units=84, activation='tanh')
        self.dense2 = Dense(units=10, activation='softmax')

        def call(self, x):
            print("x: {}".format(x.shape))

            x = self.conv1(x)
            x = self.conv2(x)
            x = self.conv3(x)

            x = self.flatten(x)

            x = self.dense1(x)
            x = self.dense2(x)

            return x

x = tf.random.normal(shape=(32, 28, 28, 1))
model = LeNet()
predictions = model(x)