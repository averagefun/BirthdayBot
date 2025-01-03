package com.birthdaybot.configuration;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
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
