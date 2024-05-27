package zxcv.asdf.domain;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "user")
    private List<Answer> answers;

}
