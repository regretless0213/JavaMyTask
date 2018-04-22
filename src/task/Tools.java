package task;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Tools {

	private String mainDir;
	private String toParent;
	private static String sparator = "\\";
	private Workbook wb = null;



	// private String createDir() {
	// Date date = new Date();
	// DateFormat format = new SimpleDateFormat("MM-dd");
	// String dateMD = format.format(date);
	//// System.out.println(dateMD);
	// String location = mainDir + sparator + dateMD;
	// File f = new File(location);
	// if (!f.exists())
	// f.mkdir();
	// return f.getAbsolutePath();
	// }

	public ArrayList<String> readNameListFromExcel(Workbook workbook) {
		ArrayList<String> nameList = new ArrayList<>();
		Sheet eSheet = workbook.getSheetAt(0);
		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			String tmpName = rowNum + eRow.getCell(1).toString();
			// System.out.println(tmpName);
			if (eRow.getCell(2) == null || !eRow.getCell(2).toString().contentEquals("转班")) {
				nameList.add(tmpName);
			} else {
				System.out.println(tmpName + "已转班");
			}
		}

		return nameList;

	}

	// private void createDirByDateAndName(String absPath, ArrayList<String> names)
	// {
	// for (String name : names) {
	// String location = absPath + sparator + name;
	// File f = new File(location);
	// if (!f.exists())
	// f.mkdir();
	// }
	// }

	// private void createWeekTaskDir(String absPath, ArrayList<String> names) {
	// File f = new File(absPath);
	// if (!f.exists())
	// f.mkdir();
	// for (String name : names) {
	// String location = absPath + sparator + name;
	// f = new File(location);
	// if (!f.exists())
	// f.mkdir();
	// }
	// }

	private void mkdir(String parent, String child) {
		String path = parent + sparator + child;
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
			System.out.println(child + "创建成功");
		}
		// else {
		// System.out.println(path);
		// }
	}

	private void mkdirs(String parent, ArrayList<String> children) {
		for (String child : children) {
			mkdir(parent, child);
		}
	}

	// private void removeAllFromTo(String fromParent, String toParent, String
	// renamePrefix) {
	// File f = new File(fromParent);
	// if (!f.isDirectory())
	// return;
	// File[] fs = f.listFiles();
	// for (File ft : fs) {
	// if (!ft.isDirectory())
	// continue;
	// File[] fss = ft.listFiles();
	// for (File ftt : fss) {
	// // System.out.println(ftt.getAbsolutePath());
	// ftt.renameTo(new File(toParent + sparator + ft.getName() + sparator +
	// renamePrefix + ftt.getName()));
	// }
	// }
	// }

	// private void unused() {
	/*
	 * 批量剪切 nm.mkdir(toParent, ""); nm.mkdirs(toParent, namelist); String
	 * fromParents = NormalManagement.mainDir; File f = new File(fromParents);
	 * File[] fs = f.listFiles(new FileFilter() {
	 * 
	 * @Override public boolean accept(File pathname) { return
	 * pathname.getName().startsWith("03"); } }); for(File ff: fs){
	 * System.out.println(ff.getAbsolutePath());
	 * nm.removeAllFromTo(ff.getAbsolutePath(), toParent, ff.getName()+"-"); }
	 */
	// }

	private Date getBeginDayOfWeek() {
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

	// private Date getLastBeginDayOfWeek() {
	// Date date = new Date();
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(date);
	// int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
	// if (dayofweek == 1) {
	// dayofweek += 7;
	// }
	// cal.add(Calendar.DATE, 2 - dayofweek - 7);
	// return cal.getTime();
	// }

	// 获取本周的结束时间
	private Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return (weekEndSta);
	}

	// private Date getLastEndDayOfWeek() {
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(getBeginDayOfWeek());
	// cal.add(Calendar.DAY_OF_WEEK, 6 - 7);
	// Date weekEndSta = cal.getTime();
	// return (weekEndSta);
	// }

	public String getDateofPath() {
		Date date1 = getBeginDayOfWeek();
		Date date2 = getEndDayOfWeek();
		DateFormat format = new SimpleDateFormat("MMdd");

		String temp = "";
		temp += format.format(date1) + "-" + format.format(date2);
		return temp;

	}

	private void mkWeekDir() {
		String dirName = "我的上交" + getDateofPath();
		// System.out.println(dirName);
		String path = mainDir + sparator + dirName;
		mkdir(path, "");
	}

	public void mkStuDir(String path, Workbook workbook) {
		mainDir = path;
		toParent = mainDir + "\\春季-计算机班-张金生-";
		wb = workbook;
		
		ArrayList<String> namelist = readNameListFromExcel(wb);
		mkWeekDir();
		toParent += getDateofPath();
		mkdir(toParent, "");
		mkdirs(toParent, namelist);

	}

}
