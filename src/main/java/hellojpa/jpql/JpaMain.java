package hellojpa.jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // TypedQuery: 반환타입이 명확할 때 사용
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            //TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            // Query: 반환 타입이 명확하지 않을 때 사용
            //Query query3 = em.createQuery("select m.username, m.age from Member m");

            // 결과가 없으면 빈 리스트 반환
            List<Member> resultList = query1.getResultList();
            System.out.println("resultList = " + resultList);

            // 결과가 없으면 NoResultException, 결과가 둘 이상이면 NonUniqueResultException 반환
            Member singleResult1 = query1.getSingleResult();
            System.out.println("singleResult = " + singleResult1);

            // 파라미터 바인딩
            Member singleResult2 = em.createQuery("select m from Member m where m.username =: username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult2 = " + singleResult2);

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
