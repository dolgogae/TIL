package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 비영속 상태
            // entity 객체를 생성하고 준비해둔 상태
            Member member = new Member();
            member.setId(100L);
            member.setUsername("HelloJPA");

            // 영속
            // 영속성 컨텍스트에 의해 관리가 되는 상태
            // 아직까지 DB에 쿼리가 날아간 상태는 아니다.
            // 트랜잭션을 commit하는 순간 쿼리가 날아간다. -> 그 전에는 쓰기 지연 SQL 저장소에 저장해둔다.
            // batch로 한꺼번에 쿼리를 보내서 속도의 이점을 볼 수 있다.
            em.persist(member);

            // 영속성 컨텍스트에서 관리하지 않는 상태로 만든다.
            em.detach(member);
            // DB에서 지우는 요청
            em.remove(member);

            /**
             * 영속성 컨텍스트는 1차 캐시를 가지고 있다.
             * persist를 하게 되면, DB로 바로 저장하는 것이 아닌
             * 1차 캐시에 저장을 하고, 조회 또한 1차 캐시에서 조회하게 된다.
             * 만약 조회시 1차 캐시에 없다면, DB를 조회하고 조회한 내용을 1차캐시에 저장을 하고 반환하게 된다.
             *
             * entity manager는 트랜잭션 단위에서만 존재하고 소멸하기 때문에 큰 효과를 볼 수 없다.
             */
            // select 쿼리가 날아가지 않는다. -> 1차 캐시에 저장된것 가져오기 때문에
            Member a = em.find(Member.class, 100L);
            Member b = em.find(Member.class, 100L);


            // true가 나온다.
            // 영속 엔티티의 동일성을 보장해준다. 1차 캐시를 했기 때문에
            System.out.println("result: " + (a == b));

            /**
             * 변경감지
             * 따로 update 쿼리 명령을 소스코드로 짜놓지 않아도
             * 변경감지를 통해서 알아서 DB에 변경된다.
             *
             * DB 커밋 시점에서 flush()를 호출하게 된다.
             * 이후, entity와 스냅샷을 비교한다.(스냅샷: 최초 상태)
             * 변경되어있는 것이 있다면 쓰기 지연 SQL저장소에 관련 쿼리를 저장해놓게 된다.
             */
            b.setUsername("JPAHello");

            /**
             * flush를 하게 되면, 중간에 원할때 DB에 쿼리를 날릴 수 있다.
             * 하지만, 1차 캐시는 유지되어 있는다. 쓰기 지연 SQL 저장소가 DB에 반영이 되는 과정이다.
             *
             * JPQL 실행시에는 SQL로 변역하는 과정이 있기 때문에
             * 그 전에 flush를 한번 날리게 된다.
             */
            Member member2 = new Member(200L, "member2");
            em.persist(member2);
            em.flush();

            /**
             * detach를 하게 되면 영속성 컨텍스트에서 더이상 관리하지 않는다. -> 준영속 상태가 된다.
             */
            Member member3 = em.find(Member.class, 100L);
            member3.setUsername("member3");
            em.detach(member3);

            /**
             * 영속성 컨텍스트에서 아예 지운다.
             * 다시 찾을 경우에는 다시 select문이 날아가서 찾아오게 된다.
             */
            em.clear();

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member4 = new Member();
            // member4.setTeamId(team.getId());
            member4.setUsername("member1");
            member4.setTeam(team);
            em.persist(member4);

            // 연관관계를 맺어준다면 바로바로 찾아올 수 있다.
            Member findMember = em.find(Member.class, member4.getId());
            Team findTeam = findMember.getTeam();

            Team newTeam = em.find(Team.class, 100L);
            findMember.setTeam(newTeam);
            em.flush();
            em.clear();

            // DB 테이블은 join으로 양방향으로 기본적으로 조회가 가능하다.
            // 이 기능을 JPA에서 해주고 싶으면 양뱡향 연관관계를 맺어주어야 한다.
            // 객체는 일반적으로 단방향으로 하면 단순하고 좋은 경우가 많다.(웬만하면 단방향 매핑으로 설계를 마치는 것이 좋다.)
            Member findMember2 = em.find(Member.class, member4.getId());
            List<Member> members = findMember2.getTeam().getMembers();

            for(Member mem: members){
                System.out.println("member: " + mem.getUsername());
            }

            em.flush();
            em.clear();
            ///////////////////////////////
            Member member5 = new Member();
            member5.setUsername("member5");
            em.persist(member5);

            Team team2 = new Team();
            team2.setName("TeamA");
            // 연관 관계의 주인이 아닌 곳에 member를 넣어도 실질적으로 DB에 반영되지 않는다.
            team2.getMembers().add(member5);
            // 연관 관계의 주인인 곳에 넣으면 DB에 반영이 된다.
            member5.setTeam(team2);

            em.flush();
            em.clear();

            // 하지만 양쪽에 모두 넣는 것을 권장한다.
            // 실제 team에서 member를 사용하는 시점에 쿼리를 날리게 된다.(따라서 값이 조회가 되긴 한다.)
            // 하지만 flush(), clear() 하기전에 값을 조회한다면 members는 아무것도 조회되지 않을 것이다.
            // 테스트케이스를 작성할때도 오류가 날 수도 있기 때문에
            // 양방향 모두에 값을 세팅해주는 것이 좋다.
            Member findMember3 = em.find(Member.class, member5.getId());
            List<Member> members2 = findMember3.getTeam().getMembers();

            for(Member mem: members2){
                System.out.println("member: " + mem.getUsername());
            }

            // 두개 모두 넣어주는 것에 실수를 대비하는 것이 좋다.
            // 반대로 team에서 member를 만들어줘도 둘다 넣어지는 것이라 편한대로 하면 된다.
            member5.changeTeam(team2);

            em.persist(team2);

            em.flush();
            em.clear();

            /**
             * 상속관계 매핑
             * 객체의 상속 구조와 DB의 슈퍼타입 서브타입 관계를 매핑하는 것.
             * 1. Join 전략: DB를 공통 컬럼 테이블에 나머지 정보를 join하는 것.(구분하는 컬럼도 보통 넣어준다.)
             * 2. 단일 테이블 전략: 한개의 테이블로 모두 묶어서 만드는 것
             * 3. 각각 테이블을 만들어서 각각 정보를 다 가지고 있는 것.
             *
             * 비즈니스적으로 복잡하고 중요하면 join으로 하고, 단순한 경우에는 단일 테이블이 좋다.
             */
            Movie movie = new Movie();
            movie.setDirector("aaaa");
            movie.setActor("bbb");
            movie.setName("바람과 함께 사라지다.");
            movie.setPrice(10000);

            em.persist(movie);
            
            em.flush();
            em.clear();

            // 필요시에 join도 JPA가 알아서 해준다.
            Movie findMovie = em.find(Movie.class, movie.getId());

            // 테이블을 각각 분리했을 시에 다음에서는 모든 테이블(movie, book 등)을 조회해야 가능하다
            Item item = em.find(Item.class, movie.getId());

            /**
             * @MappedSuperclass
             * 공통매핑 정보가 필요할때(데이터가 컬럼이 곂치는게 많다.)
             * 직접 생성할일이 없으니 추상 클래스로 만드는 것이 좋다.
             */
            Member member6 = new Member();
            member6.setUsername("kim");
            member6.setCreatedBy("user1");
            member6.setCreateDate(LocalDateTime.now());

            em.persist(member6);

            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void printMemberAndTeam(Member member){
        String username = member.getUsername();

        Team team = member.getTeam();
    }
}
