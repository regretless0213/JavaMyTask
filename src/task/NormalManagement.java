package task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NormalManagement {

	public static String mainDir = "C:\\Users\\Regretless\\OneDrive\\共享文档\\考虫\\每日统计";
	public static String sparator = "\\";
	public static String nameListExcel = mainDir + sparator + "考虫VIP计算机班问卷调查数据.csv.txt";
	public static String charset = "utf-8";

	public String createDir() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("MM-dd");
		String dateMD = format.format(date);
		System.out.println(dateMD);
		String location = mainDir + sparator + dateMD;
		File f = new File(location);
		if (!f.exists())
			f.mkdir();
		return f.getAbsolutePath();
	}

	public ArrayList<String> readNameListFromExcel() {
		ArrayList<String> nameList = new ArrayList<>();
		System.out.println(nameListExcel);
		try {
			BufferedReader fr = new BufferedReader(new FileReader(nameListExcel));
			System.out.println(new String(fr.readLine().split(",")[1].getBytes(charset)));
			// fr.readLine();
			while (fr.ready()) {
				String[] info = fr.readLine().split(",");
				String number = new String(info[0].getBytes(charset));
				String name = new String(info[1].getBytes(charset));
				String nname = number + name;
				nameList.add(nname);
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return nameList;

	}

	public void createDirByDateAndName(String absPath, ArrayList<String> names) {
		for (String name : names) {
			String location = absPath + sparator + name;
			File f = new File(location);
			if (!f.exists())
				f.mkdir();
		}
	}

	public void createWeekTaskDir(String absPath, ArrayList<String> names) {
		File f = new File(absPath);
		if (!f.exists())
			f.mkdir();
		for (String name : names) {
			String location = absPath + sparator + name;
			f = new File(location);
			if (!f.exists())
				f.mkdir();
		}
	}

	public void mkdir(String parent, String child) {
		File f = new File(parent + sparator + child);
		if (!f.exists())
			f.mkdir();
	}

	public void mkdirs(String parent, ArrayList<String> children) {
		for (String child : children) {
			mkdir(parent, child);
		}
	}

	public void removeAllFromTo(String fromParent, String toParent, String renamePrefix) {
		File f = new File(fromParent);
		if (!f.isDirectory())
			return;
		File[] fs = f.listFiles();
		for (File ft : fs) {
			if (!ft.isDirectory())
				continue;
			File[] fss = ft.listFiles();
			for (File ftt : fss) {
				// System.out.println(ftt.getAbsolutePath());
				ftt.renameTo(new File(toParent + sparator + ft.getName() + sparator + renamePrefix + ftt.getName()));
			}
		}
	}

	public void unused() {
		/*
		 * 批量剪切 nm.mkdir(toParent, ""); nm.mkdirs(toParent, namelist); String
		 * fromParents = NormalManagement.mainDir; File f = new
		 * File(fromParents); File[] fs = f.listFiles(new FileFilter() {
		 * 
		 * @Override public boolean accept(File pathname) { return
		 * pathname.getName().startsWith("03"); } }); for(File ff: fs){
		 * System.out.println(ff.getAbsolutePath());
		 * nm.removeAllFromTo(ff.getAbsolutePath(), toParent, ff.getName()+"-");
		 * }
		 */
	}

	public Date getBeginDayOfWeek() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return cal.getTime();
	}
	
	public Date getLastBeginDayOfWeek() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek-7);
		return cal.getTime();
	}

	// 获取本周的结束时间
	public Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return (weekEndSta);
	}

	public Date getLastEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6-7);
		Date weekEndSta = cal.getTime();
		return (weekEndSta);
	}

	static String toParent = NormalManagement.mainDir + "\\春季-计算机班-张金生-";
	Date date1;
	Date date2;
	DateFormat format;
	public void mkWeekDir() {
		date1 = getBeginDayOfWeek();
		date2 = getEndDayOfWeek();
		format = new SimpleDateFormat("MMdd");
		String dirName = "我的上交"+format.format(date1)+"-"+format.format(date2);
		//System.out.println(dirName);
		toParent+=format.format(date1)+"-"+format.format(date2);
		mkdir(mainDir+sparator+dirName, "");
	}
	

	public static void main(String[] args) {
		NormalManagement nm = new NormalManagement();
		// String todayDir = nm.createDir();
		ArrayList<String> namelist = nm.readNameListFromExcel();
		// nm.createDirByDateAndName(todayDir, namelist);
		// nm.createWeekTaskDir(mainDir+sparator+"周任务", namelist);
		nm.mkWeekDir();
		//nm.tryToCopyOldWeek();
		nm.mkdir(toParent, "");
		nm.mkdirs(toParent, namelist);
		//nm.mkdirs(NormalManagement.mainDir + sparator + "周任务", namelist);
		
	}

}
