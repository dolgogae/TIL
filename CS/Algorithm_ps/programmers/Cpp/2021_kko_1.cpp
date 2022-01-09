#include <string>
#include <iostream>

using namespace std;

struct Point{
    int s, e;
};
int len;

int isBigChar(char a){
    return 'A' <= a && a <= 'Z';
}

int isImpossChar(char a){
    if('a' <= a && a <= 'z')return 0;
    if('0' <= a && a <= '9')return 0;
    if(a == '-') return 0;
    if(a == '_') return 0;
    if(a == '.') return 0;
    return 1;
}

void isPossChar(string &new_id){
    for(int i=0; i<len; ++i){
        if(isBigChar(new_id[i]))
            new_id[i] = new_id[i]-'A'+'a';
    }

    for(int i=0; i<len; i++){ 
        if(isImpossChar(new_id[i])){
            new_id.erase(i, 1);
            // cout << new_id << len << '\n';
            --len;
            --i;
        }
    }
}

void checkPossPoint(string &new_id){
    int check=0;
    for(int i=0; i<len; i++){
        if(new_id[i] == '.')++check;
        if(new_id[i] != '.' || i == len-1){
            if(i==len-1)i = len;
            if(!check || check == 1){
                check = 0;
                continue;
            }
            // printf("%d %d\n", i-check+1 , i);
            new_id.erase(i-check,check-1);
            i -= (check-1);
            len -= (check-1);
            check = 0;
        }
        // cout << new_id + "  "<< len <<'\n';
    }
    if(new_id[0] == '.'){
        new_id.erase(0,1);
        len--;
    }
    if(new_id[len-1] == '.'){
        new_id.erase(len-1, 1);
        len--;
    }
}

void checkStringLength(string &new_id){
    if(len == 0){
        new_id = "aaa";
        len = 3;
    }
    if(len < 3){
        char c = new_id[len-1];
        for(int i=0; i<3-len; i++)
            new_id += c;
        len=3;
    }
    if(len > 15){
        new_id.erase(15, len - 15);
        len=15;
    }
    
    checkPossPoint(new_id);
}

string solution(string new_id) {
    len = new_id.length();
    isPossChar(new_id);
    cout << new_id << '\n';

    checkPossPoint(new_id);
    cout << new_id << '\n';

    checkStringLength(new_id);
    cout << new_id << '\n';
    

    return new_id;
}

int main(){
    string new_id;
    cin >> new_id;
    new_id = solution(new_id);
    cout << new_id << '\n';
    return 0;
}