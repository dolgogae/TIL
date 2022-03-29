#include <bits/stdc++.h>
#define INF 20000005
#define MAXN 200

using namespace std;


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


int main(){

    vector<vector<int> > fares = {{4, 1, 10}, 
                                  {3, 5, 3}, 
                                  {5, 6, 2}, 
                                  {3, 1, 41}, 
                                  {5, 1, 24}, 
                                  {4, 6, 1}, 
                                  {2, 4, 66}, 
                                  {2, 3, 4}, 
                                  {1, 6, 25}};

    cout << solution(6,4,6,2,fares) << '\n';

    return 0;
}