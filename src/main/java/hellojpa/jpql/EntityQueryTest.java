package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class EntityQueryTest {

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

            em.flush();
            em.clear();

            // 엔티티 직접 사용 - 기본 키 값
            // 둘 다 SQL은 "select count(m.id) from Member m" 실행됨
            System.out.println(em.createQuery("select count(m.id) from Member m", Long.class).getSingleResult());
            System.out.println(em.createQuery("select count(m) from Member m", Long.class).getSingleResult());

            // 둘 다 SQL은 "select m.* from Member m where m.id = ?" 실행됨
            System.out.println(em.createQuery("select m from Member m where m = :member", Member.class)
                    .setParameter("member", member1)
                    .getSingleResult().getUsername());

            System.out.println(em.createQuery("select m from Member m where m.id = :memberId", Member.class)
                    .setParameter("memberId", member1.getId())
                    .getSingleResult().getUsername());

            // 엔티티 직접 사용 - 외래 키 값
            // 둘 다 SQL은 "select m.* from Member m where m.team_id = ?" 실행됨
            List<Member> members = em.createQuery("select m from Member m where m.team = :team", Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : members) {
                System.out.println("member = " + member);
            }

            members = em.createQuery("select m from Member m where m.team.id = :teamId", Member.class)
                    .setParameter("teamId", teamA.getId())
                    .getResultList();

            for (Member member : members) {
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
