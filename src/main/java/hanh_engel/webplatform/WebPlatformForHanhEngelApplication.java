package hanh_engel.webplatform;

import hanh_engel.webplatform.crosConfig.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebConfig.class)
public class WebPlatformForHanhEngelApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebPlatformForHanhEngelApplication.class, args);
	}

}
