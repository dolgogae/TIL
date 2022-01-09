import tensorflow as tf

from tensorflow.keras.losses import MeanSquaredError
from tensorflow.keras.layers import Dense

N, n_feature = 100, 5

X = tf.random.normal(shape=(N, n_feature))
Y = tf.random.normal(shape=(N, 1))

dataset = tf.data.Dataset.from_tensor_slices((X, Y))
dataset = dataset.batch(batch_size)

model = Dense(units=1, activation='linear')
loss_object = MeanSquaredError()

for x, y in dataset:
    predictions = model(x)
    loss = loss_object(y, predictions)
    print(loss.numpy())