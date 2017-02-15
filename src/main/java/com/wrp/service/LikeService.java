package com.wrp.service;

import com.wrp.util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wuruiping on 2017/2/10.
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;
}
