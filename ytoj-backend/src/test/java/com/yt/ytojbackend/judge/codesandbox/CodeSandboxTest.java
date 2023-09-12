package com.yt.ytojbackend.judge.codesandbox;

import com.yt.ytojbackend.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import com.yt.ytojbackend.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CodeSandboxTest {

    @Test
    void executeCode() {
        CodeSandbox codeSandBox = new ExampleCodeSandbox();
        String code = "int main() {}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();

        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);

        Assertions.assertNotNull(executeCodeResponse);
    }
}