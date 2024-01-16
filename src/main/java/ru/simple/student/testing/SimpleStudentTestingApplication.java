package ru.simple.student.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import ru.simple.student.testing.model.Question;
import ru.simple.student.testing.model.TestResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SimpleStudentTestingApplication {

    @Value("${csv.file}")
    private String csvFilePath;

    @Value("${number.correct.answers}")
    private int numberOfCorrectAnswers;

    @Value("${csv.paginator}")
    private String scvPaginator;

    @Autowired
    private ResourceLoader resourceLoader;

    public static void main(String[] args) {
        SpringApplication.run(SimpleStudentTestingApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Please enter your last name:");
            Scanner scanner = new Scanner(System.in);
            String lastName = scanner.nextLine();

            System.out.println("Please enter your first name:");
            String firstName = scanner.nextLine();

            List<Question> questions = getQuestions(csvFilePath);
            int correctAnswers = 0;
            for (Question question : questions) {
                System.out.println(question.getQuestion());
                System.out.println("1: " + question.getAnswer1());
                System.out.println("2: " + question.getAnswer2());
                System.out.println("3: " + question.getAnswer3());
                System.out.println("4: " + question.getAnswer4());

                String userAnswer = scanner.nextLine();
                String actualQuestion = question.getCorrectAnswer();
                if (actualQuestion.equals(userAnswer)) {
                    correctAnswers++;
                }
            }

            TestResult testResult = new TestResult(lastName, firstName, correctAnswers);
            System.out.println("Test Result:");
            System.out.println("Last Name: " + testResult.lastName());
            System.out.println("First Name: " + testResult.firstName());
            System.out.println("Correct Answers: " + testResult.correctAnswers());
            System.out.println((numberOfCorrectAnswers <= testResult.correctAnswers() ? "Test success" : "Test failed"));
        };
    }

    public List<Question> getQuestions(String filePath) throws IOException {
        List<Question> resultList = new ArrayList<>();
        Path resourcePath = Paths.get(filePath);
        BufferedReader reader = Files.newBufferedReader(resourcePath);
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(scvPaginator);
            Question question = Question.builder()
                    .question(values[0])
                    .answer1(values[1])
                    .answer2(values[2])
                    .answer3(values[3])
                    .answer4(values[4])
                    .correctAnswer(values[5])
                    .build();
            resultList.add(question);
        }
        reader.close();
        return resultList;
    }
}
