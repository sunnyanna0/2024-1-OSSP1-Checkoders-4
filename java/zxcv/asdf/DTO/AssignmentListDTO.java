package zxcv.asdf.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentListDTO {
    private List<AssignmentDTO> assignments;
    private List<AssignmentDTO> submittedProblems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignmentDTO {
        private String title;
        private String description;
        private LocalDateTime deadline;
        private boolean isDone;
    }
}
