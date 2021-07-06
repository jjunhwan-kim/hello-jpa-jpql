package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FunctionTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamA.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자1");
            member2.changeTeam(teamA);

            em.persist(member2);

            //String query = "select concat('a', 'b') From Member m";
            //String query = "select 'a' || 'b' From Member m";
            String query = "select substring(m.username, 2, 3) From Member m";

            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            // locate
            System.out.println(em.createQuery("select locate('de', 'abcdefg') From Member m", Integer.class).getSingleResult());

            // size는 컬렉션의 사이즈
            List<Integer> size = em.createQuery("select size(t.members) from Team t", Integer.class).getResultList();
            for (Integer s : size) {
                System.out.println("s = " + s);
            }

            // 사용자 정의 함수
            //System.out.println((em.createQuery("select function('group_concat', m.username) from Member m", String.class).getSingleResult()));
            System.out.println((em.createQuery("select group_concat(m.username) from Member m", String.class).getSingleResult()));


            tx.commit();
        }
        catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }



    }
}
