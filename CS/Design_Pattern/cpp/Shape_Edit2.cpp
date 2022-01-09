#include <iostream>
#include <vector>
 
// 8. template method 패턴
// => 기반 클래스가 변하지 않은 전체 흐름을 제공하고, 변해야 하는 것을 가상함수화 해서
//    파생 클래스가 변하는 것을 변경할 수 있는 기회를 제공하는 패턴
//    "모든 패턴의 가장 기본이 되는 패턴"
 
// C++11 final 키워드: 가상함수에만 붙일 수 있다.
 
class Shape
{
    // 공통성과 가변성의 분리
    // 변하지 않는 코드 안에 숨은 변해야 하는 부분을 분리되어야 한다.
    // 변하는 부분을 가상함수로 분리한다.
protected:
    // 이 함수는 외부에서 직접 사용을 막아야 한다.
    virtual void DrawImp() 
    {
        std::cout << "Draw Shape" << std::endl;
    }
 
public:
    virtual void Draw() final // C++11 final: 파생 클래스가 재정의 하면 error
    {
        std::cout << "Mutex Lock" << std::endl;
        DrawImp();
        std::cout << "Mutex UnLock" << std::endl;
    }
 
    virtual Shape* Clone() { return new Shape(*this); } 
};
 
class Rect : public Shape
{
public:
    void DrawImp() override { std::cout << "Draw Rect" << std::endl; }
 
    Shape* Clone() override { return new Rect(*this); }
};
class Circle : public Shape
{
public:
    void DrawImp() override { std::cout << "Draw Circle" << std::endl; }
 
    Shape* Clone() override { return new Circle(*this); }
};
class Triangle : public Shape
{
public:
    void DrawImp() override { std::cout << "Draw Triangle" << std::endl; }
 
    Shape* Clone() override { return new Triangle(*this); }
};
 
int main()
{
    std::vector<Shape*> v;
 
    while (1) {
        int cmd;
        std::cin >> cmd;
 
        if (cmd == 1)v.push_back(new Rect);
        else if (cmd == 2)v.push_back(new Circle);
        else if (cmd == 8) {
            std::cout << "몇번째 도형을 복사 할까요 >>";
 
            int k;
            std::cin >> k;
            v.push_back(v[k]->Clone()); 
        }
        else if (cmd == 9) {
            for (auto p : v)
                p->Draw();   
        }
    }
}
