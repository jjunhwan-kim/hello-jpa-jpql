package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinTest {
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

            // 페치 조인
            /*
            //String query = "select m from Member m join fetch m.team";
            //String query = "select m from Member m";

            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                // 회원1, 팀A(DB)
                // 회원2, 팀A(1차 캐시)
                // 회원3, 팀B(DB)
                // 회원4, 팀C(DB)
                // 회원이 100명이고 전부 다른 팀 소속이라면 -> N + 1 만큼 쿼리가 나감
            }
            */

            // 컬렉션 페치 조인 - 연관된 엔티티를 함께 조회함, Team, Member조회
            //String query = "select distinct t from Team t join fetch t.members";
            // 일반 조인 - 연관된 엔티티를 함께 조회하지 않음, Team만 조회
            //String query = "select t from Team t join t.members m";

            /*
            컬렉션 페치 조인시 페이징 API를 사용할 수 없음
              - 페치 조인시 페이징 API 사용시 메모리에서 페이징함, DB에 페이징 쿼리가 안나가고 모든 행을 다 불러옴
              - 방향을 반대로 바꿔서 다대일로 풀어내는 방법, Member에서 Team을 Join해서 페이징
                - "select m from Member m join fetch m.team"
              - Team만 조회하고, @BatchSize 또는 hibernate.default_batch_fetch_size 사용
                - "select t from Team t"
            */
            String query = "select t from Team t";
            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("result = " + result.size());

            for (Team team : result) {
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("- member = " + member);
                }
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
