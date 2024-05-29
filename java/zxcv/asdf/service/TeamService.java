package zxcv.asdf.service;

import zxcv.asdf.DTO.TeamDTO;
import zxcv.asdf.domain.Team;
import zxcv.asdf.domain.User;
import zxcv.asdf.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public TeamDTO getTeamByLectureAndUser(Long lectureId, String userToken) {
        List<Team> teams = teamRepository.findByLectureIdAndUserToken(lectureId, userToken);

        if (teams.isEmpty()) {
            return null; // 혹은 예외 처리
        }

        Team team = teams.getFirst();
        List<TeamDTO.TeamMemberDTO> members = teams.stream()
                .map(t -> {
                    User user = t.getUser();
                    TeamDTO.TeamMemberDTO memberDTO = new TeamDTO.TeamMemberDTO();
                    memberDTO.setName(user.getName());
                    memberDTO.setToken(user.getToken());
                    return memberDTO;
                })
                .collect(Collectors.toList());

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(team.getTeamId());
        teamDTO.setMembers(members);

        return teamDTO;
    }
}
