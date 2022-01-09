#include <bits/stdc++.h>
using namespace std;

// unordered_map<string, int> um = 
//     {{"zero",0},{"one",1},{"two",2},
//     {"three",3},{"four",4},{"five",5},
//     {"six",6},{"seven",7},
//     {"eight",8},{"nine",9}};

// int solution(string s) {
//     int answer = 0;
//     string cur="";
//     for(auto i:s){
//         if('0'<=i&&i<='9'){
//             answer = answer * 10 + i-'0';
//             continue;
//         }
//         cur+=i;
//         if(um.find(cur)!=um.end()){
//             answer = answer * 10 + um[cur];
//             cur = "";
//         }
//     }
//     return answer;
// }

vector<string> v =  
    {"zero","one","two",
    "three","four","five",
    "six","seven","eight","nine"};

int solution(string s){
    for(int i=0; i<10; i++){
        s = regex_replace(s, regex(v[i]), to_string(i));
    }
    return stoi(s);
}


int main(){
    string s = "23four5six7";
    cout << solution(s) << endl;
    return 0;
}
