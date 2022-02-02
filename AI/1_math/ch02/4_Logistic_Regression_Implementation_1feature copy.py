import numpy as np
import matplotlib.pyplot as plt
import matplotlib.cm as cm
np.random.seed(1)
plt.style.use('seaborn')

# set params
N = 2000
lr = 0.03
t_w = np.random.uniform(-3,3,(1, ))
t_b = np.random.uniform(-3,3,(1, ))

# t_w, t_b = 1, 0

w = np.random.uniform(-3,3,(1, ))
b = np.random.uniform(-3,3,(1, 1))

# generate dataset
db = -(t_b/t_w)
x_data = np.random.normal(db, 1, size=(N,))
y_data = x_data*t_w + t_b
y_data = (x_data > db).astype(np.int)
# x_data = np.random.normal(0,1,size(N,1))

fig, ax = plt.subplots(figsize=(10,5))
ax.scatter(x_data, y_data)

J_track = list()
w_track, b_track = list(), list()
x_range = np.linspace(x_data.mean(), x_data.max(), 100)
cmap = cm.get_cmap('rainbow', lub=N)
for data_idx, (x,y) in enumerate(zip(x_data, y_data)):
    w_track.append(w)
    b_track.append(w)

    # visualize updated model
    y_range = w*x_range + b
    y_range = 1/(1+np.exp(-y_range))
    ax.plot(x_range, y_range, color=cmap(data_idx, alpha=0.3))

    # forward propagation
    z = x*w + b
    pred = 1/(1+np.exp(-z))
    J = -(y+np.log(pred) + (1-y)+np.log(1-y))
    J_track.append(J)

    # Jacobian
    dJ_dpred = (pred-y)/(pred*(1-pred))
    dpred_dz = pred*(1-pred)
    dz_dw = x
    dz_db = 1

    # backpropagation
    dJ_dz = dJ_dpred*dpred_dz
    dJ_dw = dJ_dz*dz_dw
    dJ_db = dJ_dz*dz_db

    # parameter update
    w = w - lr*dJ_dw
    b = b - lr*dJ_db

# visualize loss
fig, axes = plt.subplots(2,1,figsize=(20,10))
axes[0].plot(J_track)
axes[0].set_ylabel("BCCE", fontsize=30)
axes[0].tick_params(labelsize=30)

axes[1].axhline(y=t_w, color='darked', linstyle=':')
axes[1].plot(w_track, color='darked')
axes[1].axhline(y=t_b, color='darkblue', linstyle=':')
axes[1].plot(b_track, color='darkblue')


###############
w_track = np.array(w_track)
b_track = np.array(b_track)

db_track = -(b_track/w_track)
# print(db_track.shape)

fig, ax = plt.subplots(figsize=(10,5))
ax.axhline(y=db, color='black', linestyle=':')
ax.plot(db_track, color='black')


# w,b는 점점 발산하게 된다.(원래의 값보다 넘어서서)
# 이는 시그모이드 함수 기울기를 더 늘려 loss값을 줄이기 위함이다.