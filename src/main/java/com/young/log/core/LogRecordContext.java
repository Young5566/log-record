package com.young.log.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author: Young
 * @description: 日志记录上下文
 **/
public class LogRecordContext {

    private static TransmittableThreadLocal<Stack<Map<String, Object>>> threadLocal = new TransmittableThreadLocal<>();


    private static void push(Map<String, Object> maps){
        Stack<Map<String, Object>> mapStack = threadLocal.get();
        if(mapStack == null){
            mapStack = new Stack<>();
            threadLocal.set(mapStack);
        }
        mapStack.push(maps);
    }

    private static Map<String, Object> pop(){
        return threadLocal.get().pop();
    }

    private static Map<String, Object> peek(){
        return threadLocal.get().peek();
    }

    private static Object get(String key){
        return peek().get(key);
    }

    private static void putValue(String key, Object value){
        peek().put(key, value);
    }

    public static void putValue(String key1, String value1, String key2, String value2){
        peek().put(key1, value1);
        peek().put(key2, value2);
    }

    public static Map<String, Object> getVariables(){
        return peek();
    }

    public static void pushEmptySpan(){
        push(new HashMap<>());
    }

    public static void clear(){
        pop();
        if(CollectionUtils.isEmpty(threadLocal.get())){
            threadLocal.remove();
        }
    }

}
