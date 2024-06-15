package zxcv.asdf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zxcv.asdf.domain.LectureAssignment;

import java.util.Optional;

public interface LectureAssignmentRepository extends JpaRepository<LectureAssignment, Long> {
    Optional<LectureAssignment> findByLectureId(Long lectureId);
}
