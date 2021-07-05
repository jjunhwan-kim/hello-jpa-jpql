package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JoinTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.changeTeam(teamA);
            em.persist(member1);


            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(10);
            member2.changeTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(10);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("teamA");
            member4.setAge(10);
            em.persist(member4);

            em.flush();
            em.clear();

            // 내부 조인
            //String query = "select m from Member m inner join m.team t";
            //String query = "select m from Member m inner join m.team t where t.name = :teamName";

            // 외부 조인
            //String query = "select m from Member m left join m.team t";

            // 세타 조인
            //String query = "select m from Member m, Team t where m.username = t.name";

            // 조인 대상 필터링
            String query = "select t from Member m left join m.team t on t.name = 'teamA'";

            // 연관관계 없는 엔티티 외부 조인
            //String query = "select m from Member m left join Team t on m.username = t.name";

            List<Team> result = em.createQuery(query, Team.class)
                    //.setParameter("teamName", "teamA")
                    .getResultList();

            for (Team member : result) {
                System.out.println("member = " + member);
            }

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
