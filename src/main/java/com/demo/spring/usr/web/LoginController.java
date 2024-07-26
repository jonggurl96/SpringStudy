package com.demo.spring.usr.web;


import com.demo.spring.config.security.annotation.AuthenticationUser;
import com.demo.spring.config.security.annotation.RsaEncrypt;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.web.AbstractController;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class LoginController extends AbstractController {
	
	@RsaEncrypt
	@GetMapping(value = "/login")
	public String loginPage(Model model,
	                        HttpSession session,
	                        @RequestParam(value = "code", required = false) String code,
	                        @RequestParam(value = "errMsg", required = false) String errMsg) throws Exception {
		Object jsessionId = session.getAttribute("JSESSIONID");
		if(jsessionId != null)
			session.setAttribute("JSESSIONID", null);
		if(code != null)
			log.debug(">>> code: {}, errMsg: {}", code, errMsg);
		model.addAttribute("errMsg", errMsg);
		
		return "/login";
	}
	
	@PostMapping(value = "/actionLogin")
	public String login() throws Exception {
		return "/main";
	}
	
	@GetMapping("/main")
	public String mainPage(@AuthenticationUser UserDTO userDTO) throws Exception {
		log.debug(">>> main. {}", userDTO);
		return "/main";
	}
	
}
