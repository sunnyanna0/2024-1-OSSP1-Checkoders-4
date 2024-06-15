package zxcv.asdf.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureDTO {
    private String name;
    private String madeby;
    private String madeby_name;
    private Long lectureId;
    private String course;
    private LocalDateTime deadline;
}
