#include <iostream>
#include <string>
#include <conio.h>
 
#define TEMP 1

#if TEMP==0


    // 공통성과 가변성의 분리
    // 변하지 않는 흐름속에 숨겨진 변해야 하는 부분을 찾는다.
    // 변해야 하는 것을 가상함수로 분리한다.
    //                  => 변경하고 싶으면 파생 클래스 만들어서 가상함수를 override한다.
 

class Edit{
    std::string data;
public:

    virtual bool validate(char c) {
        return true;
    }
 
    std::string getData(){
        data.clear();
 
        while (1){
            char c = _getch();
 
            if (c == 13) break;
 
            if (validate(c)){
                std::cout << c;
                data.push_back(c);
            }
        }
        std::cout << std::endl;
 
        return data;
    }
};
 
class NumEdit : public Edit{
public:
    bool validate(char c) override {
        return isdigit(c);
    }
};
 
#else 

// 변하는 것을 분리하는 방법 2. 변하는 것을 다른 클래스 분리
 
// 변해야 하므로 교체 가능해야 한다.
// 약한 결합: 인터페이스부터 설계해야한다.
 
// 모든 validator는 아래 인터페이스를 구현해야 한다.
 
struct IValidator {
    virtual bool validate(std::string s, char c) = 0;
    virtual bool iscomplete(std::string s) { return true; }
 
    virtual ~IValidator() {}
};
 
 
// 주민번호 : 801 1 확인버튼
 
class Edit
{
    std::string data;
    IValidator* pVal = nullptr; // 입력값의 유효성을 확인할때 사용할 객체
public:
    void setValidator(IValidator* p) { pVal = p; }
 
    std::string getData()
    {
        data.clear();
 
        while (1)
        {
            char c = _getch();
 
            if (c == 13 && (pVal==nullptr || pVal->iscomplete(data)) ) break;
 
            if (pVal == nullptr || pVal->validate(data, c))
            {
                std::cout << c;
                data.push_back(c);
            }
        }
        std::cout << std::endl;
 
        return data;
    }
};
 
// 이제 다양한 정책을 가진 Validator를 제공하면 됩니다.
 
class LimitDigitValidator : public IValidator {
    int limit;
public:
    LimitDigitValidator(int n) : limit(n){}
     
    bool validate(std::string s, char c) override {
        return s.size() < limit && isdigit(c);
    }
 
    bool iscomplete(std::string s) override {
        return s.size() == limit;
    }
};

#endif

int main(){
    // NumEdit e;
    // while (1){
    //     std::cout << e.getData() << std::endl;
    // }

    Edit e;
    LimitDigitValidator v(5);
    e.setValidator(&v);
 
    LimitDigitValidator v(15);
    e.setValidator(&v);
 
 
    while (1)
    {
        std::cout << e.getData() << std::endl;
    }
}
 
