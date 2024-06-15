package zxcv.asdf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zxcv.asdf.domain.LectureAssignment;
import zxcv.asdf.repository.LectureAssignmentRepository;

@Service
@RequiredArgsConstructor
public class LectureAssignmentService {

    private final LectureAssignmentRepository lectureAssignmentRepository;

    public LectureAssignment getAssignmentById(Long assignmentId) {
        return lectureAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }
}
