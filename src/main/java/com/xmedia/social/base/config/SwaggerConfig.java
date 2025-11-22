package com.xmedia.social.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Xmedia Social Media Integration API",
                version = "1.0.0",
                description = "RESTful API for Instagram and Facebook integration services. " +
                        "This API provides endpoints for fetching Instagram posts, comments, " +
                        "managing Facebook OAuth tokens, and accessing social media data.",
                contact = @Contact(
                        name = "Xmedia API Support",
                        email = "support@xmedia.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                ),
                @Server(
                        url = "https://api.xmedia.com",
                        description = "Production Server"
                )
        }
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addTagsItem(new Tag()
                        .name("Instagram")
                        .description("Instagram media and posts management endpoints"))
                .addTagsItem(new Tag()
                        .name("Instagram Comments")
                        .description("Instagram comments retrieval and management"))
                .addTagsItem(new Tag()
                        .name("Facebook")
                        .description("Facebook OAuth and page management endpoints"));
    }
}

