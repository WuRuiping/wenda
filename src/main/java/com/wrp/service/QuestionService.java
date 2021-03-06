package com.wrp.service;

import com.wrp.dao.QuestionDAO;
import com.wrp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import sun.plugin.dom.html.HTMLUListElement;

import java.util.List;

/**
 * Created by wuruiping on 2017/2/3.
 */
@Service
public class QuestionService {

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addQuestion(Question question){

        //敏感词过滤
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));

        //html标签的过滤
         question.setContent(HtmlUtils.htmlEscape(question.getContent()));
         question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        return questionDAO.addQuestion(question) > 0 ?question.getId():0;

    }

    public List<Question> getLatestQuestions(int userId,int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public Question getById(int id){
        return questionDAO.getById(id);
    }
}
