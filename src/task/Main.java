package task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	/*
	 * C:\Users\Regretless\OneDrive\共享文档\考虫\每日统计\春季-计算机班-张金生-0409-0415
	 * C:\Users\Regretless\OneDrive\共享文档\考虫\每日统计\考虫VIP计算机班打卡和作业提交情况情况0409-0415.xlsx
	 */

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		int initial = 2;// Excel表格中初始列
		int interval = 5;// 选定日期间隔
		int standard = 5;// 未达标的标准
		boolean[] index = { true, true };// false ,false 0.是否统计周总结和周计划；1.是否统计学生作业提交情况
		boolean CreateOrNot = false;// true

		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);// 传入的路径
			if(!filePath.exists()) {
				System.out.println("周文件夹尚未创建！");
				CreateOrNot = true;
				continue;
			}
			if (filePath.isDirectory()) {// 如果是文件夹，统计周总结或周计划
				DirFilter df = new DirFilter();
				if (index[0]) {
					df.TotalCount(filePath);
				}
			} else {// 如果是文件，统计作业提交情况
				if (initial + interval > 9) {
					System.out.println("initial或interval初始值错误！");
					return;
				}
				ExcelProcess ep = new ExcelProcess(initial, interval, standard);
				Workbook myWorkbook = null;
				InputStream is = new FileInputStream(args[i]);
				String excelType = args[i].split("\\.")[1];// 区分文件类型
				if (excelType.equals("xlsx")) {
					myWorkbook = new XSSFWorkbook(is);
				} else if (excelType.equals("xls")) {
					myWorkbook = new HSSFWorkbook(is);
				} else {
					System.out.println("输入文件格式错误！");
				}
				if (index[1]) {
					ep.TotalCount(myWorkbook);
					ep.print();
				}
				if (CreateOrNot) {
					String fp = args[i];
					String[] tmpath = fp.split("\\\\");
					int tmpsize = tmpath.length - 1;// 未完成
					String path = "";
					for (int n = 0; n < tmpsize; n++) {
						path = path + tmpath[n] + "\\";
					}
					System.out.println(path);
					CreateDir cd = new CreateDir(path, myWorkbook);
					// String todayDir = nm.createDir();
					// nm.createDirByDateAndName(todayDir, namelist);
					// nm.createWeekTaskDir(mainDir+sparator+"周任务", namelist);
					// nm.tryToCopyOldWeek();
					cd.mkStuDir();
					// nm.mkdirs(NormalManagement.mainDir + sparator + "周任务", namelist);
				}
			}
		}

	}

}
