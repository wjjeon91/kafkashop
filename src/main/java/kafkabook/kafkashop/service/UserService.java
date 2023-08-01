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

    public Long signup(User user){

        validateDuplicateUser(user);

        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user){
        userRepository.findByName(user.getName()).ifPresent(m->{throw new IllegalStateException("이미 존재하는 회원입니다.");});
    }

    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long memberId){
        return userRepository.findById(memberId);
    }

    public User findByName(String name){
        User user = userRepository.findByName(name).orElseThrow(()->{throw new IllegalStateException("회원 정보 없습니다.");});
        return user;
    }
}
