package com.dghd.web.schedulizer.controller.event;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.dghd.web.schedulizer.model.event.EventCreation;
import com.dghd.web.schedulizer.security.manager.SessionManager;
import com.dghd.web.schedulizer.security.sessionAttribute.LoginInformation;

@Controller
public class CreateEventController {
	@Autowired
	private SessionManager sessionManager;
	
	@GetMapping("/createEvent")
	public Object createEventLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = sessionManager.getLoginInformationFromSession(session);
		if (loginInformation == null) {
			return new RedirectView("/");
		}
		model.addAttribute("eventCreation", new EventCreation());
		return "event/createEvent";
	}
	
	@PostMapping("/createEvent")
	public Object createEventSubmit(HttpSession session, Model model, @ModelAttribute EventCreation eventCreation) {
		LoginInformation loginInformation = sessionManager.getLoginInformationFromSession(session);
		if (loginInformation != null) {
			return new RedirectView("/");
		}
		// TODO: EventManager, create event, validate event, etc.
		return "home";
	}
}
