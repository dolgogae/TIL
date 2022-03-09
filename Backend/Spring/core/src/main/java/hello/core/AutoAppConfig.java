package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
    basePackages = "hello.core", // 해당 패키지 밑에서만 스캔
    basePackageClasses = AutoAppConfig.class, // 해당 클래스의 패키지 밑에서 스캔
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig{

}