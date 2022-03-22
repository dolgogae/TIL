#include <bits/stdc++.h>
using namespace std;

struct node{
	int times;
	string num;
	string state;
};

map<string, pair<int, string> > car_status;
map<string, int> parking_time;

vector<string> split(string str, char delimeter){
	vector<string> ans;
	stringstream ss(str);
	string tmp;

	while(getline(ss, tmp, delimeter))
		ans.push_back(tmp);
	return ans;
}

int calcFee(int minute, vector<int> fees){
	int minFee = (minute - fees[0])/fees[2];
	int modFee = (minute - fees[0])%fees[2];

	// cout << minFee <<' '<< modFee<< '\n';

	int cur_fee = fees[1] + (( minFee > 0 ? minFee : 0 ) + (modFee > 0 ? 1 : 0)) * fees[3];
	
	// cout << cur_fee << '\n';
	return cur_fee;
}

vector<int> solution(vector<int> fees, vector<string> records) {
    vector<int> answer;
	vector<node> records_tmp;
	vector<int> times;

	// records split

	for(auto record : records){
		vector<string> v = split(record, ' ');

		vector<string> t = split(v[0], ':');
		int times = stoi(t[0]) * 60 + stoi(t[1]);

		node cur = {times, v[1], v[2]};
		records_tmp.push_back(cur);
	}
	// record IN/OUT 정보 업데이트
	// IN: 1번 map update
	// OUT: 계산후 2번 업데이트 후 1번 업데이트


	for(auto record: records_tmp){
		if(record.state == "OUT"){	
			int minute = record.times - car_status[record.num].first;
			parking_time[record.num] += minute;
		}
		car_status[record.num] = {record.times, record.state};
	}

	// 상태가 out이 아닌 경우 23:59기준 업데이트df
	for(auto &car: car_status){
		if(car.second.second == "IN"){
			int minute = 1439 - car.second.first;
			parking_time[car.first] += minute;
		}
	}

	for(auto &car: parking_time){
		// cout << car.first << ' ' << car.second << '\n';
		int ans = calcFee(car.second, fees);
		answer.push_back(ans);
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
