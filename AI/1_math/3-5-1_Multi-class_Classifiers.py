import tensorflow as tf

from tensorflow.keras.models import Model
from tensorflow.keras.layers import Dense

class TestModel(Model):
    def __init__(self):
        super(TestModel, self).__init__()

        self.dense1 = Dense(units=8, activation='relu')
        self.dense2 = Dense(units=8, activation='relu')
        self.dense3 = Dense(units=3, activation='softmax')

    def call(self, x):
        print("X: {}\n{}\n".format(x.shape, x.numpy()))

        x = self.dense1(x)
        print("A1: {}\n{}\n".format(x.shape, x.numpy()))
        x = self.dense2(x)
        print("A2: {}\n{}\n".format(x.shape, x.numpy()))
        x = self.dense3(x)
        print("Y: {}\n{}\n".format(x.shape, x.numpy()))
        print("Sum of vectors: {}\n".format(tf.reduce_sum(x, axis=1)))

        return x
        

model = TestModel()

logit = tf.random.uniform(shape=(8,5), minval=-10, maxval=10)
Y = model(logit)