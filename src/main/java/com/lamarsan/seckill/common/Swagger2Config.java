package com.lamarsan.seckill.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * className: Swagger2Config
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/11 16:35
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Value("${swagger.enabled}")
    private boolean enable;

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2).enable(enable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lamarsan.seckill.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("秒杀项目")
                .description("秒杀项目RestAPI文档")
                .termsOfServiceUrl("...")
                .version("1.0")
                .build();
    }


}
