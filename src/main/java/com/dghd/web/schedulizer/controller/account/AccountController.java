package com.dghd.web.schedulizer.controller.account;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dghd.web.schedulizer.security.LoginInformation;

@Controller
public class AccountController {
	@RequestMapping("/account")
	public String accountLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = null;
		try {
			loginInformation = (LoginInformation)session.getAttribute("loginInformation");
		} catch (Throwable t) {
			// Invalid session. No need to do anything.
		}
		if (loginInformation != null) {
			model.addAttribute("accountName", loginInformation.getAccountName());
			return "account/account";
		} else {
			return "oops";
		}
	}
}
