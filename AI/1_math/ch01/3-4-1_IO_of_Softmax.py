import tensorflow as tf
from tensorflow.keras.layers import Activation

logit = tf.random.uniform(shape=(8,5), minval=-10, maxval=10)

softmax_value = Activation('softmax')(logit)
softmax_sum = tf.reduce_sum(softmax_value, axis=1)

print("Logits: \n", logit.numpy())
print("Probabilities: \n", softmax_value.numpy())
print("Sum of Probabilities: \n", softmax_sum)