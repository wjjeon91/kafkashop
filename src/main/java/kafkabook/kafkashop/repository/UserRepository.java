package kafkabook.kafkashop.repository;

import kafkabook.kafkashop.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long save(User member);
    Optional<User> findById(Long id);     //Optional을 사용하면 null로 반환되지 않는다.
    Optional<User> findByName(String name);
    List<User> findAll();
}
