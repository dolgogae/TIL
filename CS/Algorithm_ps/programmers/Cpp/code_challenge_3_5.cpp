#include <bits/stdc++.h>
using namespace std;

int solution(int n) {
    int answer = 0;

    for(int i=2; i<n; i++){
        if(n%i==1){
            answer = i;
            break;
        }
    }

    return answer;
}

int main(){
    return 0;
}