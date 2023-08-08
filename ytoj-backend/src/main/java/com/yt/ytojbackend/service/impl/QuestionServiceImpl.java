package com.yt.ytojbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.ytojbackend.model.entity.Question;
import com.yt.ytojbackend.service.QuestionService;
import com.yt.ytojbackend.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【question(题目表)】的数据库操作Service实现
* @createDate 2023-08-08 00:11:03
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




