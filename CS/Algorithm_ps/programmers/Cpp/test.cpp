#include <bits/stdc++.h>
using namespace std;


struct node{
    int num;
    int amount;
    int price;
    string name;
};

struct Compare {
	bool operator() (const node &a, const node &b) const{
		if (a.price == b.price)
			return a.num < b.num;
		else
			return a.price < b.price;
	}
};

set<node, Compare> m;

int main(){

    for(int i= 0; i<5; i++){
        string a; int b,c,d; cin>>a>>b>>c>>d;
        m.insert({b,c,d,a});
    }

    for(auto i: m){
        cout << i.num << i.price <<'\n';
    }
}