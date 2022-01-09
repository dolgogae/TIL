#include <bits/stdc++.h>
#define INF 987654321
#define MAXN 200
#define TEMP 0

using namespace std;

#if TEMP==1

int S, A, B;
struct node {
    int v, e;
};
int dist[203];
vector<node> graph[203];
int calcDist(int cur, int ep){
    for(int i=0; i<203; i++)
        dist[i] = INF;
    queue<int> q;
    q.push(cur);
    dist[cur] = 0;
    while(!q.empty()){
        int v = q.front();

        q.pop();
        if(v == ep)continue;

        for(auto next : graph[v]){
            int nv = next.v;
            int ne = next.e;
            if(dist[nv] > dist[v]+ne){
                dist[nv] = dist[v] + ne;
                q.push(nv);
            }
        }
    }
    dist[ep] = dist[ep] == INF ? 100005 : dist[ep];
    return dist[ep];
}

int solution(int n, int s, int a, int b, vector<vector<int>> fares) {
    int answer = INF;
    int len = fares.size();
    S = s, A = a, B = b;
    for(auto vert : fares){
        graph[vert[0]].push_back({vert[1], vert[2]});
        graph[vert[1]].push_back({vert[0], vert[2]});
    }

    for(int i=1; i<=n; i++){
        int curDist = calcDist(i, S)+calcDist(i, A)+calcDist(i, B);
        answer = min(answer, curDist);
    }

    return answer;
}

#else

int dist[MAXN + 1][MAXN + 1];
int solution(int n, int s, int a, int b, vector<vector<int>> fares) {
    int ans = INF;
    for(int s = 1; s <= n; ++s)
        for(int e = 1; e <= n; ++e){
            if(s == e) dist[s][e] = 0;
            else dist[s][e] = INF;
        }
    for(int j = 0; j < fares.size(); ++j){
        int v = fares[j][0], u = fares[j][1], c = fares[j][2];
        dist[v][u] = dist[u][v] = c;
    }
    for(int u = 1; u <= n; ++u)
        for(int s = 1; s <= n; ++s)
            for(int e = 1; e <= n; ++e)
                if(dist[s][u] < INF && dist[u][e] < INF)
                    if(dist[s][u] + dist[u][e] < dist[s][e])
                        dist[s][e] = dist[s][u] + dist[u][e];
    for(int u = 1; u <= n; ++u){
        int cost = dist[s][u] + dist[u][a] + dist[u][b];
        if(cost < ans) ans = cost;
    }
    return ans;
}

#endif
