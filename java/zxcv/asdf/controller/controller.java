package zxcv.asdf.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
//import zxcv.asdf.domain.Assignment;
import zxcv.asdf.domain.Lecture;
import zxcv.asdf.domain.LectureAssignment;
import zxcv.asdf.domain.User;
import zxcv.asdf.service.CodeFeedbackService;
import zxcv.asdf.service.Service;
import zxcv.asdf.service.Sservice;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class controller {
    @Value("${api.key}")
    private String CLIENT_ID; // REST API 키
    @Value("${redirect.uri}")
    private String REDIRECT_URI;// 리디렉션 URI
    @Value("${token.request.uri}")
    private String TOKEN_REQUEST_URI;

    private User user;
    private final Service serv;
    private final CodeFeedbackService codeFeedbackService;
    private final Sservice sservice;


    @GetMapping("/api/hello")
    public String test() {
        return "13234234";
    }
    @PostMapping("/create")
    public User createUser() {
        // 임의로 사용자 정보를 설정하여 User 객체를 생성합니다.
        User user = User.builder()
                .name("최진석")
                .build();
        return sservice.addUser(user);
    }
    @PostMapping("/createlecture")
    public LectureAssignment createLectureAssignment(@RequestParam String lectureName) {
        LectureAssignment lectureAssignment = LectureAssignment.builder()
                .title(lectureName)
                .build();
        return sservice.addLectureAssignment(lectureAssignment);
    }
    /*@GetMapping("/login")
    public void login(@RequestParam String code, HttpServletResponse response) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(TOKEN_REQUEST_URI, request, String.class);
        String userId;
        String nickname;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(tokenResponse.getBody());
            String accessToken = rootNode.path("access_token").asText();

            // 사용자 정보 요청
            headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> userInfoRequest = new HttpEntity<>(headers);
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    userInfoRequest,
                    String.class
            );
            JsonNode userInfoNode = objectMapper.readTree(userInfoResponse.getBody());
            userId = userInfoNode.path("id").asText();
            nickname = userInfoNode.path("properties").path("nickname").asText();
            System.out.println("User ID: " + userId);
            System.out.println("Nickname: " + nickname);
            user = User.builder()
                    .id_token(userId)
                    .nickname(nickname)
                    .build();
            Lecture l1 = Lecture.builder()
                    .lecture_id(0L)
                    .lectureName("ㅇㅇㅇㅇㅇㅇㅇㅇ")
                    .build();
            Lecture l2 = Lecture.builder()
                    .lecture_id(1L)
                    .lectureName("ㅁㅁㅁㅁㅁㅁㅁ")
                    .build();
            user.setLectures(List.of(l1, l2));

            response.sendRedirect("http://localhost:3000/main?userId=" + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/user")
    public User getUser(@RequestParam String userId) {
        return user;
    }

    @PostMapping("/create-lecture")
    public Lecture createLecture(@RequestParam String lectureName) {
        System.out.println(lectureName);
        Lecture lecture = Lecture.builder()
                .lecture_id(0L)
                .lectureName(lectureName)
                .build();

        Lecture savedLecture = serv.create_lecture(lecture);
        return savedLecture;
    }*/
    /*@PostMapping("/add-assignment")
    public Assignment addAssignments(@RequestParam Long lectureId,
                                     @RequestParam String assignmentName,
                                     @RequestParam Long userId) {
        Assignment assignment = Assignment.builder()
                .assignmentName(assignmentName)
                .assignment_madeby(userId)
                .build();

        return serv.addAssignments(lectureId, assignment);
    }*/

    /*@PostMapping("get-assignments")
    public List<Assignment> getAssignments(@RequestParam Long lectureId) {
        return serv.getAllAssignments(lectureId);
    }*/

    @PostMapping("/submit")
    public String submit(@RequestBody String sourceCode,@RequestParam String args,@RequestParam String xOutput) throws Exception {
        Path tempDir = Files.createTempDirectory("compile_output");
        String code=extractJavaCode(URLDecoder.decode(sourceCode, StandardCharsets.UTF_8.toString()));
        String[] arguments = extractArgs(URLDecoder.decode(args, StandardCharsets.UTF_8.toString())).split("\\s+");
        String expectedOutput = extractXOutput(URLDecoder.decode(xOutput, StandardCharsets.UTF_8.toString()));
        try {
            boolean result = serv.compile(code, tempDir);
            if (result) {
                System.out.println("컴파일 성공");
                String output = serv.test(tempDir,arguments).trim();
                if (expectedOutput.equals(output)) {
                    System.out.println("출력:"+output+"  정답:"+expectedOutput);
                }
            } else {
                System.out.println("Compilation failed");
            }
        } finally {
            Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        String feedback = codeFeedbackService.getCodeFeedback(code);
        return feedback;
        //return feedback.replace("\n", "<br>");
    }

    private String extractJavaCode(String fullCode) {
        int argsIndex = fullCode.indexOf("&args=");
        if (argsIndex != -1) {
            return fullCode.substring("sourceCode=".length(), argsIndex);
        }
        return fullCode.substring("sourceCode=".length());
    }
    public String extractArgs(String args) {
        if (args.startsWith("args=")) {
            return args.substring("args=".length());
        }
        return args; // 이미 형식이 맞는 경우 그대로 반환
    }
    public String extractXOutput(String xOutput) {
        if (xOutput.startsWith("xOutput=")) {
            return xOutput.substring("xOutput=".length());
        }
        return xOutput; // 이미 형식이 맞는 경우 그대로 반환
    }
}
