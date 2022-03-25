#include <bits/stdc++.h>
using namespace std;

vector<string> Enroll;
unordered_map<string, int> enroll_hash;
vector<int> tree[10002];
unordered_map<string, int> money;

void dfs(int cur, int cash){
    
    // 부모가 없을때(민호)

    // 1/10이 (cash%10 == 0)일때 더이상 안가고
    int n_cash = cash - (cash/10);
    money[Enroll[cur]] += n_cash;
    
    // cout << cur <<' '<< n_cash<<'\n';

    if(tree[cur].size()) {
        int next = tree[cur][0];
        if(n_cash != cash)dfs(next, cash/10);
    }
}

vector<int> solution(vector<string> enroll, vector<string> referral, vector<string> seller, vector<int> amount) {
    vector<int> answer;
    // 조직원-> hashing
    enroll.push_back("-");
    Enroll = enroll;
    for(int i=0; i<enroll.size(); i++)
        enroll_hash[enroll[i]] = i;

    // cout << "secceed\n";
    // 부모를 가지고 있는 tree 완성
    for(int i=0; i<enroll.size()-1; i++){
        int u = enroll_hash[enroll[i]];
        int v = enroll_hash[referral[i]];

        // cout << u <<' '<< v <<'\n';
        tree[u].push_back(v);
    }


    // seller, amount : dfs 순회하며 돈 업데이트 dfs(cur, cash)
    for(int i=0; i<seller.size(); i++){
        int cur = enroll_hash[seller[i]];
        int cash = amount[i]*100;

        // cout <<"===\n";
        dfs(cur, cash);
    }

    for(int i=0; i<enroll.size()-1; i++){
        answer.push_back(money[enroll[i]]);
    }

    return answer;
}

int main(){
    vector<string> enroll = {"john", "mary", "edward", "sam", "emily", "jaimie", "tod", "young"};
    vector<string> referral = {"-", "-", "mary", "edward", "mary", "mary", "jaimie", "edward"};
    vector<string> seller = {"young", "john", "tod", "emily", "mary"};
    vector<int> amount = {12, 4, 2, 5, 10};

    vector<int> answer = solution(enroll, referral, seller, amount);
    for(auto ans: answer){
        cout << ans <<' ';
    }
    cout << '\n';
    return 0;
}