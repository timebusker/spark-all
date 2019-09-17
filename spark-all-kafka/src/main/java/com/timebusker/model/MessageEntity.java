package com.timebusker.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

/**
 * @DESC:MessageEntity
 * @author:timebusker
 * @date:2019/9/17
 */
public class MessageEntity implements Serializable {

    private long id;

    private Object context;

    private Date date;

    public MessageEntity(Object context) {
        this.id = System.currentTimeMillis();
        this.context = context;
        this.date = new Date();
    }

    public long getId() {
        return id;
    }


    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
