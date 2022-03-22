#include <bits/stdc++.h>
using namespace std;
#define TEMP 0

// 내 풀이
#if TEMP==0
vector<string> split(string str, char deleimeter){
    vector<string> ans;
    stringstream ss(str);
    string tmp;

    while(getline(ss, tmp, deleimeter)){
        ans.push_back(tmp);
    }

    return ans;
}

vector<int> solution(vector<string> id_list, vector<string> report, int k) {
    vector<int> answer;
    vector<string> cands;
    map<string, set<string> > reported;
    map<string, set<string> > user_report;
    // 개인당 신고당한 횟수

    for(auto x: report){
        vector<string> ac = split(x, ' ');  
        reported[ac[1]].insert(ac[0]);
        user_report[ac[0]].insert(ac[1]);
    }

    // k이상 신고당한 사람

    for(auto rep: reported){
        if(rep.second.size() >= k) {
            cands.push_back(rep.first);
        }
    }

    // 내가 신고한 사람이 k이상 신고당한 사람인지 고르기
    for(auto id: id_list){
        int cnt = 0;
        for(auto cand: cands){
            if(user_report[id].find(cand) != user_report[id].end()){
                ++cnt;
            }
        }
        answer.push_back(cnt);
    }

    return answer;
}

#elif TEMP==1

#include <bits/stdc++.h>
#define fastio cin.tie(0)->sync_with_stdio(0)
using namespace std;

vector<int> solution(vector<string> id_list, vector<string> report, int k) {
    // 1.
    const int n = id_list.size();
    map<string, int> Conv;
    for (int i = 0; i < n; i++) Conv[id_list[i]] = i;

    // 2.
    vector<pair<int, int>> v;
    sort(report.begin(), report.end());
    report.erase(unique(report.begin(), report.end()), report.end());
    for (const auto& s : report) {
        stringstream in(s);
        string a, b; in >> a >> b;
        v.push_back({ Conv[a], Conv[b] });
    }

    // 3.
    vector<int> cnt(n), ret(n);
    for (const auto& [a, b] : v) cnt[b]++;
    for (const auto& [a, b] : v) if (cnt[b] >= k) ret[a]++;
    return ret;
}
#endif

int main(){
    vector<string> id_list = {"muzi", "frodo", "apeach", "neo"};
    vector<string> report = {"muzi frodo","apeach frodo","frodo neo","muzi neo","apeach muzi"};
    int k = 2;

    vector<int> answer = solution(id_list, report, k);

    for(auto ans: answer){
        cout << ans << ' ';
    }
    return 0;
}