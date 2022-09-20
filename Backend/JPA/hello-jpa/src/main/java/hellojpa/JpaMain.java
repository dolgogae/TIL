package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Hibernate;

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
