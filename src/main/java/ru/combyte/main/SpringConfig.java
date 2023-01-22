package ru.combyte.main;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.combyte.beans.AuthBean;
import ru.combyte.interfaces.Authorizer;

@Configuration
@ComponentScan("ru.combyte")
public class SpringConfig {

}
