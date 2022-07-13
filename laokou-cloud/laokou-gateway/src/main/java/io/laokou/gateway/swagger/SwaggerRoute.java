package io.laokou.gateway.swagger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;
/***
 * @author Kou Shenhai
 */
@Data
@Configuration
@ConfigurationProperties("laokou.swagger")
public class SwaggerRoute {
    private List<SwaggerRouteProperties> routes;
}
