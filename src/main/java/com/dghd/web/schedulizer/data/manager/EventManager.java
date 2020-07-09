package com.dghd.web.schedulizer.data.manager;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dghd.web.schedulizer.data.exception.DataNotFoundException;
import com.dghd.web.schedulizer.security.sessionAttribute.LoginInformation;

@Component("eventManager")
public class EventManager {
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private DataManager dataManager;
	
	public void createEvent(String eventName, String description, String bannerImage, String posterImage, String location,
			Timestamp startTime, Timestamp endTime, LoginInformation loginInformation) throws Throwable {
		String accountId = accountManager.getAccountIdForNameAndEmail(loginInformation.getAccountName(), loginInformation.getEmailAddress());
		if (StringUtils.isEmpty(accountId)) {
			throw new DataNotFoundException("Could not find an account for the given login information.");
		}
		String newEventId = dataManager.getNewId();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO dghd_e_event (event_id,name,description,banner_image,poster_image,location,start_time,end_time)");
		if (endTime != null) {
			stringBuilder.append(String.format(" VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')", newEventId, eventName, description,
					bannerImage, posterImage, location, startTime, endTime));
		} else {
			stringBuilder.append(String.format(" VALUES ('%s','%s','%s','%s','%s','%s','%s',NULL)", newEventId, eventName, description,
					bannerImage, posterImage, location, startTime));
		}
		try {
			Statement statement = dataManager.getConnection().createStatement();
			statement.executeUpdate(stringBuilder.toString());
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Unable to create new event: %s", t.getMessage()));
			throw t;
		}
		stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO dghd_e_event_rel_account (event_rel_account_id,event_id,account_id)");
		stringBuilder.append(String.format(" VALUES ('%s','%s','%s')", dataManager.getNewId(), newEventId, accountId));
		try {
			Statement statement = dataManager.getConnection().createStatement();
			statement.executeUpdate(stringBuilder.toString());
			dataManager.closeConnection();
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Unable to create new event: %s", t.getMessage()));
			throw t;
		}
	}
}
