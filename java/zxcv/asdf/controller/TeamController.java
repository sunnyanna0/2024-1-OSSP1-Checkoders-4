package zxcv.asdf.controller;

import lombok.RequiredArgsConstructor;

import zxcv.asdf.DTO.TeamDTO;
import zxcv.asdf.domain.Team;
import zxcv.asdf.service.RandomTeamService;
import zxcv.asdf.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final RandomTeamService randomTeamService;
    private final TeamService teamService;

    @PostMapping("/assign")
    public List<Team> assignTeams(@RequestParam Long lectureId, @RequestParam int teamSize) {
        return randomTeamService.assignTeams(lectureId, teamSize);
    }

    @GetMapping("/team")
    public ResponseEntity<TeamDTO> getTeam(@RequestParam Long lectureId, @RequestParam String userToken) {
        TeamDTO team = teamService.getTeamByLectureAndUser(lectureId, userToken);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(team);
    }
}
