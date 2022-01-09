import tensorflow as tf

from tensorflow.keras.losses import MeanSquaredError

loss_object = MeanSquaredError()

batch_size = 32
prediction = tf.random.normal(shape=(batch_size, 1))
labels = tf.random.normal(shape=(batch_size, 1))

mse = loss_object(labels, prediction)
mse_man = tf.reduce_mean(tf.math.pow(labels - prediction, 2))

print("MSE(TF): ",mse.numpy())
print("MSE(man): ",mse_man.numpy())