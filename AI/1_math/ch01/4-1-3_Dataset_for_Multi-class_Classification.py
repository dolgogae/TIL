import tensorflow as tf
import matplotlib.pyplot as plt

plt.style.use('seaborn')

N, n_feature = 30, 2
n_classes = 5

X = tf.zeros(shape=(0, n_feature))
Y = tf.zeros(shape=(0,1), dtype=tf.int32)

fig, ax = plt.subplots(figsize=(5,5))
for class_idx in range(n_classes):
    center = tf.random.uniform(minval=-15, maxval=15, shape=(2,))

    x1 = center[0] + tf.random.normal(shape=(N, 1))
    x2 = center[1] + tf.random.normal(shape=(N, 1))

    # ax.scatter(x[:, 0].numpy(), x[:, 1].numpy(), alpha=0.3)

    x = tf.concat((x1,x2), axis=0)
    y = class_idx*tf.ones(shape=(N,1), dtype=tf.int32)

    # ax.scatter(x[:, 0].numpy(), x[:, 1].numpy(), alpha=0.3)
    X = tf.concat((X,x), axis=1)
    Y = tf.concat((Y,y), axis=2)