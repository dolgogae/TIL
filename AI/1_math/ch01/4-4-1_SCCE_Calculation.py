import tensorflow as tf

from tensorflow.keras.losses import SparseCategoricalCrossentropy

batch_size, n_classes = 16, 5

predictions = tf.random.uniform(shape=(batch_size, n_classes),
                                minval=0, maxval=1,
                                dtype=tf.float32)

pred_sum = tf.reshape(tf.reduce_sum(predictions, axis=1), (-1, 1))
predictions = predictions/pred_sum

labels = tf.random.uniform(shape=(batch_size, ),
                           minval=0, maxval=n_classes,
                           dtype=tf.int32)

loss_object = SparseCategoricalCrossentropy()
loss = loss_object(labels, predictions)

print(loss.numpy())

ce = 0
for label, prediction in zip(labels, predictions):
    # print(label, prediction)
    ce += -tf.math.log(prediction[label])
ce /= batch_size
print(ce.numpy())
    