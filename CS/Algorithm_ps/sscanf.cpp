#include <iostream>
#include <string>
using namespace std;

// int sscanf(const char* str, const char* format, ...);
// str에서 데이터를 형시 문자열에서 지정하는 바에 따라 읽어와
// 그 데이터를 뒤에 부수적인 인자들이 가리키는 메모리 공간에 저장하게 된다.


// 형식 - %[*][폭(width)][한정자(modifiers)]타입(type)
// * : 데이터가 받아들이지만 무시된다. 다음 형식 태그에 대응된다.
// 폭: stdin에서 읽어들일 최대 문자수 지정
//		scanf("%10s", str);은 최대 10문자 읽어와서 str에 저장

int main() {
	string str = "123-456";
	int i,j;

	sscanf(str.c_str(), "%d", &i); // c_str(): string_ref 참고
	cout << str << endl;
	cout << i << endl;

	string sentence = "Rudolph is 12 years old";
	char str1[10];
	sscanf(sentence.c_str(), "%s %*s %d", str1, &i);
	cout << str1 << ' ' << i << endl;

	return 0;
}
