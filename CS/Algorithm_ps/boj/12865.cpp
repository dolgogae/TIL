#include <bits/stdc++.h>
using namespace std;
int n,k;
int dp[100005];
int main(){
    scanf("%d%d",&n,&k);
    for(int i=0;i<n;i++){
        int w,v;
        scanf("%d%d",&w,&v);
        for(int j=k;j>=w;j--)
            dp[j]=max(dp[j],dp[j-w]+v);
    }
    printf("%d\n",dp[k]);
    return 0;
}