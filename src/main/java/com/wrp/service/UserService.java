package com.wrp.service;

import com.wrp.controller.HomeController;
import com.wrp.dao.UserDAO;
import com.wrp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wuruiping on 2017/2/3.
 */
@Service
public class UserService {

    private static  final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
