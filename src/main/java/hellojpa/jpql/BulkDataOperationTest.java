package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class BulkDataOperationTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Team teamC = new Team();
            teamC.setName("팀C");
            em.persist(teamC);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("회원4");
            member4.changeTeam(teamC);
            em.persist(member4);

            /*
             벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리함 따라서 아래와 같이 수행해야함
               - 벌크 연산을 먼저 실행
               - 벌크 연산 수행 후 영속성 컨텍스트 초기화
            */
            // Flush 자동 호출, Commit 또는 Query가 나갈 때
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);
            
            System.out.println(member1.getAge());
            System.out.println(member2.getAge());
            System.out.println(member3.getAge());
            System.out.println(member4.getAge());

            // 영속성 컨텍스트를 초기화하고 Member를 다시 조회해서 써야함
            em.clear();

            // 다시 조회해야함
            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());


            tx.commit();
        }
        catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
