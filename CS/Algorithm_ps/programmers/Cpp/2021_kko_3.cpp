#include <bits/stdc++.h>
using namespace std;

vector<int> scoreBoard[4000];

vector<string> strSplit(string str){
    vector<string> ret;
    string cur = "";
    int len = str.size();
    for(auto ch: str){
        if(ch == ' '){
            if(cur == "and"){
                cur = "";   
                continue;
            }
            ret.push_back(cur);
            cur = "";
            continue;
        }
        cur += ch;
    }
    ret.push_back(cur);
    return ret;
}

int check;
int answer;
void pushScore(int cur, int idx, int score, vector<string> applier){
    if(cur == 4){
        // printf("%d\n", idx);
        if(check){
            int len = scoreBoard[idx].size();
            int ans = lower_bound(scoreBoard[idx].begin(), scoreBoard[idx].end(), score) - scoreBoard[idx].begin();
            answer += (len - ans);
        }
        else scoreBoard[idx].push_back(score);
        return;
    }

    if(applier[cur] == "-"){
        pushScore(cur+1, idx*10+1, score, applier);
        pushScore(cur+1, idx*10+2, score, applier);
        if(cur==0) pushScore(cur+1, idx*10+3, score, applier);
    }

    else if(applier[cur] == "cpp" || applier[cur] == "backend" || 
            applier[cur] == "junior" || applier[cur] == "chicken"){
        pushScore(cur+1, idx*10+1, score, applier);
    }
    else if(applier[cur] == "java" || applier[cur] == "frontend" || 
            applier[cur] == "senior" || applier[cur] == "pizza"){
        pushScore(cur+1, idx*10+2, score, applier);
    }
    else pushScore(cur+1, idx*10+3, score, applier);
    
}

vector<int> solution(vector<string> info, vector<string> query){
    vector<int> ret;
    for(auto appliers : info){
        vector<string> cur = strSplit(appliers);

        int score = stoi(cur[4]);
        pushScore(0,0,score,cur);
    }

    for(int i=0; i<4000; i++)
        sort(scoreBoard[i].begin(), scoreBoard[i].end());
    
    check=1;
    for(auto q: query){
        vector<string> cur = strSplit(q);

        int score = stoi(cur[4]);
        answer = 0;
        pushScore(0,0,score,cur);
        ret.push_back(answer);
    }
    return ret;
}
