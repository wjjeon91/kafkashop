package kafkashop.portal.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne // 다대일 관계 매핑
    @JoinColumn(name = "writer_id") // 외래키 설정
    private Member writer; // 작성자 정보 추가

    @Column(name = "writer_name")
    private String writerName; // 작성자 이름
}
