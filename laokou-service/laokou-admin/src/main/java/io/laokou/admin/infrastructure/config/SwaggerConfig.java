package io.laokou.admin.infrastructure.config;
import com.google.common.collect.Lists;
import io.laokou.common.constant.Constant;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import java.util.List;
/**
 * Swagger
 * @author Kou Shenhai
 * @date 2019/09/01
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = Lists.newArrayList();
        tokenPar.name(Constant.AUTHORIZATION_HEADER).description(Constant.AUTHORIZATION_HEADER).modelRef(new ModelRef("string")).parameterType(Constant.HEADER).required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //加了 ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，才生成接口文档
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")
                .version("2.0.0")
                .description("后台管理API")
                //作者信息
                .contact(new Contact("寇申海", "https://blog.csdn.net/qq_39893313", "2413176044@qq.com"))
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

}
