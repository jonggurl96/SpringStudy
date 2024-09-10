package com.demo.spring.usr.web;


import com.demo.spring.config.security.annotation.AuthenticatedUser;
import com.demo.spring.config.security.annotation.RsaAesReady;
import com.demo.spring.usr.dto.UserDTO;
import com.demo.spring.web.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController extends AbstractController {
	
	@RsaAesReady
	@SuppressWarnings({"unused"})
	@GetMapping(value = "/login")
	public String loginPage(Model model,
	                        HttpSession session) throws Exception {
		Object jsessionId = session.getAttribute("JSESSIONID");
		if(jsessionId != null)
			session.setAttribute("JSESSIONID", null);
		
		return "/login";
	}
	
	@PostMapping("/loginError")
	public String redirectLoginPage(@RequestParam("code") String code,
	                                @RequestParam("errMsg") String errMsg,
	                                RedirectAttributes rttr) {
		rttr.addFlashAttribute("code", code)
		    .addFlashAttribute("errMsg", errMsg);
		return "redirect:/login";
	}
	
	@GetMapping("/main")
	public String mainPage(@AuthenticatedUser UserDTO userDTO, Model model, HttpServletRequest request) throws Exception {
		log.debug(">>> User access /main. {}", userDTO.getUserNo());
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("jwt", request.getHeader("Authorization"));
		return "/main";
	}
	
}
