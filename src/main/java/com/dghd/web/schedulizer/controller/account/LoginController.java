package com.dghd.web.schedulizer.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dghd.web.schedulizer.data.manager.AccountManager;
import com.dghd.web.schedulizer.model.account.Login;

@Controller
public class LoginController {
	@Autowired
	private AccountManager accountManager;
	
	@GetMapping("/login")
	public String loginLanding(Model model) {
		model.addAttribute("login", new Login());
		return "account/login";
	}
	
	@PostMapping("/login")
	public String loginSubmit(@ModelAttribute Login login) {
		if (!accountManager.isValidLogin(login.getEmailAddress(), login.getPassword())) {
			return "account/loginFailure";
		}
		return "account/loginSuccess";
	}
}
