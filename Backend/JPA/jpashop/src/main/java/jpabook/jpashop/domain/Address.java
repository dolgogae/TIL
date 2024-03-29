package jpabook.jpashop.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable // 어딘가에 내장될 수 있다는 어노테이션
@Getter @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;

    /**
     * @return
     * 다음처럼 해당 변수에만 맞는 메서드를 만들수 있다.
     */
    private String fullAddress(){
        return getCity() + " " + getStreet() + " " + getZipcode();
    }
}
