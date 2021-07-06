package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class SubQueryTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            for (int i = 0; i < 10; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            Double avgAge = em.createQuery("select avg(m.age) from Member m", Double.class).getSingleResult();
            System.out.println(avgAge);

            String query = "select m from Member m where m.age > (select avg(m2.age) from Member m2)";
            List<Member> members = em.createQuery(query, Member.class).getResultList();
            for (Member member : members) {
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
    }
}
