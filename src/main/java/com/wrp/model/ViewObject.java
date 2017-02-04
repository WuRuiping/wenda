package com.wrp.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;
import java.util.Objects;

/**
 * Created by wuruiping on 2017/2/3.
 */
public class ViewObject {

    private Map<String,Object>  objs = new HashedMap();

    public void set(String key, Object value){
        objs.put(key, value);
    }

    public Object get(String key){
        return objs.get(key);
    }
}
