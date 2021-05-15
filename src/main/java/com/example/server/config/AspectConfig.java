package com.example.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 全鸿润
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.example.server.util.aspect")
public class AspectConfig {

}
