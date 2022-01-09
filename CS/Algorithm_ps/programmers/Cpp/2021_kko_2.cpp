#include <string>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <iostream>

using namespace std;

int len;
unordered_map<string, int> h;
string origin;

void makeChar(int idx, string cur, int depth){
    if(idx == origin.size() && cur.size() < depth)return;
    if(cur.size() == depth){
        h[cur]++;
        return;
    }

    makeChar(idx+1, cur+origin[idx], depth);
    makeChar(idx+1, cur, depth);
}

vector<string> solution(vector<string> orders, vector<int> course) {
    vector<string> answer;
    for(auto i : course){
        h.clear();

        for(auto order: orders){
            sort(order.begin(),order.end());
            origin = order;
            makeChar(0, "", i);
        }

        int Max = 0;
        for(auto elem: h){
            Max = max(Max, elem.second);
        }
        if(Max < 2)continue;
        for(auto elem: h){
            if(elem.second == Max){
                answer.push_back(elem.first);
            }
        }
    }

    sort(answer.begin(), answer.end());
    return answer;
}

int main(){
    vector<string> orders;
    vector<int> course;
    int n; scanf("%d", &n);
    for(int i=0; i<n; i++){
        string str; cin >> str;
        orders.push_back(str);
    } 
    scanf("%d", &n);
    for(int i=0; i<n; i++){
        int m; scanf("%d", &m);
        course.push_back(m);
    }

    vector<string> answer = solution(orders, course);

    for(auto str: answer){
        cout<<str+'\n';
    }
    return 0;
}
