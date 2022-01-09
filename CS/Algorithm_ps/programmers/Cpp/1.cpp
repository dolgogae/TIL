#include <bits/stdc++.h>
using namespace std;

const int MAX_VAL = 7;

vector<int> solution(vector<int> lottos, vector<int> win_nums) {
     vector<int> answer;

     int same_num = 0;
     int unknown = 0;
     sort(lottos.begin(), lottos.end());
     sort(win_nums.begin(), win_nums.end());

     for(auto lotto: lottos){
         if(!lotto){
             unknown++;
             continue;
         }
         int search_num = lotto;
         int search_idx = lower_bound(win_nums.begin(), win_nums.end(), search_num) - win_nums.begin();
         if(win_nums[search_idx] == search_num){
             same_num++;
         }
     }

     int Max = MAX_VAL - (same_num + unknown);
     int Min = MAX_VAL - same_num;

     Max = Max==7?6:Max;
     Min = Min==7?6:Min;

     answer.push_back(Max);
     answer.push_back(Min);

     return answer;
 }
