package com.dghd.web.schedulizer.data.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dghd.web.schedulizer.data.account.AccountType;
import com.dghd.web.schedulizer.model.account.Registration;
import com.dghd.web.schedulizer.utility.DateUtilites;

@Component("accountManager")
public class AccountManager {
	@Autowired
	private DataManager dataManager;
	
	public void createAccount(String name, String emailAddress, String password, AccountType type) throws SQLException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("INSERT INTO dghd_a_account (account_id,name,email_address,password_hash,profile_image,banner_image,type,description,create_time)");
		stringBuilder.append(String.format(" VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')", dataManager.getNewId(), name, emailAddress,
				BCrypt.hashpw(password, BCrypt.gensalt()), "NULL", "NULL", type.name(), "NULL", DateUtilites.getSqlTimestamp()));
		try {
			Statement statement = dataManager.getConnection().createStatement();
			statement.executeUpdate(stringBuilder.toString());
			dataManager.closeConnection();
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Unable to create new account: %s", t.getMessage()));
			throw t;
		}
	}
	
	public String getAccountNameForCredentials(String emailAddress, String password) throws SQLException {
		try {
			Statement statement = dataManager.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT name, password_hash FROM dghd_a_account WHERE email_address = '%s'", emailAddress));
			while (resultSet.next()) {
				if (BCrypt.checkpw(password, resultSet.getString(2))) {
					dataManager.closeConnection();
					return resultSet.getString(1);
				}
			}
			dataManager.closeConnection();
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Unable to look up account: %s", t.getMessage()));
			throw t;
		}
		return null;
	}
	
	public String getAccountIdForNameAndEmail(String accountName, String emailAddress) throws SQLException {
		if (StringUtils.isEmpty(accountName)) {
			return null;
		}
		if (StringUtils.isEmpty(emailAddress)) {
			return null;
		}
		try {
			Statement statement =  dataManager.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT account_id FROM dghd_a_account WHERE name = '%s' AND email_address = '%s'",
					accountName, emailAddress));
			if (resultSet.next()) {
				dataManager.closeConnection();
				return resultSet.getString(1);
			}
			dataManager.closeConnection();
			return null;
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Unable to look up account: %s", t.getMessage()));
			throw t;
		}
	}
	
	public boolean validateRegistration(Registration registration) throws SQLException {
		try {
			Statement statement = dataManager.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM dghd_a_account WHERE name = '%s' AND email_address = '%s'",
					registration.getAccountName(), registration.getEmailAddress()));
			if (resultSet.next()) {
				dataManager.closeConnection();
				return false;
			}
			dataManager.closeConnection();
		} catch (Throwable t) {
			// TODO: Better logging.
			System.out.println(String.format("Error while trying to look up account [%s]: %s", registration.getAccountName(), t.getMessage()));
			throw t;
		}
		return true;
	}
}
