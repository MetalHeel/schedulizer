package com.dghd.web.schedulizer.data.manager;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("dataManager")
public class DataManager {
	private final String DATABASE_URL;
	private final String DATABASE_USERNAME;
	private final String DATABASE_PASSWORD;
	private Connection connection;
	
	public DataManager() throws URISyntaxException {
		URI databaseUri = new URI(System.getenv("DATABASE_URL"));
		String[] userInfo = databaseUri.getUserInfo().split(":");
		DATABASE_URL = String.format("jdbc:postgresql://%s:%s%s", databaseUri.getHost(), databaseUri.getPort(), databaseUri.getPath());
		DATABASE_USERNAME = userInfo[0];
		DATABASE_PASSWORD = userInfo[1];
	}
	
	/**
	 * Will return the currently-opened connection, or create a new one if there isn't already one, to the database.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (connection == null) {
			connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
		}
		return connection;
	}
	
	/**
	 * It is HIGHLY RECOMMENDED that this is used to close the connection rather than directly closing this due to there being a
	 * generalized connection for this singleton.
	 * 
	 * TODO: Do we need to close on SQLException? Or does it already happen?
	 */
	public void closeConnection() throws SQLException {
		connection.close();
		connection = null;
	}
	
	public String getNewId() {
		return  UUID.randomUUID().toString().replace("-", "");
	}
}
