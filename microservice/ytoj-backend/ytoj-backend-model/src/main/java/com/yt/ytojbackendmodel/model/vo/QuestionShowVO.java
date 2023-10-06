package com.yt.ytojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yt.ytojbackendmodel.model.dto.question.JudgeCase;
import com.yt.ytojbackendmodel.model.dto.question.JudgeConfig;
import com.yt.ytojbackendmodel.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 题目VO
 *
 * @TableName question
 */
@Data
public class QuestionShowVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 标签列表
     */
    private List<String> tags;


    /**
     * 通过数
     */
    private String acceptedRate;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建时间
     */
    private Date createTime;



    private static final long serialVersionUID = 1L;

    private static Gson gson = new Gson();

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionShowVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionShowVO questionShowVO = new QuestionShowVO();
        BeanUtils.copyProperties(question, questionShowVO);
        List<String> tags = JSONUtil.toList(question.getTags(), String.class);
        questionShowVO.setTags(tags);
        Integer acceptedNum = question.getAcceptedNum();
        Integer submitNum = question.getSubmitNum();
        double percentage = submitNum == 0 ? 0 : Double.valueOf(acceptedNum) / submitNum;

        // 创建DecimalFormat对象来格式化百分比
        DecimalFormat df = new DecimalFormat("0.00%");
        String formattedPercentage = df.format(percentage);
        questionShowVO.setAcceptedRate(formattedPercentage);
        return questionShowVO;
    }
}