package com.dghd.web.schedulizer.data.manager;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.stereotype.Component;

// TODO: Error handling.
@Component("dataManager")
public class DataManager {
	private final String DATABASE_URL;
	private final String DATABASE_USERNAME;
	private final String DATABASE_PASSWORD;
	
	public DataManager() throws URISyntaxException {
		URI databaseUri = new URI(System.getenv("DATABASE_URL"));
		String[] userInfo = databaseUri.getUserInfo().split(":");
		DATABASE_URL = String.format("jdbc:postgresql://%s:%s%s", databaseUri.getHost(), databaseUri.getPort(), databaseUri.getPath());
		DATABASE_USERNAME = userInfo[0];
		DATABASE_PASSWORD = userInfo[1];
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
	}
	
	public String getNewId() {
		return  UUID.randomUUID().toString().replace("-", "");
	}
	
	public Timestamp getSqlDateTime() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = dateFormat.format(new Date().getTime());
			Date date = dateFormat.parse(timeString);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			// TODO: What do we do about the parse exception?
			return null;
		}
	}
}
