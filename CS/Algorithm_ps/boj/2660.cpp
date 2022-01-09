#include <bits/stdc++.h>
#define INF 987654321
using namespace std;

int dist[505][505];
int n;
vector<int> ans;

void sol(){
    for(int i=1; i<=n; i++){
        for(int u=1; u<=n; u++){
            for(int v=1; v<=n; v++){
                if(dist[u][v] > dist[u][i]+dist[i][v])
                    dist[u][v] = dist[u][i]+dist[i][v];
            }
        }
    }
    vector<int> score;
    for(int i=1; i<=n; i++){
        int Max=-1;
        for(int j=1; j<=n; j++){
            Max = max(Max, dist[i][j]);
        }
        score.push_back(Max);
    }

    int candScore = score[min_element(score.begin(), score.end())-score.begin()];
    for(int i=0; i<n; i++){
        if(score[i] == candScore)
            ans.push_back(i);
    }

    printf("%d %d\n", candScore, ans.size() );
    for(auto i: ans)
        printf("%d ", i+1);
    printf("\n");
}

int main(){
   
    scanf("%d", &n);
    for(int i=1; i<=n; i++){
        for(int j=0; j<=n; j++)dist[i][j] = INF;
        dist[i][i] = 0;
    }
    while(true){
        int u, v;   scanf("%d %d", &u, &v);
        if(u+v == -2) break;
        dist[u][v] = dist[v][u] = 1;
    }
    sol();
    return 0;
}