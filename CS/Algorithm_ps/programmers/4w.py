def solution(table, languages, preference):
    answer = ''
    jobs = {}
    for job in table:
        lang = list(job.split())
        jobs[lang[0]] = lang[1:]
    
    maxV = 0
    for job, prior in jobs.items():
        score = 0
        for idx, language in enumerate(languages):
            score += preference[idx] * (5 - (prior.index(language) if language in prior else 5))
        if maxV <= score:
            if maxV == score:
                answer = answer if job > answer else job
            else :
                maxV= score
                answer = job

    return answer

def solution(table, languages, preference):
    score = {}
    for t in table:
        for lang, pref in zip(languages, preference):
            if lang in t.split():
                score[t.split()[0]] = score.get(t.split()[0], 0) +  (6 - t.split().index(lang)) * pref
    return sorted(score.items(), key = lambda item: [-item[1], item[0]])[0][0]



table = ["SI JAVA JAVASCRIPT SQL PYTHON C#", "CONTENTS JAVASCRIPT JAVA PYTHON SQL C++", "HARDWARE C C++ PYTHON JAVA JAVASCRIPT", "PORTAL JAVA JAVASCRIPT PYTHON KOTLIN PHP", "GAME C++ C# JAVASCRIPT C JAVA"]	
languages = ["PYTHON", "C++", "SQL"]
preference = [7,5,5]

print(solution(table, languages, preference))
