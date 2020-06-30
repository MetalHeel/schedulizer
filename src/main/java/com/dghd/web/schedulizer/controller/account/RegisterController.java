package com.dghd.web.schedulizer.controller.account;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.dghd.web.schedulizer.data.manager.AccountManager;
import com.dghd.web.schedulizer.model.account.Registration;
import com.dghd.web.schedulizer.security.LoginInformation;

@Controller
public class RegisterController {
	@Autowired
	private AccountManager accountManager;
	
	@GetMapping("/register")
	public Object registerLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = null;
		try {
			loginInformation = (LoginInformation)session.getAttribute("loginInformation");
		} catch (Throwable t) {
			// Invalid session. No need to do anything.
		}
		if (loginInformation != null) {
			return new RedirectView("/account");
		}
		model.addAttribute("registration", new Registration());
		model.addAttribute("isRegistrationError", Boolean.FALSE);
		return "account/register";
	}
	
	@PostMapping("/register")
	public Object registerSubmit(HttpSession session, Model model, @ModelAttribute Registration registration) {
		try {
			if (!accountManager.validateRegistration(registration)) {
				model.addAttribute("isRegistrationError", Boolean.TRUE);
				model.addAttribute("errorMessage", "Account name and email combo already exists!");
				return "account/register";
			}
			accountManager.createAccount(registration.getAccountName(), registration.getEmailAddress(), registration.getPassword(), registration.getAccountType());
		} catch (Throwable t) {
			model.addAttribute("isRegistrationError", Boolean.TRUE);
			model.addAttribute("errorMessage", "There was a problem creating the account. Please try again.");
			return "account/register";
		}
		LoginInformation loginInformation = new LoginInformation();
		loginInformation.setAccountName(registration.getAccountName());
		loginInformation.setEmailAddress(registration.getEmailAddress());
		session.setAttribute("loginInformation", loginInformation);
		session.setMaxInactiveInterval(3000);
		return new RedirectView("/account");
	}
}
