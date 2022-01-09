#include <bits/stdc++.h>
using namespace std;

int table[60007];
struct node{
    int s, e;
};
vector<node> logsN;
int st, play_timeN, adv_timeN;

int encoding(string str){
    vector<int> num;
    int curN = 0, ret = 0;
    str.push_back(':');
    for(auto c: str){
        if(c == ':'){
            num.push_back(curN);
            curN = 0;
        }
        curN = curN*10+(c-'0');
    }
    ret = num[0]*6000 + num[1]*100 + num[2];
    return ret;
}

node logEncoding(string str){
    node ret;
    string s,e;
    int i, len = str.size();
    for(i=0; ; i++){
        if(str[i] == '-')break;
        s.push_back(str[i]);
    }
    for(int j=i+1; j<len; j++)
        e.push_back(str[j]);
    ret.s = encoding(s);
    ret.e = encoding(e);
    return ret;
}

void calcScore(int s, int &Max){
    if(s + adv_timeN > play_timeN)return;

    int cur = table[s + adv_timeN] - table[s];
    if(Max<cur){
        Max = cur;
        st = s;
    }
}

string decoding(){
    string ms = to_string(st % 100);
    st /= 100;
    string m = to_string(st / 60);
    string s = to_string(st % 60);
    string ret = m + ':' + s + ':' + ms;
    return ret;
}

string solution(string play_time, string adv_time, vector<string> logs) {
    string answer = "";

    play_timeN = encoding(play_time);
    adv_timeN = encoding(adv_time);
    for(auto log : logs){
        node t = logEncoding(log);
        logsN.push_back(t);
        table[t.s] = 1, table[t.e] = -1;
    }
    for(int i=1; i<play_timeN; i++)
        table[i] += table[i-1];
    
    for(int i=1; i<play_timeN; i++)
        table[i] += table[i-1];
    
    int Max = -1;
    for(auto log: logsN){
        calcScore(log.s, Max);
        calcScore(log.e-adv_timeN, Max);
    }
    answer = decoding();
    return answer;
}
