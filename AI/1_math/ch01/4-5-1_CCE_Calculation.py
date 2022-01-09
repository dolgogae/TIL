import tensorflow as tf

from tensorflow.keras.losses import CategoricalCrossentropy

batch_size, n_classes = 16, 5

predictions = tf.random.uniform(shape=(batch_size, n_classes),
                                minval=0, maxval=1,
                                dtype=tf.float32)

pred_sum = tf.reshape(tf.reduce_sum(predictions, axis=1), (-1, 1))
predictions = predictions/pred_sum

labels = tf.random.uniform(shape=(batch_size, ),
                           minval=0, maxval=n_classes,
                           dtype=tf.int32)

labels = tf.one_hot(labels, n_classes)

loss_object = CategoricalCrossentropy()
loss = loss_object(labels, predictions)

print("CCE(TF): ", loss.numpy())

tmp = tf.reduce_mean(tf.reduce_sum(labels*tf.math.log(predictions), axis=1))
print("CCE(Man): ", tmp.numpy())