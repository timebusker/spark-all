package com.timebusker.interceptor;

import org.hibernate.EmptyInterceptor;

import java.io.Serializable;

/**
 * hibernate phoenix 数据源拦截器
 *
 * @Description:Configuration
 * @Author:Administrator
 * @Date2019/11/14 20:40
 **/
public class PhoenixInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 2981942639232722933L;

    /**
     * 拦截sql模版
     */
    @Override
    public String onPrepareStatement(String sql) {
        System.err.println("拦截SQL是：" + sql);
        return super.onPrepareStatement(sql);
    }

    /**
     * 拦截实体获取
     */
    @Override
    public Object getEntity(String entityName, Serializable id) {
        System.err.println("拦截实体信息是：" + entityName + "\t" + id);
        return super.getEntity(entityName, id);
    }

}
