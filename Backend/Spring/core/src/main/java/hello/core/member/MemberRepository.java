package hello.core.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository {

    void save(Member member);

    Member findById(Long memberId);
}
