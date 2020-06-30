package com.dghd.web.schedulizer.controller.event;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.dghd.web.schedulizer.model.event.EventCreation;
import com.dghd.web.schedulizer.security.LoginInformation;

@Controller
public class CreateEventController {
	@GetMapping("/createEvent")
	public Object createEventLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = null;
		try {
			loginInformation = (LoginInformation)session.getAttribute("loginInformation");
		} catch (Throwable t) {
			// Invalid session. No need to do anything.
		}
		if (loginInformation == null) {
			return new RedirectView("/");
		}
		model.addAttribute("eventCreation", new EventCreation());
		return "event/createEvent";
	}
	
	// TODO: Manual URL manipulation protection.
	@PostMapping("/createEvent")
	public Object createEventSubmit(HttpSession session, Model model, @ModelAttribute EventCreation eventCreation) {
		// TODO: EventManager, create event, validate event, etc.
		return "home";
	}
}
