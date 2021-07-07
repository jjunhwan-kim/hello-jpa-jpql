package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PathExpressionTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.changeTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.changeTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            // 상태 필드
            // 연관 필드 - 단일 값 연관 필드, 컬렉션 값 연관 필드

            // 상태 필드
            //String query = "select m.username from Member m";

            // 단일 값 연관 경로 - 묵시적 내부 조인 발생
            //String query = "select m.team from Member m";

            // 컬렉션 값 연관경로 - t.members에서 더 이상 탐색 불가
/*
            String query = "select t.members from Team t";

            Collection result = em.createQuery(query, Collection.class).getResultList();
            for (Object o : result) {
                System.out.println(o);
            }
*/
            // from 절에서 명시적 조인을 통해 별칭을 얻어 탐색 가능
            String query = "select m.username from Team t join t.members m";
            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String member : result) {
                System.out.println(member);
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
