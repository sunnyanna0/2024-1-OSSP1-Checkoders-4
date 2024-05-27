package zxcv.asdf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxcv.asdf.domain.Enrollment;
import zxcv.asdf.domain.Lecture;
import zxcv.asdf.domain.LectureAssignment;
import zxcv.asdf.domain.User;
import zxcv.asdf.repository.EnrollmentRepository;
import zxcv.asdf.repository.LectureAssignmentRepository;
import zxcv.asdf.repository.LectureRepository;
import zxcv.asdf.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Sservice {

    @Autowired
    private final LectureRepository lectureRepository;
    private final LectureAssignmentRepository lectureAssignmentRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public Lecture create_lecture(Lecture lecture){
        lectureRepository.save(lecture);
        return lecture;
    }

    public LectureAssignment addLectureAssignment(LectureAssignment lectureAssignment) {
        return lectureAssignmentRepository.save(lectureAssignment);
    }

    public List<Long> getLectureIdsByUserId(Long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(Enrollment::getLecture)
                .map(Lecture::getId)
                .collect(Collectors.toList());
    }
    public User addUser(User user) {
        return userRepository.save(user);
    }
}
