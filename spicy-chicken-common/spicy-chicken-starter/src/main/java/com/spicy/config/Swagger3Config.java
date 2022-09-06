package com.spicy.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.Set;

@Configuration
@EnableOpenApi
public class Swagger3Config extends WebMvcConfigurationSupport {

    /**
     * Add swagger-ui.html resource
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //Add Swagger Configuration Resource

        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/swagger-resource/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-resources/");

        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v3/api-docs/");

        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");

    }

    @Bean
    public Docket baseDocket() {

        return createRestApi("SpicyChicken", "com.spicy.system");
    }

    /**
     * Create Swagger Group And ApiPage
     */
    private Docket createRestApi(String groupName, String basePackages) {

        return new Docket(DocumentationType.OAS_30)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackages))
                .apis(RequestHandlerSelectors.withClassAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .protocols(Set.of("Https", "Http"))
                .securitySchemes(Collections.singletonList(securityScheme()));
    }

    /**
     * Create Swagger Description Info
     */
    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Pleas taste spicy chicken 🐤")
                .description("Delicious food 🌰")
                .contact(new Contact("SpicyChicken-SmallPartyDish",
                        "https://github.com/aselince",
                        "aselince0518@gmail.com"))
                .version("V1.0.0")
                .build();
    }

    /**
     * Create SecurityScheme
     * <p>
     * This method can be used check Authentication
     */
    private SecurityScheme securityScheme() {

        return new ApiKey("X-Access-Token", "X-Access-Token", "header");
    }

}
