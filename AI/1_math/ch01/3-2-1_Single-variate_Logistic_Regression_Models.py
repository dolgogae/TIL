import tensorflow as tf
import matplotlib.pyplot as plt

from tensorflow.keras.layers import Dense

plt.style.use('seaborn')

X = tf.random.normal(shape=(1,1))
dense = Dense(units=1, activation='sigmoid')

Y = dense(X)

fig, ax = plt.subplots(figsize=(10,5))
ax.plot(X.numpy().flatten(), Y.numpy().flatten())