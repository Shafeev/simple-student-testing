package ru.simple.student.testing.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Question {
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer;


}
