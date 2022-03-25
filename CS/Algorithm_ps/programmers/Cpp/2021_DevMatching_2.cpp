#include <bits/stdc++.h>
using namespace std;

int mat[102][102];

vector<int> solution(int rows, int columns, vector<vector<int>> queries) {
    vector<int> answer;

    for(int i=1; i<=rows; i++)for(int j=1; j<=columns; j++)
        mat[i][j] = (i-1)*columns + j;
    
    for(auto query: queries){
        int x = query[0], y = query[1], a = query[2], b = query[3];
        int start = mat[x][b];

        int num = start;
        for(int i = b; i>=y+1; i--) {mat[x][i] = mat[x][i-1]; num = min(num, mat[x][i]);}
        for(int i = x; i<=a-1; i++) {mat[i][y] = mat[i+1][y]; num = min(num, mat[i][y]);}
        for(int i = y; i<=b-1; i++) {mat[a][i] = mat[a][i+1]; num = min(num, mat[a][i]);}
        for(int i = a; i>=x+2; i--) {mat[i][b] = mat[i-1][b]; num = min(num, mat[i][b]);}
        mat[x+1][b] = start;

        answer.push_back(num);
    }
    

    return answer;
}

int main(){
    vector<vector<int>> queries = {{2,2,5,4},{3,3,6,6},{5,1,6,3}};
    vector<int> ans = solution(6,6, queries);

    for(auto i:ans)cout << i << ' ';
    cout <<'\n';
    return 0;
}



        // for(int i=1; i<=rows; i++){
        //     for(int j=1; j<=columns; j++)
        //         cout << mat[i][j] << '\t';
        //     cout << '\n';
        // }
        // cout << '\n';