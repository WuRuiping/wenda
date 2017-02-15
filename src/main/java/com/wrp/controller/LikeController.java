package com.wrp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wuruiping on 2017/2/10.
 */
@Controller
public class LikeController {

    @RequestMapping(path = {"like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commitId") int commentId){
       return "";
    }
}
