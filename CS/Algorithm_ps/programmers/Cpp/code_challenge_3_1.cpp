#include <bits/stdc++.h>
using namespace std;

int solution(vector<int> numbers) {
    int answer = 0;
    int arr[10] = {0,};

    for(auto num: numbers){
        arr[num] = 1;
    }

    for(int i=0;i<10; i++){
        if(arr[i]==0)answer+=i;
    }

    return answer;
}

int main(){
    return 0;
}