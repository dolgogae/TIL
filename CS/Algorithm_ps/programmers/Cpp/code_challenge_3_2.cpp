#include <bits/stdc++.h>
using namespace std;

const int dr[4] = {-1,0,1,0};
const int dc[4] = {0,1,0,-1};

int N, M;
int visited[502][502][4];

vector<int> solution(vector<string> grid) {
    vector<int> answer;
    N = grid.size();
    M = grid[0].size();

    for(int dir=0; dir<4; dir++){
        
        for(int r=0; r<N; r++)for(int c=0; c<M; c++){
            if(visited[r][c][dir])continue;

            int curR = r;
            int curC = c;
            int curD = dir;
            int num = 0;
            while(true){

                if(visited[curR][curC][curD]){
                    answer.push_back(num-visited[curR][curC][curD]);
                    break;
                }
                visited[curR][curC][curD] = num;

                // i?nt nd = curD, nr = curR, nc = curC;
                if(grid[curR][curC] == 'R')
                    curD = curD-1>=0?curD-1:3;
                else if(grid[curR][curC] == 'L')
                    curD = curD+1<=3?curD+1:0;
                
                curR = (curR + dr[curD] + N)%N;
                curC = (curC + dc[curD] + M)%M;
                num++;
            }
        }
    }

    sort(answer.begin(), answer.end());
    return answer;
}

int main(){
    vector<string> grid = {"R", "R"};
    vector<int> answer = solution(grid);

    for(auto ans:answer){
        cout << ans <<' ';
    }cout <<'\n';
    return 0;
}