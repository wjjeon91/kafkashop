package kafkashop.portal.repository;

import kafkashop.portal.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Long save(Member member);
    Optional<Member> findById(Long id);     //Optional을 사용하면 null로 반환되지 않는다.
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
