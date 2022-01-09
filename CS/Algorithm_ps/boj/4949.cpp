#include <bits/stdc++.h>
using namespace std;



int main(){
    while(true){
        string str, ans = "yes";
        stack<char> st;
        getline(cin, str);
        if(str == ".")break;

        for(auto c: str){
            if(c == '(' || c == '[')st.push(c);
            else if(c == ')'){
                if(st.empty()){ ans = "no"; break;}
                char cur = st.top(); st.pop();
                if(cur != '('){
                    ans = "no"; break;
                }
            }
            else if(c == ']'){
                if(st.empty()){ ans = "no"; break;}
                char cur = st.top(); st.pop();
                if(cur != '['){
                    ans = "no"; break;
                }
            }
        }
        if(!st.empty())ans = "no";
        cout << ans << '\n';
    }
    return 0;
}