package zxcv.asdf.repository;

import zxcv.asdf.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByLectureIdAndUserToken(Long lectureId, String userToken);
}