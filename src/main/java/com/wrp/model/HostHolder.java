package com.wrp.model;

import org.springframework.stereotype.Component;

/**
 * Created by wuruiping on 2017/2/7.
 */
@Component
public class HostHolder {

    //局部线程变量
    //每个线程自己拥有的变量
    private static  ThreadLocal<User> users = new ThreadLocal<>();

    public  User getUser() {
        return users.get();
    }

    public void setUser(User user) {
       users.set(user);
    }

    public  void clear(){
        users.remove();
    }
}
