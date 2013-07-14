package dateandtime;

import android.text.format.Time;

public class DateAndTime {

	// for get time and date
	private Time today = new Time(Time.getCurrentTimezone());

	String date = null;
	String time = null;

	public DateAndTime() {

	}

	public String getDate() {
		// Set today date
		today.setToNow();
		// extract date from today variable
		if (today.monthDay < 10) {
			date = "0" + today.monthDay + "/";
		} else {
			date = today.monthDay + "/";
		}
		if (today.month < 9) {
			date = date + "0" + (today.month + 1) + "/" + today.year;
		} else {
			date = date + (today.month + 1) + "/" + today.year;
		}
		return date;
	}

	public String getTime() {
		// Set today date
		today.setToNow();
		// extract time from today variable
		if (today.hour == 0) {
			time = "00:";
		} else if (today.hour < 10) {
			time = "0" + today.hour + ":";
		} else {
			time = today.hour + ":";
		}
		if (today.minute < 10) {
			time = time + "0" + today.minute;

		} else {
			time = time + today.minute;
		}
		return time;

	}

}
