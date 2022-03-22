#include <bits/stdc++.h>
using namespace std;

int max_diff;
int check;
vector<int> ans = {0,0,0,0,0,0,0,0,0,0,0};

int calcDiff(vector<int> apeach, vector<int> lion){
    int p = 0, l = 0;
    for(int i=0; i<=10; i++){
        if(apeach[i]==0 && lion[i]==0)continue;

        if(apeach[i] >= lion[i])p += (10-i);
        else l += (10-i);
    }
    return l-p;
}

bool checkLowNumber(vector<int> lion){
    for(int i=10; i>=0; i--){
        // cout <<  lion[i] << ' '<< ans[i]<<'\n';
        if(lion[i] > ans[i]) return true;
        else if (lion[i] < ans[i]) return false;
    }
    return true;
}

void dfs(int cur, int arrow, vector<int> apeach, vector<int> lion){
    if(arrow < 0) return;

    if(cur == 11){
        lion[10] += arrow;
        int diff = calcDiff(apeach, lion);
        // cout << diff << '\n';
        if(max_diff < diff || ((max_diff == diff) && checkLowNumber(lion))){
            // for(int i=10; i>=0; i--){
            //     cout << lion[i] <<' '; 
            // }
            // cout << '\n';
            
            max_diff = diff;
            ans = lion;
            check = 1;
        }
        return;
    }

    dfs(cur+1, arrow, apeach, lion);
    int use_arrow = apeach[cur] + 1;
    lion[cur] = use_arrow;
    dfs(cur+1, arrow-use_arrow, apeach, lion);
}

vector<int> solution(int n, vector<int> info) {

    vector<int> lion = {0,0,0,0,0,0,0,0,0,0,0};
    dfs(0, n, info, lion);

    if(!max_diff)ans={-1};
    return ans;
}


int main(){
    int n = 5;
    vector<int> info = {2,1,1,1,0,0,0,0,0,0,0};
    vector<int> answer = solution(n, info);

    for(auto i: answer){
        cout << i << '\n';
    }
    return 0;
}