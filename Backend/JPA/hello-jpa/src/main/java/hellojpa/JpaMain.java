package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Hibernate;

public class JpaMain {
    public static void main1(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

//             /* ex2 */
//             Team team = new Team();
//             team.setName("abdc");
//             em.persist(team);

//             Member member = new Member();
//             member.setUsername("kim");
//             member.setTeam(team);
//             em.persist(member);

//             team.addMember(member);

//             // 역방향에선 값을 넣어줘도 DB에 반영이 되지 않는다.

// //            em.flush();
// //            em.clear();

//             Member findMember = em.find(Member.class, member.getId()); // 1차 캐시(DB에 select query가 안나갔다.)
//             List<Member> members = findMember.getTeam().getMembers();
//             for(Member m : members){
//                 System.out.println("member name = "+m.getUsername());
//             }
// //            /* ex1 */
// //            Member member = new Member();
// //            member.setId(2L);
// //            member.setUsername("park");
// //
// //            // id generator를 할 경우 영속성 컨텍스트에서는 persist 단계에서 DB에 집어 넣는다.
// //            // 단, sequence옵션일 경우에는 트랜잭션 커밋 시점에 업데이트 된다.
// //            em.persist(member);
// //
// //            Member findMember = em.find(Member.class, 1L);
// //            System.out.println("findMember id: "+ findMember.getId());
// //            System.out.println("findMember name: "+ findMember.getUsername());
// //
// //            List<Member> members = em.createQuery("select m from Member as m", Member.class)
// //                    .setFirstResult(5)
// //                    .setMaxResults(8)
// //                    .getResultList();
// //
// //            // 변경감지가 된다.
// //            // 따로 persist를 호출하지 않아도 자동으로 변경된다.(dirty checking)
// //            findMember.setUsername("drogba");
// //
// //            em.remove(findMember);
// //
// //            // 강제로 DB에 쿼리를 반영한다.
// //            em.flush();
// //
// //            // transaction commit 시점에서 SQL 쿼리가 나간다.
// //            // buffering 같은 개념
//            tx.commit();

            /** 
             * 프록시 
             */
            Member member = new Member();
            member.setUsername("hello");

            em.persist(member);
            
            em.flush();
            em.clear();

            // Member findMember = em.find(Member.class, member.getId());
            Member findMember1 = em.getReference(Member.class, member.getId());
            System.out.println("findMember: "+findMember1.getUsername());

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            Member findMember2 = em.getReference(Member.class, member.getId());
            
            System.out.println((findMember1.getClass() == findMember2.getClass()));
            System.out.println((findMember1 instanceof Member));
            
            Member reference = em.getReference(Member.class, member.getId());

            // 항상 true로 나와야 한다.
            System.out.println(findMember1 == reference);

            Member refMember = em.getReference(Member.class, member.getId());
            Hibernate.initialize(refMember);    // 강제 초기화
            Member findMember = em.find(Member.class, member.getId());

            System.out.println(refMember == findMember);

            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

//        em.close();
        emf.close();
    }

    private static void printMemberAndTeam(Member member){
        String username = member.getUsername();

        Team team = member.getTeam();
    }
}
