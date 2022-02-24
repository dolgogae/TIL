package com.sihun.jpa.bookmanager.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity // JPA가 관리하는 Entity 객체임을 선언
//@Table(name="user", indexes = {@Index(columnList = "name")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@EntityListeners(value = MyEntityListener.class)
public class User implements Auditable{
    @Id     // table pk
    @GeneratedValue // 사용자가 생성하지 않고 생성된 값을 쓰겠다는 것.
    private Long id;

    @NonNull            // 필수값이 된다.
    private String name;
    @NonNull
//    @Column(unique = true)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

//    @Column(name = "crtd_dt")
    @Column(updatable = false)
    private LocalDateTime createdAt;

//    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Transient
    private String testData;

//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Address> addresses;

//    @PrePersist // insert method가 실행되기전
//    @PreUpdate // merge method가 실행되기전
//    @PreRemove // delete method가 실행되기전
//    @PostPersist  // insert method가 실행한후
//    @PostUpdate // merge method가 실행된 후
//    @PostRemove // delete method가 실행된 후
//    @PostLoad // select method가 실행된 후
//
//    @PrePersist
//    public void perPersist(){
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void perUpdate(){
//        this.updatedAt = LocalDateTime.now();
//    }
}