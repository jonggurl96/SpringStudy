package com.demo.spring.usr.repository.qdsl;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.vo.User;

import java.util.List;

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
	
	public List<User> testSort(SearchDTO searchDTO);
}
