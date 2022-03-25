#include <bits/stdc++.h>
using namespace std;

struct AbsComp{
    bool operator()(const int &a, const int &b) const {
        return abs(a) < abs(b);
    }
};

struct Student{
    int id;
    int phone_number;

    bool operator <(const Student s) const{
        if(s.id == this->id) return this->phone_number < s.phone_number;
        return this->id < s.id;
    }
};


int main(){
    // list의 쉬운 초기화
    initializer_list<int> il{1,2,3,4,5};

    map<string, string> m;

    m["a"] = "aa";
    m["b"] = "bb";
    m["c"] = "cc";

    list<int> li, li2;

    set<int, AbsComp> s;

    return 0;
}