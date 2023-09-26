package com.yt.ytojbackendjudgeservice.judge;

import com.yt.ytojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.yt.ytojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.yt.ytojbackendjudgeservice.judge.strategy.JudgeContext;
import com.yt.ytojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.yt.ytojbackendmodel.codesandbox.JudgeInfo;
import com.yt.ytojbackendmodel.model.entity.QuestionSubmit;
import com.yt.ytojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
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
