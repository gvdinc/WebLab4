package ru.combyte.main;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.combyte.beans.AuthBean;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringConfig.class
        );

        AuthBean authBean = context.getBean("authBean", AuthBean.class);
        System.out.println(authBean);

        context.close();
    }
}
