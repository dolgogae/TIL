import tensorflow as tf
import numpy as np

from tensorflow.keras.layers import Conv2D, AveragePooling2D, Dense, Flatten, Layer
from tensorflow.keras.models import Model, Sequential

from tensorflow.keras.datasets import mnist
from tensorflow.keras.losses import SparseCategoricalCrossentropy

### LeNet Implementation ###
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
        if self.pool == True:
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

### Dataset Preparation ###
(train_images, train_labels), _ = minst.load_data()
train_images = np.expand_dims(train_images, axis=3).astype(np.float32)
train_labels = train_labels.astype(np.int32)
print(train_images.shape, train_images.dtype)
print(train_labels.shape, train_labels.dtype)

train_ds = tf.data.Dataset.from_tensor_slices((train_images, train_labels))
train_ds = train_ds.batch(32)

### Forward Propagation ###
model = LeNet()
loss_object = SparseCategoricalCrossentropy()

for images, labels in train_ds:
    predictions = model(images)
    loss = loss_object(predictions, labels)