import tensorflow as tf

from tensorflow.keras.layers import Dense
from tensorflow.keras.losses import SparseCategoricalCrossentrpy
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

dataset = tf.dataset.Dataset.from_tensor_slices((X,Y))
dataset = dataset.batch(batch_size)

model = Dense(units=n_classes, activation='softmax')
loss_object = SparseCategoricalCrossentrpy()

for x, y in dataset:
    predictions = model(x)
    loss = loss_object(y, predictions)