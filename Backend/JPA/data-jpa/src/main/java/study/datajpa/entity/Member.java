package study.datajpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
/** 
 * jpa가 프록시를 할때, 기본 생성자가 private이거나 없으면
 * 만들지 못하기 때문에 최소 protected까진 해줘야한다.
 */
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

    protected Member(){}

    public Member(String username){
        this.username = username;
    }   
}
