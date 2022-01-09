import tensorflow as tf

import tensorflow.keras.losses import BinaryCrossEntropy

batch_size = 32

predictions = tf.random.uniform(shape=(batch_size, 1),
                                minval=0, maxval=1,
                                dtype=tf.float32)
labels = tf.random.uniform(shape=(batch_size, 1),
                          minval=0, maxval=n_classes,
                          dtype=tf.int32)

loss_object = BinaryCrossEntropy()
loss = loss_object(labels, predictions)

labels = tf.cast(labels, tf.float32)
bce_man = (labels*tf.math.log(predictions) + (1-labels)*tf.math.log(1-predictions))
bce_man = tf.reduce_mean(bce_man)

print("BCE(TF):", loss.numpy())
print("BCE(Man):", bce_man.numpy())