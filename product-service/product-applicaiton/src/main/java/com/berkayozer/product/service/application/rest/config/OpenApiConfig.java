package com.berkayozer.product.service.application.rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "N11 Product Service API",
                description = "Katalog, Kategori ve Ürün Yönetimi Servisi (Take My Order Platform)",
                version = "1.0",
                contact = @Contact(
                        name = "Berkay Özer",
                        email = "berkay@example.com",
                        url = "https://github.com/OzerBerkay"
                )
        ),
        // Tüm API'ler varsayılan olarak bu güvenliği isteyecek (Public olanları Controller'da ezeceğiz)
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Token. Lütfen 'Bearer ' öneki koymadan sadece token'ı yapıştırın.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Tamamen Anotasyon bazlı konfigürasyon
}