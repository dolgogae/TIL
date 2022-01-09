#import sys
#
#N,K=map(int,sys.stdin.readline().split())
#numbers = [[0,0]]
#
#for i in range (N):
#    numbers.append(list(map(int,sys.stdin.readline().split())))
#
#    dp_list = [[0]*(K+1) for _ in range(N+1)]
#
#for i in range(1, N+1):
#    for j in range (1, K+1):
#        w=numbers[i][0]
#        v=numbers[i][1]
#        if j>=w:
#            dp_list[i][j]=max(dp_list[i-1][j-w]+v,dp_list[i-1][j])
#        else:
#            dp_list[i][j]=dp_list[i-1][j]
#            print(dp_list[N][K])

import sys

N, K = map(int, sys.stdin.readline().split())
item = [[0,0]]

dp_list = [[0] * (K+1) for _ in range(N+1)]

for _ in range(N):
    item.append(list(map(int, sys.stdin.readline().split())))

for i in range(1, N+1):
    for j in range(1, K+1):
        w = item[i][0]
        v = item[i][1]
        if j < w:
            dp_list[i][j] = dp_list[i-1][j]
        else:
            dp_list[i][j] = max(v + dp_list[i-1][j-w], dp_list[i-1][j])

print(dp_list[N][K])
