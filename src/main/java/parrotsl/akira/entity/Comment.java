package parrotsl.akira.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String commentText;
    private LocalDateTime commentedAt;
    @OneToOne
    @JoinColumn(name = "comment_by_id")
    private User commentBy;
}
