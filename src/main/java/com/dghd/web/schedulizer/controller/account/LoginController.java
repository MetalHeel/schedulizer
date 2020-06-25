package com.dghd.web.schedulizer.controller.account;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dghd.web.schedulizer.data.manager.AccountManager;
import com.dghd.web.schedulizer.model.account.Login;
import com.dghd.web.schedulizer.security.LoginInformation;

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
	public String loginSubmit(@ModelAttribute Login login, HttpSession session) {
		// TODO: Inefficient database transactions here. Change or convert when we use better database tech.
		if (!accountManager.isValidLogin(login.getEmailAddress(), login.getPassword())) {
			return "account/loginFailure";
		}
		String accountName = accountManager.getAccountNameForCredentials(login.getEmailAddress(), login.getPassword());
		if (StringUtils.isEmpty(accountName)) {
			return "account/loginFailure";
		}
		LoginInformation loginInformation = new LoginInformation();
		loginInformation.setAccountName(accountName);
		loginInformation.setEmailAddress(login.getEmailAddress());
		session.setAttribute("loginInformation", loginInformation);
		session.setMaxInactiveInterval(3000);
		return "account/loginSuccess";
	}
}
