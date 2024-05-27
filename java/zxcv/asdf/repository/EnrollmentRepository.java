package zxcv.asdf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zxcv.asdf.domain.Enrollment;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByLectureId(Long lectureId);

}