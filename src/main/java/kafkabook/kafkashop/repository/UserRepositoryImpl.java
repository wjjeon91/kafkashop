package kafkabook.kafkashop.repository;

import kafkabook.kafkashop.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @PersistenceContext
    private EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = em.find(User.class,id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByName(String name) {
        List<User> result = em.createQuery("select u from User u where u.name = :name",User.class)
                .setParameter("name",name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }
}
