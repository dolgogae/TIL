#include <bits/stdc++.h>
using namespace std;

string solution(string new_id){
    for(char& ch : new_id){
        if('A' <= ch && ch <= 'Z') ch |= 32;
    }

    string ret;
    ret.clear();
    for(char& ch : new_id){
        if('a' <= ch && ch <= 'z' ||
            '0' <= ch && ch <= '9' ||
            strchr("_.-", ch))ret += ch;
    }

    new_id = ret;
    ret.clear();
    for(char& ch : new_id){
        if(!ret.empty() && ret.back() && ch =='.') continue;
        ret += ch;
    }

    if(ret.front() == '.')ret.erase(0,1);
    if(ret.back() == '.')ret.pop_back();

    if(ret.empty())ret = "a";
    if(ret.size() >= 16) ret = ret.substr(0,15);
    if(ret.back() == '.') ret.pop_back();
    while(ret.size()<3)ret+=ret.back();

    return ret;
}

int main(){
    string new_id;
    cin >> new_id;
    new_id = solution(new_id);
    cout << new_id << '\n';
    return 0;
}