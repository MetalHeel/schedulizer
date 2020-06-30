package com.dghd.web.schedulizer.security.manager;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.dghd.web.schedulizer.security.sessionAttribute.LoginInformation;

@Component("sessionManager")
public class SessionManager {
	public LoginInformation getLoginInformationFromSession(HttpSession session) {
		try {
			return (LoginInformation)session.getAttribute("loginInformation");
		} catch (Throwable t) {
			// Invalid session. Just return null, there is no login info.
			return null;
		}
	}
}
