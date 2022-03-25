#include <bits/stdc++.h>
using namespace std;

int mat[1003][1003];

int solution(vector<vector<int>> board, vector<vector<int>> skills) {
    int answer = 0;
    int n = board.size();
    int m = board[0].size();

    for(auto skill: skills){
        int type = skill[0], r1 = skill[1]+1, c1 = skill[2]+1, r2 = skill[3]+2, c2 = skill[4]+2, degree = skill[5];

        if(type == 1){
            mat[r1][c1] -= degree;
            mat[r2][c2] -= degree;
            mat[r1][c2] += degree;
            mat[r2][c1] += degree;
        }else{
            mat[r1][c1] += degree;
            mat[r2][c2] += degree;
            mat[r1][c2] -= degree;
            mat[r2][c1] -= degree;
        }
    }

    for(int i=1; i<=n; i++){
        for(int j=1; j<=m; j++){
            mat[i][j] = mat[i][j] + mat[i-1][j] + mat[i][j-1] - mat[i-1][j-1];
        }
    }

    for(int i=1; i<=n; i++){
        for(int j=1; j<=m; j++){
            if(mat[i][j] + board[i-1][j-1] > 0)answer++;
        }
    }

    return answer;
}

int main(){
    
    return 0;
}