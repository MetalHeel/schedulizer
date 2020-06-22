package com.dghd.web.schedulizer.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("controllerName", this.getClass().getSimpleName());
		return "account/login";
	}
}
