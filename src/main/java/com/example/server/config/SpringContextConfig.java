package com.example.server.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 全鸿润
 * Sring上下文配置类
 */
@Component
@Lazy(false)
public class SpringContextConfig implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextConfig.applicationContext = applicationContext;
    }

    /**
     * 通过类的ClASS类型获取对象实例
     *
     * @param clazz CLASS对象
     * @param <T>   返回的对象类型
     * @return 对象实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过类名获取对象实例
     *
     * @param name 类名
     * @param <T>  返回的对象的类型
     * @return 对象实例
     */
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    @PostConstruct
    public void init(){

    }

}
