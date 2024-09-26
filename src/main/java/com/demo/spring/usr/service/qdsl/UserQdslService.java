package com.demo.spring.usr.service.qdsl;


import com.demo.spring.config.jdbc.util.SearchDTO;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.usr.vo.User;

import java.util.List;

public interface UserQdslService {
	
	public void increaseCntLoginFailr(UserDTO userDTO);
	
	public void initCntLoginFailr(UserDTO userDTO);
	
	public List<User> testSort(SearchDTO searchDTO);
	
}
