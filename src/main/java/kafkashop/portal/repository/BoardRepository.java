package kafkashop.portal.repository;

import kafkashop.portal.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 기타 메서드 추가 가능
}
