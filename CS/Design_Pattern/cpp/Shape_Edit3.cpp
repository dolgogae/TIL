 
#include <iostream>
#include <vector>
 
class NotImplemented : public std::exception 
{
 
};
 
class Shape
{
public:
    virtual ~Shape() {} // 기반 클래스 소멸자는 가상이어야 한다.
 
protected:
    // 가상함수: 재정의 하지 않으면 구현 물려 주겠다는 것.
    // 순수 가상함수: 재정의 하지 않으면 에러, 반드시 만들어라
    virtual void DrawImp() = 0;
 
public:
    virtual void Draw() final
    {
        std::cout << "Mutex Lock" << std::endl;
        DrawImp();
        std::cout << "Mutex UnLock" << std::endl;
    }
 
    //virtual Shape* Clone() = 0; // 이 코드가 좋지만, 아래처럼 하기도 한다.
    // 재정의 하지 않고 사용하지 않으면 ok,
    // 재정의 하지 않고 사용하면 예외 발생
    virtual Shape* Clone()
    {
        throw NotImplemented();
    }
};
 
class Rect : public Shape
{
public:
    void DrawImp() override { std::cout << "Draw Rect" << std::endl; }
 
    // 가상함수 재정의시 반환 타입 변경 가능합니다(단, 상속관계 타입끼리만 가능)
    Rect* Clone() override { return new Rect(*this); }
};
class Circle : public Shape
{
public:
    void DrawImp() override { std::cout << "Draw Circle" << std::endl; }
 
    Circle* Clone() override { return new Circle(*this); }
};
class Triangle : public Shape
{
public:
    void DrawImp() override { std::cout << "Draw Triangle" << std::endl; }
 
    Triangle* Clone() override { return new Triangle(*this); }
};
 
int main()
{
    std::vector<Shape*> v;
 
    while (1) {
        int cmd;
        std::cin >> cmd;
 
        // 생각해볼 문제 1. 객체 생성을 다형성을 사용할 수 없을까? OCP만족하게
        //              ==> "팩토리"
 
        //              2. Undo / Redo를 하려면 어떻게 해야 할까?
        //              ==> "Command" 패턴
 
        if (cmd == 1)v.push_back(new Rect);
        else if (cmd == 2)v.push_back(new Circle);
        else if (cmd == 8) {
            std::cout << "몇번째 도형을 복사 할까요 >>";
 
            int k;
            std::cin >> k;
            v.push_back(v[k]->Clone()); // 다형성, OCP 만족
        }
        else if (cmd == 9) {
            for (auto p : v)
                p->Draw(); // 다형성, OCP 만족
        }
    }
}
