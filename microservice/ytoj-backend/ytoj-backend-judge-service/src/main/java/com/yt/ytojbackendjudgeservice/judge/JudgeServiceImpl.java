package com.yt.ytojbackendjudgeservice.judge;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yt.ytojbackendcommon.common.ErrorCode;
import com.yt.ytojbackendcommon.exception.BusinessException;
import com.yt.ytojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yt.ytojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.yt.ytojbackendjudgeservice.judge.strategy.JudgeContext;
import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeResponse;
import com.yt.ytojbackendmodel.codesandbox.JudgeInfo;
import com.yt.ytojbackendmodel.model.dto.question.JudgeCase;
import com.yt.ytojbackendmodel.model.entity.Question;
import com.yt.ytojbackendmodel.model.entity.QuestionSubmit;
import com.yt.ytojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.yt.ytojbackendmodel.model.vo.QuestionSubmitVO;
import com.yt.ytojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

//    @Resource
//    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManage judgeManage;

    @Value("${codesandbox.type}")
    private String codeSandBoxType;


    private Gson gson = new Gson();


    @Override
    public QuestionSubmitVO doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);

        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 如果不为等待中
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不是等待中，不能判题");
        }
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题状态更新错误");
        }
        // 调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(codeSandBoxType);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = gson.fromJson(judgeCaseStr, new TypeToken<List<JudgeCase>>() {}.getType());
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language).build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        // 根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo = judgeManage.doJudge(judgeContext);
        // 修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(gson.toJson(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新异常");
        }

        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        questionSubmitVO.setJudgeInfo(judgeInfo);
        return questionSubmitVO;
    }


}
