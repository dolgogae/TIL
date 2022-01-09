
def solution(price, money, count):
    answer = -1

    total = (count*(count+1))/2*price
    answer = total - money
    if answer < 0:
        answer = 0
    return answer
