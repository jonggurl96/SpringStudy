package com.demo.spring.sec.role.usr.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tn_user_authority")
@IdClass(UserAuthorityId.class)
@Schema(description = "사용자 권한 목록")
public class UserAuthority {

	@Id
	@Schema(description = "사용자 번호")
	private String userNo;
	
	@Id
	@Schema(description = "권한 코드")
	private String authorCode;
}
