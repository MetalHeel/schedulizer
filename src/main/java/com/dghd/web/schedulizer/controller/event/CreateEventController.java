package com.dghd.web.schedulizer.controller.event;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.dghd.web.schedulizer.data.exception.DataNotFoundException;
import com.dghd.web.schedulizer.data.manager.EventManager;
import com.dghd.web.schedulizer.model.event.EventCreation;
import com.dghd.web.schedulizer.security.manager.SessionManager;
import com.dghd.web.schedulizer.security.sessionAttribute.LoginInformation;
import com.dghd.web.schedulizer.utility.DateUtilites;

@Controller
public class CreateEventController {
	@Autowired
	private EventManager eventManager;
	@Autowired
	private SessionManager sessionManager;
	
	// TODO: ID in parameter for editing.
	// TODO: We need bands.
	// TODO: We need to handle tags.
	@GetMapping("/createEvent")
	public Object createEventLanding(HttpSession session, Model model) {
		LoginInformation loginInformation = sessionManager.getLoginInformationFromSession(session);
		if (loginInformation == null) {
			return new RedirectView("/");
		}
		model.addAttribute("eventCreation", new EventCreation());
		model.addAttribute("isCreateError", Boolean.FALSE);
		return "event/createEvent";
	}
	
	@PostMapping("/createEvent")
	public Object createEventSubmit(HttpSession session, Model model, @ModelAttribute EventCreation eventCreation) {
		LoginInformation loginInformation = sessionManager.getLoginInformationFromSession(session);
		if (loginInformation == null) {
			return new RedirectView("/");
		}
		// Validate here so we can put appropriate error messages.
		if (StringUtils.isEmpty(eventCreation.getEventName())) {
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", "Events require an event name.");
			return "event/createEvent";
		}
		if (StringUtils.isEmpty(eventCreation.getLocation())) {
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", "Events require a location.");
			return "event/createEvent";
		}
		if (StringUtils.isEmpty(eventCreation.getStartTime())) {
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", "Events require a start time.");
			return "event/createEvent";
		}
		Date startTime = null;
		Date endTime = null;
		try {
			String startTimeString = DateUtilites.formatDateTimeLocalString(eventCreation.getStartTime());
			startTime = DateUtilites.getDateFromString(startTimeString);
			if (!StringUtils.isEmpty(eventCreation.getEndTime())) {
				String endTimeString = DateUtilites.formatDateTimeLocalString(eventCreation.getEndTime());
				endTime = DateUtilites.getDateFromString(endTimeString);
			}
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Error parsing event times: %s", t.getMessage()));
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", "There was a problem processing event times. Please try again.");
			return "event/createEvent";
		}
		if (startTime.before(new Date())) {
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", "Cannot create an event in the past.");
			return "event/createEvent";
		}
		if (endTime != null && endTime.before(startTime)) {
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", "Event end time cannot be before start time.");
			return "event/createEvent";
		}
		try {
			Timestamp startTimestamp = new Timestamp(startTime.getTime());
			Timestamp endTimestamp = null;
			if (endTime != null) {
				endTimestamp = new Timestamp(endTime.getTime());
			}
			eventManager.createEvent(eventCreation.getEventName(), eventCreation.getDescription(), eventCreation.getBannerImage(),
					eventCreation.getPosterImage(), eventCreation.getLocation(), startTimestamp, endTimestamp, loginInformation);
		} catch (DataNotFoundException e) {
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", e.getMessage());
			return "event/createEvent";
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Error creating event: %s", t.getMessage()));
			model.addAttribute("isCreateError", Boolean.TRUE);
			model.addAttribute("errorMessage", "There was a problem creating the event. Please try again.");
			return "event/createEvent";
		}
		return new RedirectView("/");
	}
}
