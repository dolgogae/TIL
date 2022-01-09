import tensorflow as tf

from tensorflow.keras.layers import Conv2D, AveragePooling2D, Dense, Flatten
from tensorflow.keras.models import Model

class LeNet(Model):
    def __init__(self):
        super(LeNet, self).__init__()

        self.conv1 = Conv2D(filters=6, kernel_size=5, padding='same',
                            activation='tanh')
        self.conv1_pool = AveragePooling2D(pool_size=2, strides=2)

        self.conv2 = Conv2D(filters=16, kernel_size=5, padding='valid',
                            activation='tanh')
        self.conv2_pool = AveragePooling2D(pool_size=2, strides=2)

        self.conv3 = Conv2D(filters=120, kernel_size=5, padding='valid',
                            activation='tanh')
        self.conv3_pool = AveragePooling2D(pool_size=2, strides=2)
        self.flatten = Flatten()

        self.dense1 = Dense(units=84, activation='tanh')
        self.dense2 = Dense(units=10, activation='softmax')

        def call(self, x):
            print("x: {}".format(x.shape))

            x = self.conv1(x)
            x = self.conv1_pool(x)

            x = self.conv2(x)
            x = self.conv2_pool(x)

            x = self.conv3(x)
            x = self.conv3_pool(x)

            x = self.flatten(x)

            x = self.dense1(x)
            x = self.dense2(x)

            return x

x = tf.random.normal(shape=(32, 28, 28, 1))
model = LeNet()
predictions = model(x)