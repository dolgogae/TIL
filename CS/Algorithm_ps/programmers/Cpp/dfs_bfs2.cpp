#include <bits/stdc++.h>
using namespace std;

int n;
int visited[203];

vector<int> graph[203];

void dfs(int cur){
    // cout << cur <<'\n';
    for(int i=0;i<graph[cur].size(); i++){
        // cout << "## "<< graph[cur][i] <<'\n';
        if(!visited[graph[cur][i]]){
            // cout << "ssss" <<'\n';
            visited[graph[cur][i]]=1;
            dfs(graph[cur][i]);
        }
    }
}

int solution(int n, vector<vector<int>> computers) {
    int answer = 0;
    n = computers.size();
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            if(i == j)continue;

            if(computers[i][j]){
                graph[i].push_back(j);
                // graph[j].push_back(i);
            }
        }
    }

    for(int i=0; i<n; i++){
        if(!visited[i]){
            // cout << "start: "<< i<< '\n';
            visited[i]=1;
            dfs(i);
            answer++;
        }
    }

    return answer;
}

int main(){

    vector<vector<int>> computers = {{1, 1, 0}, {1, 1, 1}, {0, 1, 1}};
    cout << solution(3, computers) <<'\n';
    return 0;
}