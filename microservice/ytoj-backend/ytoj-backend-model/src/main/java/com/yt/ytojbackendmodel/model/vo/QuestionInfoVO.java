package com.yt.ytojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yt.ytojbackendmodel.model.dto.question.JudgeCase;
import com.yt.ytojbackendmodel.model.dto.question.JudgeConfig;
import com.yt.ytojbackendmodel.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 题目VO
 *
 * @TableName question
 */
@Data
public class QuestionInfoVO implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 标签列表
     */
    private List<String> tags;


    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;


    /**
     * 判题用例
     */
    private ArrayList<JudgeCase> judgeCase;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;



    private static final long serialVersionUID = 1L;

    private static Gson gson = new Gson();

    /**
     * 包装类转对象
     *
     * @param questionInfoVO
     * @return
     */
    public static Question voToObj(QuestionInfoVO questionInfoVO) {
        if (questionInfoVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionInfoVO, question);
        List<String> tagList = questionInfoVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig judgeConfig = questionInfoVO.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        ArrayList<JudgeCase> judgeCase = questionInfoVO.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(gson.toJson(judgeCase));
        }
        return question;
    }
    


    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionInfoVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionInfoVO questionInfoVO = new QuestionInfoVO();
        BeanUtils.copyProperties(question, questionInfoVO);
        List<String> tags = JSONUtil.toList(question.getTags(), String.class);
        questionInfoVO.setTags(tags);
        questionInfoVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        questionInfoVO.setJudgeCase(gson.fromJson(question.getJudgeCase(), new TypeToken<List<JudgeCase>>(){}.getType()));
        return questionInfoVO;
    }
}