#include <iostream>
#include <vector>

// 5. 다형성(가상함수기반 코딩)은 "OCP"를 만족하는 좋은 코딩 스타일입니다.
 
// 6. 디자인 패턴이란?
//    자주 사용하는 코딩 스타일에 "이름"을 부여한 것
//    1994년 4명의 저자가 쓴 "디자인 패턴"이란 책 제목, 23개의 패턴 소개
//    흔히 "Gangs Of Four"라고 표현 GoF's Design Pattern
 
// 7. Prototype 패턴: 견본 객체를 생생후, 복사를 통해서새로운 객체를 만드는 기술
//                    Clone() 가상함수.
 

class Shape
{
public:
    virtual void Draw() { std::cout << "Draw Shape" << std::endl;}
    virtual Shape* Clone() {return new Shape(*this);}
}

class Rect : public Shape{
public:
    void Draw() override { std::cout << "Draw Rect" << std::endl; }
    Shape* Clone() override { return new Rect(*this);}
}

class Circle : public Shape{
public:
    void Draw() override { std::cout << "Draw Circle" << std::endl; }
    Shape* Clone() override { return new Circle(*this);}
}

int main(){
    std::vector<Shape*> v;

    while(true){
        int cmd;
        std::cin >> cmd;

        if(cmd == 1)v.push_back(new Rect);
        else if(cmd == 2)v.push_back(new Circle);
        else if(cmt == 8){
            int k;
            std::cin>>k;
 
            // k번째 만들었던 도형의 복사본을 만들어서 v 끝에 추가합니다.
            // 그런데, k번째 만든 도형은 뭘까요?
            // 어떻게 코딩해야 할까요?
 
 
            /*
            방법 1. 타입을 조사해서 "조건식"으로...
                    --> OCP를 만족할 수 없는 좋지 않은 코드 입니다.
            switch (v[k - 1]->type)
            {
            case 1: v.push_back(new Rect(*v[k-1])); break;
            case 2: v.push_back(new Circle(*v[k - 1])); break;
            }
            */
 
            // 리팩토링 용어: "Replace Conditional With Polymorphism"
            //               "조건식이 있으면 다형성"으로 교체 해라.
            v.push_back(v[k]->Clone()); // 다형성, OCP를 만족합니다.
                                        // triangle이 추가되어도 이 코드는 변하지 않습니다.

        }
        else if(cmd == 9){
            for(auto p : v)
                p->Draw();   // 다형성(Polymorphism)
                            // 동일한 표현식이 상황(실제 객체 종류)에 따라 다르게 동작
            // 객체지향언어의 3대 특징: 캡슐화, 상속, 다형성
        }
    }
    return 0;
}
