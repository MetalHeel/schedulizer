package com.dghd.web.schedulizer.data.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.dghd.web.schedulizer.data.account.AccountType;
import com.dghd.web.schedulizer.model.account.Registration;

@Component("accountManager")
public class AccountManager {
	@Autowired
	private DataManager dataManager;
	
	public void createAccount(String name, String emailAddress, String password, AccountType type) throws SQLException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO dghd_a_account (account_id,name,email_address,password_hash,profile_image,banner_image,type,description,create_time)");
		stringBuilder.append(String.format(" VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')", dataManager.getNewId(), name, emailAddress,
				BCrypt.hashpw(password, BCrypt.gensalt()), "NULL", "NULL", type.name(), "NULL", dataManager.getSqlDateTime()));
		try {
			Connection connection = dataManager.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(stringBuilder.toString());
			connection.close();
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Unable to create new account: %s", t.getMessage()));
			throw t;
		}
	}
	
	public String getAccountNameForCredentials(String emailAddress, String password) throws SQLException {
		try {
			Connection connection = dataManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT name, password_hash FROM dghd_a_account WHERE email_address = '%s'", emailAddress));
			while (resultSet.next()) {
				if (BCrypt.checkpw(password, resultSet.getString(2))) {
					connection.close();
					return resultSet.getString(1);
				}
			}
			connection.close();
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Unable to look up account: %s", t.getMessage()));
			throw t;
		}
		return null;
	}
	
	public boolean validateRegistration(Registration registration) throws SQLException {
		try {
			Connection connection = dataManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM dghd_a_account WHERE name = '%s' AND email_address = '%s'",
					registration.getAccountName(), registration.getEmailAddress()));
			if (resultSet.next()) {
				connection.close();
				return false;
			}
			connection.close();
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Error while trying to look up account [%s]: %s", registration.getAccountName(), t.getMessage()));
			throw t;
		}
		return true;
	}
}
