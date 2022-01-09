#include <iostream>
#include <string>
using namespace std;



int main() {

	string str = "12345";

	// c_str: string을 C 스타일의 문자열로 바꿀 때 쓰는 함수
	// const char*를 리턴한다.
	const char *s = str.c_str();
	
	// substr: string을 시작점부터 길이만큼
	// substr(str, 시작인덱스, 시작인덱스부터 읽어올 숫자 수)
	string time = "12:12:12:111";
	int h = stoi(time.substr(0, 2));

	return 0;
}
