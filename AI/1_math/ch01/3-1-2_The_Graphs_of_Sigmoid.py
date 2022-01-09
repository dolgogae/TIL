import tensorflow as tf
import matplotlib.pyplot as plt

from tensorflow.keras.layers import Activation

X = tf.linspace(-10, 10, 100)
sigmoid = Activation('sigmoid')(X)

fig, ax = plt.subplots(figsize=(10,5))
ax.plot(X.numpy(), sigmoid.numpy())