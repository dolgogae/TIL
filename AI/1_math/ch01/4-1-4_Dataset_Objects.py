import tensorflow as tf

N, n_feature = 100, 5
batch_size = 32
t_weights = tf.constant([1,2,3,4,5], dtype=tf.float32)
t_bias = tf.constant([10], dtype=tf.float32)

X = tf.random.normal(mean=0, stddev=1, shape=(N, n_feature))
Y = tf.reduce_sum(t_weights * X, axis=1) + t_bias

# for batch_idx in range(N // batch_size):
#     x = X[batch_idx*batch_size : (batch_idx+1)*batch_size, ...]
#     x = X[batch_idx*batch_size : (batch_idx+1)*batch_size, ...]

#     print(x.shape, y.shape)

dataset = tf.data.Dataset.from_tensor_slices((X, Y))
dataset = dataset.batch(batch_size).shuffle(100)

for x, y in dataset:
    print(x.shape, y.shape)