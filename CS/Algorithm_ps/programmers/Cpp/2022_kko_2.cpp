#include <bits/stdc++.h>
using namespace std;

// split
vector<string> split(string str, char delimeter){
    vector<string> ans;
    stringstream ss(str);
    string tmp;

    while(getline(ss, tmp, delimeter)){
        ans.push_back(tmp);
    }
    return ans;
}

bool primeN(long long num) {
	if (num < 2) return false;
	for (long long i = 2; i <= sqrt(num); i++)
        if (num % i == 0) return false;
	return true;
}

int solution(int n, int k) {
    int answer = 0;
    string convertNumber = "";
    // 진수 변환
    while(n>0){
        convertNumber = char(n%k+'0') + convertNumber;
        n/=k;
    }

    cout << convertNumber <<'\n';
    vector<string> splitZeroNumber = split(convertNumber, '0');

    // 소수 갯수 count

    for(auto num: splitZeroNumber){
        cout << num << '\n';
        long long cur = 0;
        for(auto i: num){
            cur = cur*10 + (i-'0');
        }
        if(primeN(cur))++answer;
    }

    return answer;
}

int main(){

    cout << solution(2,3) ;
    return 0;
}