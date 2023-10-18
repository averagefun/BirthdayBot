package com.birthdaybot.utills;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//Class for training config
@Configuration
@ComponentScan("com.birthdaybot")//no needed
public class SpringConfig {

//    @Bean
//    public MessageConfig messageConfig(){
//        return new MessageConfig();
//    }
//    @Bean
//    public AddCommand addCommand(){
//    return new AddCommand();
//    } We can use instead @Component in class
    @Getter
    private static ApplicationContext applicationContext;


    public SpringConfig(ApplicationContext applicationContext) {
        SpringConfig.applicationContext = applicationContext;
    }
}
