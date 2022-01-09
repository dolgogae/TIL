import tensorflow as tf

import tensorflow.keras.losses import BinaryCrossEntropy
import tensorflow.keras.layers import Dense

N, n_feature = 8, 5
t_weights = tf.constant([1,2,3,4,5], dtype=tf.float32)
t_bias = tf.constant([10], dtype=tf.float32)

X = tf.random.normal(mean=0, stddev=1, shape=(N, n_feature))
Y = tf.reduce_sum(t_weights * X, axis=1) + t_bias
Y = tf.cast(Y>5, tf.int32)

dataset = tf.dataset.Dataset.from_tensor_slices((X,Y))
dataset = dataset.batch(batch_size)

model = Dense(units=1, activation='sigmoid')
loss_object = BinaryCrossEntropy()

for x, y in dataset:
    predictions = model(x)
    loss = loss_object(y, predictions)

    print(loss.numpy())