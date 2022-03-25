#include <bits/stdc++.h>
using namespace std;

unordered_map<string, int> menus;
int maxL;
int visited[12];
string curStr;

void dfs(int cur, string str){
    if(str.size() == maxL){
        menus[str]++;
        return;
    }
    if(maxL < str.size())return;

    for(int i = cur; i < curStr.size(); i++){
        if(!visited[i]){
            visited[i] = 1;
            str += curStr[i];
            dfs(i+1, str);
            visited[i] = 0;
            str.pop_back();
        }
    }
}

vector<string> solution(vector<string> orders, vector<int> course) {
    vector<string> answer;

    for(auto c: course){
        maxL = c;
        menus.clear();
        for(auto order: orders){
            curStr = order;
            sort(curStr.begin(), curStr.end());
            memset(visited, 0, sizeof(visited));
            dfs(0,"");
        }

        int Max = 0;
        for(auto menu: menus){
            Max = max(Max, menu.second);
        }
        
        if(Max < 2)continue;
        for(auto menu:menus){
            if(menu.second == Max) {
                answer.push_back(menu.first);
            }
        }
    }

    sort(answer.begin(), answer.end());

    return answer;
}

int main(){

    vector<string> orders = {"ABCFG", "AC", "CDE", "ACDE", "BCFG", "ACDEH"};
    vector<int> course = {2,3,4};

    vector<string> ans  = solution(orders, course);

    for(auto a: ans)
        cout << a << ' ';
    cout << '\n';

    return 0;
}
