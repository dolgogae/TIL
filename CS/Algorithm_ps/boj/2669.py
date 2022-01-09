import sys

TEMP = 0

if TEMP == 0:

    mat = [[0] * 100 for _ in range(100)]
    
    for _ in range(4):
        sx, sy, ex, ey = map(int, sys.stdin.readline().split())
    
        for i in range(sx, ex):
            for j in range(sy, ey):
                mat[i][j] = 1
    
    ans = 0
    for i in range(100):
        ans += mat[i].count(1)
    #    for j in range(100):
    #        if mat[i][j] == 1:
    #            ans += 1
    print(ans)

else:
    li = []
    
    for i in range(4):
        x1, y1, x2, y2 = list(map(int, sys.stdin.readline().split()))
        
        for j in range(x1, x2):
            for k in range(y1, y2):
                li.append([j, k])
    
    li = list(set(tuple(i) for i in li))
    
    print(len(li))
