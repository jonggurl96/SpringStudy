package com.demo.spring.usr.dto;


import com.demo.spring.usr.vo.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User DTO")
public class UserDTO {
	
	private String userNo;
	
	private String userId;
	
	private String username;
	
	private String pwd;
	
	public UserDTO(User user) {
		this.userNo = user.getUserNo();
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.pwd = user.getPassword();
	}
	
}
