package task;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;

public class Main {
	/*
	 * C:\Users\Regretless\OneDrive\共享文档\考虫\每日统计\春季-计算机班-张金生-0409-0415
	 * C:\Users\Regretless\OneDrive\共享文档\考虫\每日统计\考虫VIP计算机班打卡和作业提交情况情况0409-0415.xlsx
	 */

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		int initial = 2;// Excel表格中初始列
		int interval = 7;// 选定日期间隔
		int standard = 7;// 未达标的标准
		boolean[] index = { true, true };// false ,false 0.是否统计周总结和周计划；1.是否统计学生作业提交情况
		boolean CreateOrNot = false;// true

		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);// 传入的路径
			if (!filePath.exists()) {
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
				Tools wb = new Tools();
				Workbook myWorkbook = wb.getWorkbook(args[i]);
				if (index[1]) {
					ep.TotalCount(myWorkbook);
					ep.printBadStuList();
					System.out.println();
					
					ep.printRatio();
					System.out.println();
					
					ep.printDaily();
					System.out.println();
					
					ep.printReqForLeave();
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
					Tools cd = new Tools();
					// String todayDir = nm.createDir();
					// nm.createDirByDateAndName(todayDir, namelist);
					// nm.createWeekTaskDir(mainDir+sparator+"周任务", namelist);
					// nm.tryToCopyOldWeek();
					cd.mkStuDir(path, myWorkbook);
					// nm.mkdirs(NormalManagement.mainDir + sparator + "周任务", namelist);
				}
			}
		}

	}

}
