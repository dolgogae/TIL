#include <bits/stdc++.h>
using namespace std;

int R, C;   
const int dr[4]={-1,1,0,0};
const int dc[4]={0,0,-1,1};

struct node{
    int r,c;
};

queue<node> qa, qb;
string mat[1504];
int visit[1504][1504];

int meltIce(queue<node> &q, int num){
    queue<node> nextQ;
    while(!q.empty()){
        int r = q.front().r;
        int c = q.front().c;

        q.pop();

        if(mat[r][c] == 'L'){
            return 0;
        }

        if(visit[r][c]+num == 3)return 1;

        for(int i=0; i<4; i++){
            int nr = r+dr[i];
            int nc = c+dc[i];
            if(0<=nr && nr<R && 0<=nc && nc<C && visit[nr][nc]!=num){
                if(mat[nr][nc] == '.'){
                    q.push({nr,nc});
                    visit[nr][nc]=num;
                }
                if(mat[nr][nc] == 'X'){
                    nextQ.push({nr, nc});
                    visit[nr][nc]=num;
                }
            }
        }
    }
    q = nextQ;
    while(!nextQ.empty()){
        int r = nextQ.front().r;
        int c = nextQ.front().c;

        nextQ.pop();

        mat[r][c] = '.';
    }
    return 0;
}

int main(){
    scanf("%d %d",&R,&C);
    for(int i=0; i<R; i++)
        cin >> mat[i];

    int flag=1;
    for(int i=0; i<R; i++){
        for(int j=0; j<C; j++){
            if(mat[i][j] == 'L'){
                if(flag){
                    qa.push({i,j});
                    visit[i][j]=1;
                    flag=0;
                }
                else {
                    qb.push({i,j});
                    visit[i][j]=2;
                }
                mat[i][j]='.';
            }
        }
    }

    int day=0;
    while(true){
        if(meltIce(qa,1))break;
        if(meltIce(qb,1))break;
        day++;
    }
    printf("%d",day);
    return 0;
}