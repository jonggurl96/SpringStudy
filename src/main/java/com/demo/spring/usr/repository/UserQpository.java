package com.demo.spring.usr.repository;


import com.demo.spring.usr.dto.UserDTO;

/**
 * UserQpository.java
 * <pre>
 * Querl DSL Repository for User Entity
 * </pre>
 *
 * @author jongg
 * @version 1.0.0
 * @since 24. 9. 2.
 */
public interface UserQpository {
	
	public void increaseCntLoginFailr(UserDTO userDTO);
	
	public void initCntLoginFailr(UserDTO userDTO);
}
