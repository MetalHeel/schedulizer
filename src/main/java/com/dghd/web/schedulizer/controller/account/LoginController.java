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
import com.dghd.web.schedulizer.security.LoginInformation;

@Controller
public class LoginController {
	@Autowired
	private AccountManager accountManager;
	
	@GetMapping("/login")
	public Object loginLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = null;
		try {
			loginInformation = (LoginInformation)session.getAttribute("loginInformation");
		} catch (Throwable t) {
			// Invalid session. No need to do anything.
		}
		if (loginInformation != null) {
			return new RedirectView("/account");
		}
		model.addAttribute("login", new Login());
		model.addAttribute("isLoginError", Boolean.FALSE);
		return "account/login";
	}
	
	// TODO: Manual URL manipulation protection.
	@PostMapping("/login")
	public Object loginSubmit(HttpSession session, Model model, @ModelAttribute Login login) {
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
		LoginInformation loginInformation = new LoginInformation();
		loginInformation.setAccountName(accountName);
		loginInformation.setEmailAddress(login.getEmailAddress());
		session.setAttribute("loginInformation", loginInformation);
		session.setMaxInactiveInterval(3000);
		return new RedirectView("/account");
	}
}
