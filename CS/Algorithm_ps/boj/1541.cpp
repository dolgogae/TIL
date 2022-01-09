#include <bits/stdc++.h>
using namespace std;

string str;
vector<int> num;

int sol(){
    int ret = 0;
    int cur = 0, curN = 0;
    int len = str.size();
    for(int i=len-1; i>=0; i--){
        if(str[i]=='-' || str[i]=='+'){
            str.push_back(str[i]);
            break;
        }
    }

    if(len == str.size())str.push_back('+');

    for(int i=0; i<=len ; i++){
        if('0'<= str[i] && str[i] <= '9')curN = curN*10+(str[i]-'0');
        if(str[i] == '+'){
            cur+=curN;
            if(i == len)num.push_back(cur);
            curN=0;
        }
        if(str[i] == '-'){
            cur+=curN;
            num.push_back(cur);
            cur=curN=0;
        }
    }

    if(str[0] == '-')ret = num[0]*(-1);
    else ret = num[0];
    len = num.size();
    for(int i=1; i<len; i++){
        ret-=num[i];
    }
    return ret;
}

int main(){
    cin >> str;
    cout << sol() << '\n';
    return 0;
}