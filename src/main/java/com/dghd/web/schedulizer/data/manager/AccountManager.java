package com.dghd.web.schedulizer.data.manager;

import java.sql.Connection;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
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
				password, "NULL", "NULL", type.name(), "NULL", dataManager.getSqlDateTime()));
		try {
			Connection connection = dataManager.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(stringBuilder.toString());
			connection.close();
		} catch (Throwable t) {
			// TODO: Logging.
			System.out.println(String.format("Unable to create new account: %s", t.getMessage()));
		}
	}
}
