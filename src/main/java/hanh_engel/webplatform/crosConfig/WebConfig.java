package hanh_engel.webplatform.crosConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // Allows ALL origins
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)  // Must be false when allowing all origins
                .maxAge(3600);
    }
}
