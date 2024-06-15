package zxcv.asdf.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class page6_review {
    private String description;
    private String hw_test1;
    private String hw_test_answer1;

    private String answer_text;
    private Long assignment_id;
    private String user_token;
    private String gpt_feedback;
    private String result;
}
