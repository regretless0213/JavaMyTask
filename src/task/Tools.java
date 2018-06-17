package task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

	public ArrayList<String> readNameListFromExcel(Workbook workbook) {// 获取成员名单
		ArrayList<String> nameList = new ArrayList<>();
		Sheet eSheet = workbook.getSheetAt(0);
		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			String tmpName = rowNum + eRow.getCell(1).toString();
			// System.out.println(tmpName);
			if (!LeaveOrNot(tmpName)) {
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

	private void mkdir(String parent, String child) {// 创建文件夹
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

	private void mkdirs(String parent, ArrayList<String> children) {//批量创建文件夹
		for (String child : children) {
			mkdir(parent, child);
		}
	}

	public Workbook getWorkbook(String path) {// 判断Excel表格类型
		Workbook wbtmp = null;
		InputStream is;
		try {
			is = new FileInputStream(path);

			String excelType = path.split("\\.")[1];// 区分文件类型
			if (excelType.equals("xlsx")) {
				wbtmp = new XSSFWorkbook(is);
			} else if (excelType.equals("xls")) {
				wbtmp = new HSSFWorkbook(is);
			} else {
				System.out.println("输入文件格式错误！");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wbtmp;

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

	private Date getBeginDayOfWeek() {// 获取本周周期初始日的日期
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 7) {
			dayofweek -= 7;
		}
		cal.add(Calendar.DATE, -dayofweek);
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
	private Date getEndDayOfWeek() {// 获取本周周期结束日的日期
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

	public String getDateofPath() {// 将本周期日期跨度拼接
		Date date1 = getBeginDayOfWeek();
		Date date2 = getEndDayOfWeek();
		DateFormat format = new SimpleDateFormat("MMdd");

		String temp = "";
		temp += format.format(date1) + "-" + format.format(date2);
		return temp;

	}

	private void mkWeekDir() {// 创建“我的上交”+“周期”文件夹
		String dirName = "我的上交" + getDateofPath();
		// System.out.println(dirName);
		String path = mainDir + sparator + dirName;
		mkdir(path, "");
	}

	public void mkStuDir(String path, Workbook workbook) {// 创建周任务文件夹及每位学生子文件夹
		mainDir = path;
		toParent = mainDir + "\\春季-计算机班-张金生-";
		wb = workbook;

		ArrayList<String> namelist = readNameListFromExcel(wb);
		mkWeekDir();
		toParent += getDateofPath();
		mkdir(toParent, "");
		mkdirs(toParent, namelist);

	}

	public ArrayList<String> getStuLeave() {
		return getStuLeave("C:\\Users\\Regretless\\OneDrive\\共享文档\\考虫\\每日统计\\转班名单.xlsx");
	}

	public ArrayList<String> getStuLeave(String path) {//获取转班学员名单
		ArrayList<String> stuLeave = new ArrayList<String>();
		Workbook mywb = getWorkbook(path);
		Sheet mysh = mywb.getSheetAt(0);
		for (int rowNum = 1; rowNum <= mysh.getLastRowNum(); rowNum++) {
			Row myrow = mysh.getRow(rowNum);
			String name = myrow.getCell(0).toString().split("\\.")[0] + myrow.getCell(1).toString();
			stuLeave.add(name);
		}
		return stuLeave;
	}

	public boolean LeaveOrNot(String name) {// 判断学生是否转班
		ArrayList<String> stuList = getStuLeave();
		if (stuList.contains(name)) {
			return true;
		} else {
			return false;
		}
	}

	public Comparator<String> sc = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			// TODO Auto-generated method stub
			Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
			Matcher m1 = p.matcher(o1);
			Matcher m2 = p.matcher(o2);
			int n1 = 0;
			int n2 = 0;
			if (m1.find() && m2.find()) {
				n1 = Integer.parseInt(m1.group(0));
				n2 = Integer.parseInt(m2.group(0));
			}
			if (n1 > n2)
				return 1;
			else
				return -1;
		}
	};
	
}
