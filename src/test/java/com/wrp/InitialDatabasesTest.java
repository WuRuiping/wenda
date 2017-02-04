package com.wrp;

import com.wrp.WendaApplication;
import com.wrp.dao.QuestionDAO;
import com.wrp.dao.UserDAO;
import com.wrp.model.Question;
import com.wrp.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by wuruiping on 2017/1/30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitialDatabasesTest{

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Test
    public  void initDatabase(){

        Random random = new Random();

        for(int i=0;i<10;i++){
            User user = new User();
            user.setName(String.format("User%d",i));
            user.setPassword("");
            user.setSalt("");
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            userDAO.addUser(user);

            user.setPassword("password");
            userDAO.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("Title{%d}",i));
            question.setContent(String.format("Content %d",i));
            questionDAO.addQuestion(question);


        }

    }

}
