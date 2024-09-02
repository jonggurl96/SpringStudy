package com.demo.spring.config.security.annotation;


import com.demo.spring.usr.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : userDTO")
@RequestBody(
		description = "사용자 정보",
		content = {
				@Content(
						schema = @Schema(
								implementation = UserDTO.class,
								requiredMode = Schema.RequiredMode.NOT_REQUIRED
						)
				)
		}
)
public @interface AuthenticatedUser {
}
