package com.sihun.jpa.bookmanager.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sihun.jpa.bookmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Optional<User> findByName(String name);
    Set<User> findByName(String name);
    // 코드 가독성을 높이기 위해 is를 붙이기도 한다. 쿼리문은 위와 동일하게 날아간다.
    Set<User> findUserByNameIs(String name);
    Set<User> findUserByName(String name);
    Set<User> findUserByNameEquals(String name);

    User findByEmail(String email);

    User getByEmail(String email);

    User readByEmail(String email);

    User queryByEmail(String email);

    User searchByEmail(String email);

    User streamByEmail(String email);

    // 가독성을 위해서 User를 넣어주기도 한다 (= findByName)
    User findUserByEmail(String email);

    // 잘못된 네이밍에도 오류가 나지 않고 그대로 실행된다.
    // User findSomethingByEmail(String email);

    // first, find 뒤에 숫자만큼의 결과를 반환한다.
    List<User> findFirst1ByName(String name);
    List<User> findTop1ByName(String name);

    // 해당 쿼리는 만들어지지 않는다 -> findByName의 변형이름으로 인식하게 된다.
//    List<User> findLast1ByName(String name);

    List<User> findByEmailAndName(String email, String name);
    List<User> findByEmailOrName(String email, String name);

    // After를 넣게 되면 보다 큰 값을 가져온다.
    // equals를 포함하지 않는다.
    List<User> findByCreatedAtAfter(LocalDateTime yesterday);
    List<User> findByIdAfter(Long id);

    // After와 같은 쿼리문을 날리게된다. 좀더 범용적으로 쓰인다.
    List<User> findByCreatedAtGreaterThan(LocalDateTime yesterday);

    List<User> findByCreatedAtGreaterThanEqual(LocalDateTime yesterday);

    // Between은 기본적으로 equal 포함
    List<User> findByCreatedAtBetween(LocalDateTime yesterday, LocalDateTime tomorrow);
    List<User> findByIdBetween(Long id1, Long id2);
    // 위와 동일한 동작을 하게 된다.
    // List<User> findByCreatedAtGreaterThanEqualAndLessThanEqual(LocalDateTime yesterday, LocalDateTime tomorrow);

    List<User> findByIdIsNotNull();

    // 문자열의 not empty가 아니라 collection의 not empty를 체크한다.
//    List<User> findByAddressesIsNotEmpty(); // name is not null and name is not ''??

    // or 조건과 비슷하다.
    List<User> findByNameIn(List<String> names);

    List<User> findByNameStartingWith(String name);
    List<User> findByNameEndingWith(String name);
    List<User> findByNameContains(String name);

    List<User> findByNameLike(String name);
}
