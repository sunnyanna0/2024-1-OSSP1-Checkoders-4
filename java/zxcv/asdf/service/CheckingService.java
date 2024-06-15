package zxcv.asdf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zxcv.asdf.domain.Answer;
import zxcv.asdf.domain.LectureAssignment;
import zxcv.asdf.repository.AnswerRepository;
import zxcv.asdf.repository.LectureAssignmentRepository;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class CheckingService {

    private final CodeFeedbackService codeFeedbackService;
    private final LectureAssignmentRepository lectureAssignmentRepository;
    private final AnswerRepository answerRepository;

    public String submit(String sourceCode, Long assignmentId, Answer answer) throws Exception {
        Path tempDir = Files.createTempDirectory("compile_output");
        String code = URLDecoder.decode(sourceCode, StandardCharsets.UTF_8.toString());

        LectureAssignment assignment = lectureAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        try {
            boolean result = compile(code, tempDir);
            if (result) {
                System.out.println("컴파일 성공");

                String[] inputs = {assignment.getHwTest1(), assignment.getHwTest2(), assignment.getHwTest3(), assignment.getHwTest4(), assignment.getHwTest5()};
                String[] expectedOutputs = {assignment.getHwTestAnswer1(), assignment.getHwTestAnswer2(), assignment.getHwTestAnswer3(), assignment.getHwTestAnswer4(), assignment.getHwTestAnswer5()};

                for (int i = 0; i < inputs.length; i++) {
                    if (inputs[i] != null && expectedOutputs[i] != null) {
                        System.out.println("테스트 케이스 " + (i + 1) + ": 입력 = " + inputs[i] + ", 기대 출력 = " + expectedOutputs[i]);
                        String output = test(tempDir, inputs[i]).trim();
                        System.out.println("실제 출력: " + output);
                        if (!expectedOutputs[i].equals(output)) {
                            System.out.println("출력: " + output + "  정답: " + expectedOutputs[i]);
                            answer.setCorrect(false);
                            answerRepository.save(answer);
                            return "오답입니다! 테스트 케이스 " + (i + 1) + "에서 실패했습니다.";
                        } else {
                            System.out.println("테스트 케이스 " + (i + 1) + " 통과");
                        }
                    }
                }
                System.out.println("모든 테스트 통과");
                answer.setCorrect(true);
                answerRepository.save(answer);
                return "정답입니다!";
            } else {
                System.out.println("Compilation failed");
                answer.setCorrect(false);
                answerRepository.save(answer);
                return "컴파일 실패!";
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
    }

    public boolean compile(String sourceCode, Path outputpath) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<String> options = Arrays.asList("-d", outputpath.toString());
        JavaFileObject file = new InMemoryJavaFileObject("Hello", sourceCode); // public class 이름
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
        boolean result = task.call();
        if (!result) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.err.println("Error on line " + diagnostic.getLineNumber() + " in " + diagnostic.getSource().toUri());
                System.err.println("Error message: " + diagnostic.getMessage(null));
            }
        }
        return result;
    }

    static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        final String code;

        InMemoryJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    public String test(Path tempDir, String input) throws Exception {
        System.out.println("테스트 메소드 시작");
        System.out.println("전달된 input: " + input);

        // Check if the compiled class file exists
        Path classFilePath = Paths.get(tempDir.toString(), "Hello.class");
        if (!Files.exists(classFilePath)) {
            throw new RuntimeException("Compiled class file not found: " + classFilePath);
        }

        URLClassLoader classLoader = URLClassLoader.newInstance(
                new URL[]{tempDir.toUri().toURL()},
                this.getClass().getClassLoader()
        );

        Class<?> clazz = Class.forName("Hello", true, classLoader);
        Method method = clazz.getMethod("main", String[].class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(bos));

        try {
            method.invoke(null, (Object) new String[]{input});
            System.setOut(originalOut);
            String result = bos.toString(StandardCharsets.UTF_8).trim();
            System.out.println("테스트 메소드 종료, 출력 결과: " + result);
            return result;
        } finally {
            System.setOut(originalOut);
        }
    }
}
