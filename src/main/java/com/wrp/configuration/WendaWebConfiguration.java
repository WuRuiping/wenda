package com.wrp.configuration;

import com.wrp.Interceptor.PassportIntercepror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by wuruiping on 2017/2/7.
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter{

    @Autowired
    PassportIntercepror passportIntercepror;


    @Override
    public  void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(passportIntercepror);
        super.addInterceptors(registry);
    }
}
