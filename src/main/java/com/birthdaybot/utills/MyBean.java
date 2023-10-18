package com.birthdaybot.utills;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

    @Getter
    private static ApplicationContext applicationContext;

    public MyBean(ApplicationContext applicationContext) {
        MyBean.applicationContext = applicationContext;
    }

}