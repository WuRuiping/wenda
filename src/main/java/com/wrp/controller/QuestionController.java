package com.wrp.controller;

import com.wrp.model.HostHolder;
import com.wrp.model.Question;
import com.wrp.service.QuestionService;
import com.wrp.service.UserService;
import com.wrp.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.util.Date;
import java.util.List;

/**
 * Created by wuruiping on 2017/2/7.
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try {
             Question question = new Question();
             question.setTitle(title);
             question.setContent(content);
             question.setCreatedDate(new Date());
             question.setCommentCount(0);
             if(hostHolder.getUser() == null){
                     question.setUserId(WendaUtil.ANONYMOUS_USERID);
             }else{
                 question.setUserId(hostHolder.getUser().getId());
             }
            if(questionService.addQuestion(question) > 0){
                 return WendaUtil.getJSONString(0);

            }

        }catch (Exception e){
            logger.error("增加题目失败"+e.getMessage());

        }
        return WendaUtil.getJSONString(1,"增加题目失败");

    }

   @RequestMapping(value = "question/{qid}",method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid){
        Question question = questionService.getById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));
        return "detail";
   }

}
