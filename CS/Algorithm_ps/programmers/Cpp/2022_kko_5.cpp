#include <bits/stdc++.h>

using namespace std;

struct node{
    int cur;
    int sheep;
    int wolf;
    int status;
};

vector<int> Info;
vector<int> graph[20];

bool visited[20][1<<17];


int solution(vector<int> info, vector<vector<int>> edges) {
    int answer=0;
    Info = info;

    for(auto edge : edges){
        graph[edge[0]].push_back(edge[1]);
        graph[edge[1]].push_back(edge[0]);
    }

    queue<node> q;
    q.push({0,1,0,1});
    visited[0][1]=1;

    while(!q.empty()){
        node p = q.front(); q.pop();

        // cout << p.cur << ' ' << p.sheep << ' '<< p.wolf << ' ' << p.status <<'\n';

        answer = max(answer, p.sheep);

        for(int np : graph[p.cur]){

            int ns = 0, nw = 0;
            if(!(p.status & (1<<np))){
                if(!Info[np])ns++;
                else nw++;
            }
            int nstatus = p.status|(1<<np);

            if(Info[np] && p.sheep <= p.wolf + nw) continue;
            if(visited[np][nstatus])continue;
            
            visited[np][nstatus]=1;
            q.push({np, p.sheep + ns, p.wolf + nw, nstatus});
        }
    }

    return answer;
}

int main(){
    vector<int> info = {0,1,0,1,1,0,1,0,0,1,0};
    vector<vector<int> > edges = {{0,1},{0,2},{1,3},{1,4},{2,5},{2,6},{3,7},{4,8},{6,9},{9,10}};
    cout << solution(info, edges) << '\n';

    return 0;
}

    // vector<int> info = {0,0,1,1,1,0,1,0,1,0,1,1};
    // vector<vector<int> > edges = {{0,1},{1,2},{1,4},{0,8},{8,7},{9,10},{9,11},{4,3},{6,5},{4,6},{8,9}};
    