#include <bits/stdc++.h>
using namespace std;

int ans[27];

int main(){
    string str;
    cin >> str;
    for(auto c: str){
        ans[c-'a']++;
    }
    for(int i=0; i<26; i++)
        printf("%d ", ans[i]);
    printf("\n");
}