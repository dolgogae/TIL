import tensorflow as tf

from tensorflow.keras.losses import SparseCategoricalCrossentropy

y = tf.random.normal(minval=0, maxval=10,
                     shape=(32,),
                     dtype=tf.int32)

y = tf.one_hot(y, depth=10)

loss_object = SparseCategoricalCrossentropy()
loss = loss_object(y)
