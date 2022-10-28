package study.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * Spring data Jpa에서 알아서 읽어서 적용을 해준다.
 * 
 * 시간은 어지간한 엔티티에 모두 필요하지만 생성,수정자의 정보는 필요 없는 경우가 많다.
 * 따라서 시간만 분리해서 상속해주면 된다.
 */
@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity extends BaseTimeEntity{

    // main에서 등록한 AuditorAware에서 사용자를 가져오는 로직을 따라서 넣어주게 된다.
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
