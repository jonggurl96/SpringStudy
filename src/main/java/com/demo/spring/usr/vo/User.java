package com.demo.spring.usr.vo;


import com.demo.spring.config.jdbc.qdsl.annotation.ClassAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tn_users")
@Schema(description = "tn_user 엔티티")
@ClassAlias("user")
@Alias("user")
public class User {
	
	@Id
	@Schema(description = "사용자 번호")
	private String userNo;
	
	@Schema(description = "사용자 ID")
	private String userId;
	
	@Schema(description = "사용자 명")
	private String username;
	
	@Schema(description = "사용자 암호")
	private String password;
	
	@Schema(description = "로그인 실패 횟수")
	private int cntLoginFailr;
}
