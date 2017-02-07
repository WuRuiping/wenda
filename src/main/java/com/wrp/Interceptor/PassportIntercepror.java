package com.wrp.Interceptor;

import com.wrp.dao.LoginTicketDAO;
import com.wrp.dao.UserDAO;
import com.wrp.model.HostHolder;
import com.wrp.model.LoginTicket;
import com.wrp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by wuruiping on 2017/2/7.
 */
@Component
public class PassportIntercepror implements HandlerInterceptor{

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;


    //调用其他页面之前先调用这个函数
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        //验证浏览器发送过来的ticket
        if(httpServletRequest.getCookies() != null){
           for(Cookie cookie : httpServletRequest.getCookies()){
               if(cookie.getName().equals("ticket")){
                   ticket = cookie.getValue();
                   break;
               }
           }
        }

        if(ticket != null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);//写入user
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
           if(modelAndView !=null && hostHolder.getUser() != null){
               modelAndView.addObject("user",hostHolder.getUser());
           }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
          hostHolder.clear();
    }
}
