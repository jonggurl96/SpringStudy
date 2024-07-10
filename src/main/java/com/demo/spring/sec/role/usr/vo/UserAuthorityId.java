package com.demo.spring.sec.role.usr.vo;


import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorityId implements Serializable {
	
	@Column(name = "user_no")
	private String userNo;
	
	@Column(name = "author_code")
	private String authorCode;
}
