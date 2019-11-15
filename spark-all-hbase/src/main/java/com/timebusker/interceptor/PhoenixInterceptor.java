package com.timebusker.interceptor;

import org.hibernate.EmptyInterceptor;

import java.io.Serializable;

/**
 * hibernate phoenix 数据源拦截器
 *
 * @author yulinying
 * @since 2019-10-26
 */
public class PhoenixInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 2981942639232722933L;

    /**
     * 拦截sql模版
     */
    @Override
    public String onPrepareStatement(String sql) {
        //System.out.println("interceptor-SQL: " + sql);
        return super.onPrepareStatement(sql);
    }
    
    /**
     * 拦截实体获取
     */
    @Override
    public Object getEntity(String entityName, Serializable id) {
        //System.out.println("PhoenixInterceptor-entityName: " + entityName);
        return super.getEntity(entityName, id);
    }

}
