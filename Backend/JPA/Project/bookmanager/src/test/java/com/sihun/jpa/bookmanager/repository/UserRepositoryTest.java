package com.sihun.jpa.bookmanager.repository;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

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
    }
}
