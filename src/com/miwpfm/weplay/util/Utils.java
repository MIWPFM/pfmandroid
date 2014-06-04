package com.miwpfm.weplay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static String formatDate(String date) {
		String dateOut = date;
		SimpleDateFormat formatterIn = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat formatterOut = new SimpleDateFormat(
				"dd/MM/yyyy");
		
		try {
			Date dateIn = formatterIn.parse(date);
			dateOut= formatterOut.format(dateIn);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dateOut;		
	}
}
