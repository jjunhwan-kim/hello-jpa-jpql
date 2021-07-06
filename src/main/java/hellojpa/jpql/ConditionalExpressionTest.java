package hellojpa.jpql;

import javax.persistence.*;
import java.util.List;

public class ConditionalExpressionTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            for (int i = 5; i < 15; i++) {
                Member member = new Member();
                //member.setUsername("member" + i);
                member.setUsername(null);
                //member.setUsername("관리자");
                member.setAge(i);
                em.persist(member);
            }

            String query = "select " +
                            "case when m.age <= 10 then '학생요금'" +
                            "     when m.age >= 60 then '일반요금'" +
                            "     else '일반요금' " +
                            "end " +
                            "from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            }

            // coalesce: username이 null이면 '이름 없는 회원' 반환
            query = "select coalesce(m.username, '이름 없는 회원') from Member m";
            // nullif: username이 '관리자'이면 null 반환
            //query = "select nullif(m.username, '관리자') from Member m";

            result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
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
    }
}
