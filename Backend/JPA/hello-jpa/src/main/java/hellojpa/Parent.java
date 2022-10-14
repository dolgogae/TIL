package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Parent {
    
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    /**
     * 다음처럼 cascade를 ALL로 설정해주면 parent만 영속성 컨텍스트에 등록해도 child도 자동으로 등록해준다.
     * ALL: 모두 적용 
     * PERSIST: 영속 - 저장할 때만 라이프사이클을 맞추고 싶을때(삭제를 빼고 싶을때 사용)
     * REMOVE: 삭제
     *
     * 하나의 부모가 자식을 관리할때는 의미가 있다.(소유가자 하나일때)
     * 하지만 만약 여러군데서 관리하는 엔티티일 경우에는 cascade를 사용하지 않는 것이 맞다.
     * 1. 자식-부모의 라이프 사이클이 동일할때
     * 2. 단일 소유자일 때
     * 
     * orphanRemoval: 고아객체
     * child가 해당 리스트에서 빠질때 삭제되버린다.
     * 참조하는 곳이 하나일때 사용해야한다.
     * 만약 parent객체를 지우게 된다면, 리스트에 있는 child는 모두 delete 쿼리가 날아간다.
     * cascade REMOVE처럼 동작한다.
     * 
     * cascade ALL + orphanRemoval true
     * 부모 엔티티의 라이프사이클로 자식의 라이프 사이클을 관리할 수 있다.(따라 영속성컨텍스트로 child를 관리하지 않는 느낌을 준다.)
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }

    public List<Child> getChildList() {
        return childList;
    }
    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
