#include <iostream>
#include <unordered_map>
#include <vector>
#include <string>
#include <sstream>
using namespace std;

unordered_map<string, string> um;

vector<string> solution(vector<string> record) {
    vector<string> answer;
    vector<vector<string> > mat;

    for (auto i : record) {
        vector<string> v;
        istringstream ss(i);
        string stringBuffer;
        while (getline(ss, stringBuffer, ' '))
            v.push_back(stringBuffer);
        
        mat.push_back(v);
        if (v[0] == "Enter")         um[v[1]] = v[2];
        else if (v[0] == "Change")   um[v[1]] = v[2];
    }

    for (auto i : mat) {
        if (i[0] == "Enter") {
            answer.push_back(um[i[1]] + "님이 들어왔습니다.");
        }
        else if(i[0] == "Leave") {
            answer.push_back(um[i[1]] + "님이 나갔습니다.");
        }
    }

    return answer;
}

int main() {
    vector<string> record = { "Enter uid1234 Muzi", "Enter uid4567 Prodo", "Leave uid1234", "Enter uid1234 Prodo", "Change uid4567 Ryan" };

    vector<string> answer = solution(record);

    for (auto i : answer)
        cout << i << endl;

   return 0;
}
