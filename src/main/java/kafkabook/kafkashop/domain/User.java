package kafkabook.kafkashop.domain;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;

    public User() {
        // 기본 생성자
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // 비밀번호를 암호화하여 저장하는 메서드
    public void setPassword(String password) {
        // 여기에서 비밀번호를 암호화하는 로직을 추가합니다.
        // 예를 들어, BCryptPasswordEncoder를 사용하여 비밀번호를 암호화할 수 있습니다.
        // 아래는 스프링 시큐리티의 BCryptPasswordEncoder를 사용한 예시입니다.
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    // 비밀번호 검증 메서드
    public boolean checkPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, this.password);
    }
}
