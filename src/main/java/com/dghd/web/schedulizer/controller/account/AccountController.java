package com.dghd.web.schedulizer.controller.account;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dghd.web.schedulizer.security.manager.SessionManager;
import com.dghd.web.schedulizer.security.sessionAttribute.LoginInformation;

@Controller
public class AccountController {
	@Autowired
	private SessionManager sessionManager;
	
	@RequestMapping("/account")
	public String accountLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = sessionManager.getLoginInformationFromSession(session);
		if (loginInformation != null) {
			model.addAttribute("accountName", loginInformation.getAccountName());
			return "account/account";
		} else {
			return "oops";
		}
	}
}
