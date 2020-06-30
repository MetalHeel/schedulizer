package com.dghd.web.schedulizer.controller.account;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.dghd.web.schedulizer.data.manager.AccountManager;
import com.dghd.web.schedulizer.model.account.Login;
import com.dghd.web.schedulizer.security.manager.SessionManager;
import com.dghd.web.schedulizer.security.sessionAttribute.LoginInformation;

@Controller
public class LoginController {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private SessionManager sessionManager;
	
	@GetMapping("/login")
	public Object loginLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = sessionManager.getLoginInformationFromSession(session);
		if (loginInformation != null) {
			return new RedirectView("/account");
		}
		model.addAttribute("login", new Login());
		model.addAttribute("isLoginError", Boolean.FALSE);
		return "account/login";
	}
	
	@PostMapping("/login")
	public Object loginSubmit(HttpSession session, Model model, @ModelAttribute Login login) {
		LoginInformation loginInformation = sessionManager.getLoginInformationFromSession(session);
		if (loginInformation != null) {
			return new RedirectView("/account");
		}
		String accountName = null;
		try {
			accountName = accountManager.getAccountNameForCredentials(login.getEmailAddress(), login.getPassword());
		} catch (Throwable t) {
			model.addAttribute("isLoginError", Boolean.TRUE);
			model.addAttribute("errorMessage", "There was a problem logging in. Please try again.");
			return "account/login";
		}
		if (StringUtils.isEmpty(accountName)) {
			model.addAttribute("isLoginError", Boolean.TRUE);
			model.addAttribute("errorMessage", "No account found for this email/password combination.");
			return "account/login";
		}
		loginInformation = new LoginInformation();
		loginInformation.setAccountName(accountName);
		loginInformation.setEmailAddress(login.getEmailAddress());
		session.setAttribute("loginInformation", loginInformation);
		session.setMaxInactiveInterval(3000);
		return new RedirectView("/account");
	}
}
