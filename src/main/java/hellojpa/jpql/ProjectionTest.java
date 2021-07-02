package hellojpa.jpql;

import javax.persistence.*;
import java.util.List;

public class ProjectionTest {
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

            em.flush();
            em.clear();

            // 1. 엔티티 프로젝션
            // 영속성 컨텍스트에 저장됨
//            List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
//            Member findMember = result.get(0);
//            findMember.setAge(20);

            // Join
            // 묵시적 조인
//            List<Team> result = em.createQuery("select m.team from Member m", Team.class).getResultList();
            // 명시적 조인
//            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();

            // 2. 임베디드 타입 프로젝션
//            List<Address> result = em.createQuery("select o.address from Order o", Address.class).getResultList();

            // 3. 스칼라 타입 프로젝션
//            List resultList = em.createQuery("select distinct m.username, m.age from Member m").getResultList();
//            Object o = resultList.get(0);
//            Object[] result = (Object[]) o;
//            System.out.println("username = " + result[0]);
//            System.out.println("age = " + result[1]);

//            List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m").getResultList();
//            Object[] result = resultList.get(0);
//            System.out.println("username = " + result[0]);
//            System.out.println("age = " + result[1]);

            List<MemberDTO> result = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());


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
