package com.sihun.jpa.bookmanager.repository;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

import java.time.LocalDateTime;
import java.util.List;
import com.sihun.jpa.bookmanager.domain.User;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void _crud(){
         userRepository.save(new User());

         System.out.println("print db");
         userRepository.findAll().forEach(System.out::println);
    }

    @Test
    // @Transactional
    void crud(){
        // userRepository.save(new User());

        // System.out.println("print db");
        // userRepository.findAll().forEach(System.out::println);
        
        // #####
        // List<User> users = userRepository.findAll(Sort.by(Direction.DESC, "name"));

        // List<User> users = userRepository.findAllById(Lists.newArrayList(1L, 2L, 5L));

        // users.forEach(System.out::println);

        User user1 = new User("jack", "jack@gmail.com");
        User user2 = new User("steve", "steve@gamil.com");

        userRepository.saveAll(Lists.newArrayList(user1, user2));

        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);


        // User user3 = userRepository.getOne(1L);

        User user3 = userRepository.findById(1L).orElse(null);

        System.out.println(user3);

        userRepository.saveAndFlush(new User("kante", "kante@gmail.com"));

        // userRepository.flush();
        userRepository.findAll().forEach(System.out::println);

        long count = userRepository.count();
        System.out.println(count);

        boolean exist = userRepository.existsById(1L);
        System.out.println(exist);

        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));

        // userRepository.deleteAll();
        userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(1L,3L)));
        userRepository.findAll().forEach(System.out::println);

        userRepository.deleteInBatch(userRepository.findAllById(Lists.newArrayList(1L, 3L)));
        userRepository.findAll().forEach(System.out::println);
        
        Page<User> users2 = userRepository.findAll(PageRequest.of(1, 3));

        System.out.println("page: "+users2);
        System.out.println("totalElements: "+users2.getTotalElements());
        System.out.println("totalPages: "+users2.getTotalPages());
        System.out.println("numberOfElements: "+users2.getNumberOfElements());
        System.out.println("sort: "+users2.getSort());
        System.out.println("size: "+users2.getSize());

        users2.getContent().forEach(System.out::println);

        // 생각보다 실무에서 많이 쓰이진 않는다.
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name")
                // .withMatcher("email", endsWith());  // 뒤와 매칭이 되는것 - email의 도메인
                .withMatcher("email", contains());      // 문자열을 포함하는 것
                // 실제로는 처리를 안하고 그냥 matcher만 사용해서 비교한다.

        Example<User> example = Example.of(new User("ma", "gmail.com"), matcher); // 가짜 엔티티
        // ignorepath로 인해 "name"은 무시가 됐다.
        userRepository.findAll(example).forEach(System.out::println);

        userRepository.save(new User("tiago", "tiago@gmail.com"));

        // update query
        User user4 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user4.setEmail("sihun-update@gmail.com");

        userRepository.save(user4);
    }

    @Test
    void select(){
//        System.out.println("--- print query ---");
//        System.out.println(userRepository.findByName("drogba"));
//
//        System.out.println("findByEmail: "+userRepository.findByEmail("drogba@gmail.com"));
//        System.out.println("getByEmail: "+userRepository.getByEmail("drogba@gmail.com"));
//        System.out.println("readByEmail: "+userRepository.readByEmail("drogba@gmail.com"));
//        System.out.println("queryByEmail: "+userRepository.queryByEmail("drogba@gmail.com"));
//        System.out.println("searchByEmail: "+userRepository.searchByEmail("drogba@gmail.com"));
//        System.out.println("streamByEmail: "+userRepository.streamByEmail("drogba@gmail.com"));
//        System.out.println("findUserByEmail: "+userRepository.findUserByEmail("drogba@gmail.com"));
//        System.out.println("findSomethingByEmail: "+userRepository.findSomethingByEmail("drogba@gmail.com"));
//        System.out.println("findTop1ByName: "+userRepository.findTop1ByName("drogba"));
//        System.out.println("findFirst1ByName: "+userRepository.findFirst1ByName("drogba"));
//        System.out.println("findLast1ByName: "+userRepository.findLast1ByName("drogba"));

        System.out.println("findByEmailAndName: "+userRepository.findByEmailAndName("drogba@gmail.com", "drogba"));
        System.out.println("findByEmailOrName: "+userRepository.findByEmailOrName("drogba@gmail.com", "drogba"));
        System.out.println("findByCreatedAtAfter: "+userRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByIdAfter: "+userRepository.findByIdAfter(4L));

        System.out.println("findByCreatedAtGreaterThan: "+userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByCreatedAtGreaterThanEqual: "+userRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(1L)));

        System.out.println("findByCreatedAtBetween: "+userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween: "+userRepository.findByIdBetween(1L, 3L));

        System.out.println("findByIdIsNotNull: "+userRepository.findByIdIsNotNull());
//        System.out.println("findByAddressesIsNotEmpty: "+userRepository.findByAddressesIsNotEmpty());

        System.out.println("findByNameIn: "+userRepository.findByNameIn(Lists.newArrayList("drogba","maison")));

        System.out.println("findByNameStartingWith: "+userRepository.findByNameStartingWith("dro"));
        System.out.println("findByNameEndingWith: "+userRepository.findByNameEndingWith("ba"));
        System.out.println("findByNameContains: "+userRepository.findByNameContains("rog"));

        System.out.println("findByNameLike: "+userRepository.findByNameLike("%rog%"));
    }
}
