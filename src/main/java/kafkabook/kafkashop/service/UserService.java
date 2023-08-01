package kafkabook.kafkashop.service;

import kafkabook.kafkashop.domain.User;
import kafkabook.kafkashop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long save(User user){

        validateDuplicateUser(user);

        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user){
        userRepository.findByName(user.getName()).ifPresent(m->{throw new IllegalStateException("이미 존재하는 회원입니다.");});
    }

    public List<User> findMembers(){
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long memberId){
        return userRepository.findById(memberId);
    }
}
