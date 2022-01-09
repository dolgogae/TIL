import random
import multiprocessing

def compute(n):
     return sum([random.randint(1, 100) for i in range(1000000)])

pool = multiprocessing.Pool(precess=8)
print("Results: %s" % pool.map(compute, range(8)))