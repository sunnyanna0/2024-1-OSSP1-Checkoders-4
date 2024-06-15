//package zxcv.asdf.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//import zxcv.asdf.DTO.page6_review;
//import zxcv.asdf.domain.Answer;
//import zxcv.asdf.repository.AnswerRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequiredArgsConstructor
//public class ReviewController {
//
//    private final AnswerRepository answerRepository;
//
//    @GetMapping("/review/{assignmentId}")
//    public List<page6_review> getReviews(@PathVariable Long assignmentId) {
//        List<Answer> answers = answerRepository.findByAssignmentId(assignmentId);
//
//        return answers.stream().map(answer -> page6_review.builder()
//                .description(answer.getAssignment().getDescription())
//                .hw_test1(answer.getAssignment().getHwTest1())
//                .hw_test_answer1(answer.getAssignment().getHwTestAnswer1())
//                .answer_text(answer.getAnswerText())
//                .assignment_id(answer.getAssignment().getId())
//                .user_token(answer.getUser().getToken())
//                .gpt_feedback(answer.getGptFeedback())
//                .result(answer.getResult())
//                .build()).collect(Collectors.toList());
//    }
//}
