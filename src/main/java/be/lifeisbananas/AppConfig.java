package be.lifeisbananas;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("be.lifeisbananas")
@EnableAspectJAutoProxy
public class AppConfig {
}
