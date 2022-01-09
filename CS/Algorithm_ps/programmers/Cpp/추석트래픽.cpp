
#define TEMP 1

#if TEMP==0

#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
using namespace std;

vector<string> split(string input, char delimiter) {
	vector<string> ret;
	stringstream ss(input);
	string stringBuffer;

	while (getline(ss, stringBuffer, delimiter))
		ret.push_back(stringBuffer);
	return ret;
}

int timeToInt(string time) {
	int h = stoi(time.substr(0, 2));
	int m = stoi(time.substr(3, 2));
	int s = stoi(time.substr(6, 2));
	int ss = stoi(time.substr(9));
	return (h * 3600 + m * 60 + s) * 1000 + ss;
}

int ssToInt(string ss) {
	int len = ss.size();
	vector<string> v = split(ss, '.');
	string n = v[0];
	string e = "";
	if (v.size() == 1) n.pop_back();
	if (v.size() == 2) e = v[1].substr(0, v[1].size() - 1);

	while (e.size() < 3)
		e += '0';

	int ret = stoi(n + e);

	return ret;
}

int solution(vector<string> lines) {
	int answer = 0;
	vector<pair<int, int> > times;

	for (auto line : lines) {
		vector<string> time = split(line, ' ');
		int et = timeToInt(time[1]);
		int st = et - ssToInt(time[2]) + 1;

		times.push_back({ st, et });
	}

	int len = times.size();
	for (int i = 0; i < len; ++i) {
		int st = times[i].first;
		int et = times[i].second;
		int cnt = 0, isMax = 0;

		while (true) {
			if (st > et)st = et, isMax = 1;
			cnt = 0;
			for (int j = 0; j < len; ++j) {
				int flag = 0;
				if (st <= times[j].first && times[j].first < st + 1000) flag = 1;
				if (st <= times[j].second && times[j].second < st + 1000) flag = 1;
				if (flag) ++cnt;
			}
			answer = max(answer, cnt);

			if (st == et)break;
			if (isMax)break;
			st += 1000;
		}
	}

	return answer;
}

#elif TEMP==1
#include <iostream>
#include <string>
#include <vector>
#include <time.h>

using namespace std;

int solution(vector<string> lines) {
    int answer = 0;
    int tps = 0;
    vector<pair<double, double>> response;

    for (auto x : lines) {
        string ss = x.substr(11, 12);
        double responseCompleteTime = (stod(ss.substr(0, 2)) * 3600) + (stod(ss.substr(3, 2)) * 60) + stod(ss.substr(6, 2)) + (stod(ss.substr(9)) / 1000.0); // 응답완료시간

        string temp = x.substr(24);
        double throughputTime = stod(temp.substr(0, temp.length() - 1));

        double responseStartTime = responseCompleteTime - throughputTime + 0.001;

        response.push_back(make_pair(responseStartTime, responseCompleteTime));

    }

    for (int i = 0; i < response.size(); i++) {
        double start = response[i].first;
        double end = response[i].second;
        tps = 1;

        for (int j = i + 1; j < response.size(); j++) {
            if (start + 1 >= response[j].first || end + 1 > response[j].first) {

                tps++;
            }

        }

        answer = max(answer, tps);
    }

    return answer;
}

#else 

#include <string>
#include <vector>
#include <algorithm>
#include <iostream>
using namespace std;

#define BUFF_SIZE 86400000

int buff[BUFF_SIZE] = { 0 };

int solution(vector<string> lines) {
	int answer = 0;


	for (int i = 0; i < lines.size(); i++)
	{
		int end = 0;
		int elapsed_time;
		int y, m, d, hh, mm, ss, zzz;
		double elapsed_double;
		sscanf(lines[i].c_str(), "%d-%d-%d %d:%d:%d.%d %lfs", &y, &m, &d, &hh, &mm, &ss, &zzz, &elapsed_double);
		elapsed_time = (int)(elapsed_double * 1000);

		end += hh * 60;
		end += mm;
		end *= 60;
		end += ss;
		end *= 1000;
		end += zzz;

		for (int j = end - (elapsed_time - 1) - 999; j <= end; j++)
		{
			if (j < 0 || j >= BUFF_SIZE)
				continue;
			buff[j] += 1;
			answer = max(answer, buff[j]);
		}
	}

	return answer;
}


#endif

int main() {
	vector<string> lines = {
		"2016-09-15 01:00:04.002 2.0s",
		"2016-09-15 01:00:07.000 2s"
	};

	cout << solution(lines) << endl;
	return 0;
}
