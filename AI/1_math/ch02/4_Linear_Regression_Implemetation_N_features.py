import numpy as np
import matplotlib.pyplot as plt
np.random.seed(1)
plt.style.use('seaborn')

# set params
N, n_feature = 100, 3
lr = 0.1
t_W = np.random.uniform(-3,3,(n_feature, 1))
t_b = np.random.uniform(-3,3,(1,1))

W = np.random.uniform(-3,3,(n_feature, 1))
b = np.random.uniform(-3,3,(1,1))

# generate dataset
x_data = np.random.randn(N, n_feature)
# print(x_data, t_W.shape, t_b.shape)
y_data = x_data @ t_W  
# print(y_data.shape)

# print(x_data.shape, y_data.shape)

J_track = list()
W_track, b_track = list(), list()
for data_idx, (X,y) in enumerate(zip(x_data, y_data)):
    W_track.append(W)
    b_track.append(b)

    # forward propagation
    X = X.reshape(1, -1)
    pred = X @ W + n
    print(y.shape, pred.shape)
    J = (y-pred)**2
    J_track.append(J.sqeeze())

    # jacobians
    dJ_dpred = -2*(y - pred)
    dpred_dW = X
    dpred_db = 1

    # backpropagation
    dJ_dW = dJ_dpred*dpred_dW
    dJ_db = dJ_dpred*dpred_db

    # parameter update
    W = W - lr*dJ_dW.T
    b = b - lr*dJ_db

print(W_track[0].shape)
W_track = np.hstack(W_track)
b_track = np.concatenate(b_track).flatten()

# visualize results
fig, axes = plt.subplots(figsize=(20,10))
axes[0].plot(J_track)
axes[0].set_ylabel('MSE', fontsize=30)
axes[0].tick_params(labelsize=20)

cmap = cm.get_cmap('rainbow', lut=n_feature)
for w_idx, (t_w, w) in enumerate(zip(t_w, W_track)):
    axes[1].axhline(y=t_w, color=cmap(w_idx), linestyle=':')
    axes[1].plot(w)
axes[1].axhline(y=t_b, color='black', linestyle=':')
axes[1].plot(b_track, color='black')
axes[1].tick_params(labelsize=20)