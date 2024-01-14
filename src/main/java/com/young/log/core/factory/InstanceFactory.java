package com.young.log.core.factory;

/**
 * @author: Young
 * @description: 日志上下文实例工厂接口
 **/
public interface InstanceFactory <T, V>{

    /**
     * 获取实例接口
     * @param v
     * @return
     * @throws Exception
     */
    T getInstance(V v) throws Exception;
}
