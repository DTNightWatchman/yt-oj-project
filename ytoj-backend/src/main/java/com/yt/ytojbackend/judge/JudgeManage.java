package com.yt.ytojbackend.judge;

import com.yt.ytojbackend.judge.strategy.DefaultJudgeStrategy;
import com.yt.ytojbackend.judge.strategy.JavaLanguageJudgeStrategy;
import com.yt.ytojbackend.judge.strategy.JudgeContext;
import com.yt.ytojbackend.judge.strategy.JudgeStrategy;
import com.yt.ytojbackend.judge.codesandbox.model.JudgeInfo;
import com.yt.ytojbackend.model.entity.QuestionSubmit;
import com.yt.ytojbackend.model.enums.QuestionSubmitLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * 判题器管理
 */
@Service
public class JudgeManage {

    /**
     * 策略执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage("不支持的语言");
            return judgeInfo;
        }
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (languageEnum.equals(QuestionSubmitLanguageEnum.JAVA)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
