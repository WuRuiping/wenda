package com.wrp.service;

import com.wrp.dao.QuestionDAO;
import com.wrp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wuruiping on 2017/2/3.
 */
@Service
public class QuestionService {

    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId,int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
