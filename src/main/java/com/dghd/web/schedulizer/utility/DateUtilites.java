package com.dghd.web.schedulizer.utility;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: We need to make sure these are in GMT.
public class DateUtilites {
	public static Timestamp getSqlTimestamp() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = dateFormat.format(new Date().getTime());
			Date date = dateFormat.parse(timeString);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			// Since all values are coming from internal Java functionality, this shouldn't ever happen.
			return null;
		}
	}
	
	public static Date getDateFromString(String dateString) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.parse(dateString);
	}
	
	/**
	 * This is to be used to correctly format the date time values that come from the HTML datetime-local.
	 * 
	 * @param dateTimeLocalString
	 * @return
	 */
	public static String formatDateTimeLocalString(String dateTimeLocalString) {
		return dateTimeLocalString.replace("T", " ").concat(":00");
	}
}
