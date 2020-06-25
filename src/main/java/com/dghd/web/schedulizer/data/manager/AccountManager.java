package com.dghd.web.schedulizer.data.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.dghd.web.schedulizer.data.account.AccountType;

@Component("accountManager")
public class AccountManager {
	@Autowired
	private DataManager dataManager;
	
	public void createAccount(String name, String emailAddress, String password, AccountType type) {
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
			// TODO: Logging and error handling.
			System.out.println(String.format("Unable to create new account: %s", t.getMessage()));
		}
	}
	
	public boolean isValidLogin(String emailAddress, String password) {
		try {
			Connection connection = dataManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT password_hash FROM dghd_a_account WHERE email_address = '%s'", emailAddress));
			while (resultSet.next()) {
				if (BCrypt.checkpw(password, resultSet.getString(1))) {
					connection.close();
					return true;
				}
			}
			connection.close();
		} catch (Throwable t) {
			// TODO: Logging and error handling.
			System.out.println(String.format("Unable to look up account: %s", t.getMessage()));
		}
		return false;
	}
	
	public String getAccountNameForCredentials(String emailAddress, String password) {
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
			// TODO: Logging and error handling.
			System.out.println(String.format("Unable to look up account: %s", t.getMessage()));
		}
		return null;
	}
}
