def solution(scores):
    answer = ""
    size = len(scores)

    scores = list(map(list,zip(*scores)))

    for idx, score in enumerate(scores):
        slen = size
        avg = sum(score)
        if score[idx] == min(score) or score[idx] == max(score):
            if score.count(score[idx]) == 1:
                avg -= score[idx]
                slen -= 1

        avg = avg/slen

        if avg >=90:
            answer += 'A'
        elif avg >= 80:
            answer += 'B'
        elif avg >= 70:
            answer += 'C'
        elif avg >= 50:
            answer += 'D'
        else:
            answer += 'F'
    return answer


print(solution([[70,49,90],[68,50,38],[73,31,100]]))

