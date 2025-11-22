# Swagger Setup (Simple Steps)

## ✅ 1. Add Swagger Dependency

Add the following dependency in your **pom.xml**:

``` xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

------------------------------------------------------------------------

## ✅ 2. Create Swagger Configuration File

Create the file:

`src/main/java/com/example/instagram/config/SwaggerConfig.java`

``` java
package com.example.instagram.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Documentation",
                version = "1.0",
                description = "Swagger OpenAPI documentation"
        )
)
public class SwaggerConfig {
}
```

------------------------------------------------------------------------

## ✅ 3. Run Your Spring Boot Application

    mvn spring-boot:run

or

    java -jar target/*.jar

------------------------------------------------------------------------

## ✅ 4. Open Swagger UI

After the application starts, open:

👉 **http://localhost:8080/swagger-ui/index.html**
