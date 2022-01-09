import numpy as np
import tensorflow as tf

import matplotlib.pyplot as plt
plt.style.use('seaborn')

p_np = np.linspace(0.01, 0.99, 100)
p_tf = tf.linspace(0.01, 0.09, 100)

# print(p_np)
# print(p_tf)

odds_np = p_np/(1-p_np)
odds_tf = p_tf/(1-p_tf)

logit_np = np.log(odds_np)
logit_tf = tf.math.log(odds_tf)

fig, axes = plt.subplots(2,1, figsize=(15,10),
                        sharex=True)
axes[0].plot(p_np, odds_np)
axes[1].plot(p_tf, odds_tf)

xticks = np.arange(0, 1.1, 0.2)
axes[0].tick_params(labelsize=15)
axes[0].set_xticks(xticks)
axes[0].set_ylabel('Odds', fontsize=20, color='darkblue')
axes[1].tick_params(labelsize=15)
axes[1].set_xticks(xticks)
axes[1].set_ylabel('Logits', fontsize=20, color='darkblue')