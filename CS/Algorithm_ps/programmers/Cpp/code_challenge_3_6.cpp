#include <bits/stdc++.h>
using namespace std;

vector<int> solution(int n, long long left, long long right) {
    vector<int> answer;

    int leftR = left/n+1;
    int leftC = left%n;
    int rightR = right/n+1;
    int rightC = right%n;

    list<int> ans;

    for(int r = leftR; r<=rightR; r++){
        int cur = r;
        for(int i = 1; i<=n; i++){
            if(i>r)cur++;
            ans.push_back(cur);
        }
    }

    for(int i=0; i<leftC; i++)ans.pop_front();
    for(int i=0; i<n-1-rightC; i++)ans.pop_back();

    for(auto i : ans)answer.push_back(i);

    return answer;
}

vector<int> solution2(int n, long long left, long long right) {
    vector<int> answer;
    int row;
    int column;
    for(long long i = left; i <= right; i++){
        answer.push_back(max(i / n, i % n) + 1);
    }

    return answer;
}

int main(){

    vector<int> answer = solution(4,7,14);

    for(auto ans:answer){
        cout<< ans <<' ';
    }cout<<'\n';

    return 0;
}