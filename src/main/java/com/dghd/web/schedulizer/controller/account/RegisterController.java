package com.dghd.web.schedulizer.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {
	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("controllerName", this.getClass().getSimpleName());
		return "account/register";
	}
}
