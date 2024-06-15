package zxcv.asdf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zxcv.asdf.domain.Answer;
import zxcv.asdf.repository.AnswerRepository;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }
}
