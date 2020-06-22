package com.dghd.web.schedulizer.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dghd.web.schedulizer.model.account.Registration;

@Controller
public class RegisterController {
	@GetMapping("/register")
	public String registerLanding(Model model) {
		model.addAttribute("registration", new Registration());
		return "account/register";
	}
	
	@PostMapping("/register")
	public String registerSubmit(@ModelAttribute Registration registration) {
		return "account/registerSuccess";
	}
}
