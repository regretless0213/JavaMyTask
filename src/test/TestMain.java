package test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import task.Tools;

public class TestMain {
	private Date getBeginDayOfWeek() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		System.out.println(dayofweek);
		if (dayofweek == 7) {
			dayofweek -= 7;
		}
		System.out.println(dayofweek);
		cal.add(Calendar.DATE, 2 - dayofweek - 2);
		return cal.getTime();
	}
	
	private Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return (weekEndSta);
	}
	
	public String getDateofPath() {
		Date date1 = getBeginDayOfWeek();
		Date date2 = getEndDayOfWeek();
		DateFormat format = new SimpleDateFormat("MMdd");

		String temp = "";
		temp += format.format(date1) + "-" + format.format(date2);
		return temp;

	}
	
	public static void main(String[] args) throws Exception {
//		String s = (new TestMain()).getDateofPath();
//		System.out.println(s);
		Tools t = new Tools();
		ArrayList<String> sl = t.getStuLeave();
		for(String tmp : sl) {
			System.out.println(tmp);
		}
		if(t.LeaveOrNot("24´ÔÑÞÆ»")) {
			System.out.println("×ª°à");
		}else {
			System.out.println("ÓÐÎó");
		}
	}
}
