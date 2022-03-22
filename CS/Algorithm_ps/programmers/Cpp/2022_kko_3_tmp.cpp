#include <bits/stdc++.h>
using namespace std;

struct node{ int times; string num; string status; };

vector<node> records_tmp; // num, state
map<string, node> car_info;
map<string, int> parking_time;


vector<string> split(string str, char delimeter){
    vector<string> answer;
    stringstream ss(str);
    string tmp;

    while(getline(ss, tmp, delimeter)){
        answer.push_back(tmp);
    }
    return answer;
}

int calcFee(int minute, vector<int> fees){
    int minute_fee = (minute - fees[0])/fees[2];
    int logit = (minute - fees[0])%fees[2];

    cout << minute_fee << '\n';

    int ret = fees[1] + ((minute_fee>0?minute_fee:0) + (logit>0?1:0)) * fees[3];
    return ret;
}

vector<int> solution(vector<int> fees, vector<string> records) {
    vector<int> answer;

    // record split + 시간을 분으로(int)
    for(auto record: records){
        vector<string> v = split(record, ' ');

        vector<string> time_string = split(v[0], ':');
        int times = stoi(time_string[0]) * 60 + stoi(time_string[1]);

        records_tmp.push_back({times, v[1], v[2]});
    }

    // 사용자 시간 더하기

    for(auto record: records_tmp){
        if(record.status == "OUT"){
            int minute = record.times - car_info[record.num].times;
            // cout << "min "<< record.first << ' ' << minute << '\n';
            parking_time[record.num] += minute;
        }
        car_info[record.num] = record;
    }

    // 나가지 않은 사람 시간 더하기
    for(auto &car: car_info){
        if(car.second.status == "IN"){
            int minute = 1439 - car.second.times;
            parking_time[car.first] += minute;
        }
    }

    // 요금 계산

    for(auto &fee : parking_time){
        // cout << fee.first << ' '<<fee.second << '\n';
        int ans = calcFee(fee.second, fees);
        answer.push_back(ans);
        // cout << ans << '\n';
    }

    return answer;
}

int main(){
	vector<int> fees = {180, 5000, 10, 600};
	vector<string> records = {"05:34 5961 IN", "06:00 0000 IN", "06:34 0000 OUT", "07:59 5961 OUT", "07:59 0148 IN", "18:59 0000 IN", "19:09 0148 OUT", "22:59 5961 IN", "23:00 5961 OUT"};
	vector<int> ans = solution(fees, records);
	// cout << ans[0] << '\n';
	return 0;
}
