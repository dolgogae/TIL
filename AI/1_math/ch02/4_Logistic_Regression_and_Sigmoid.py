import numpy as np
import matplotlib.pyplot as plt
import matplotlib.cm as cm

x = np.linspace(-3,3,100)

n_w = 20
# w_list = np.linspace(0.1,10,n_w)
w = 1
b_list = np.linspace(0,5,n_w)
cmap = cm.get_cmap('rainbow', lut=n_w)

fig, ax = plt.subplots(figsize=(10,5))
for w_idx, w in enumerate(w_list):
    z = x*w+b
    sigmoid = 1/(1+np.exp(-z))

    ax.plot(xm sigmoid, color=cmap(w_idx))