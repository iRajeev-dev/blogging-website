package blogging;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "My Blog REST APIs",
				description = "Spring boot blog app REST APIs documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Rajeev Singh",
						email = "myblog@email.in",
						url = "https://myblog.com/contact_me"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://myblog.com/license"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Documentation on Github",
				url = "https://github.com/rajeev/my-blog-rest-api"
		)
)
public class SpringbootBlogRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

}
