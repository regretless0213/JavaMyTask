package task.week;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import task.Tools;

public class ExcelProcess {
	private static int initial;// Excel表格中初始列
	private static int interval;// 选定日期间隔
	private static int standard;// 未达标的标准

	private Map<String, Integer> stuName;// 学生列表，用来统计作业提交情况
	private ArrayList<String> dcResult;// 用来收录单词未达标学生
	private ArrayList<String> zhResult;// 用来收录综合未达标学生

	private ArrayList<Integer> Result;// 记录每天有多少学生签到及提交作业
	private ArrayList<int[]> stuChange;// 记录人员变动
	private int leave;

	private int cmon;// 当前是否统计小组成员：决定不合格学生名单是否将小组成员计算在内
	private ArrayList<String> groupStu;// 用来收录小组成员
	private ArrayList<String[]> rlStu;// 用来收录请假学生
	private ArrayList<String[]> stuDaily;// 用来统计每位学生打卡总次数

	private Tools t;


	public ExcelProcess(int initialNum, int intervalNum, int standardNum) {//初始化构造函数
		initial = initialNum;
		interval = intervalNum;
		standard = standardNum;
		leave = 0;
		cmon = 1;

		// 初始化
		stuName = new HashMap<String, Integer>();
		dcResult = new ArrayList<String>();
		zhResult = new ArrayList<String>();
		Result = new ArrayList<Integer>();
		stuChange = new ArrayList<int[]>();
		groupStu = new ArrayList<String>();
		rlStu = new ArrayList<String[]>();
		stuDaily = new ArrayList<String[]>();

		t = new Tools();
	}

	// private boolean Leave(Workbook wb, String n)
	// {//替换成tools.LeaveOrNot###########待完成
	// Sheet eSheet = wb.getSheetAt(0);
	// for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
	// Row eRow = eSheet.getRow(rowNum);
	// String name = rowNum + eRow.getCell(1).toString();
	// if (n.equals(name)) {
	// if (eRow.getCell(2) != null && eRow.getCell(2).toString().contains("转班")) {
	// return true;
	// }
	// }
	// }
	// // System.out.println(name+"名字输入有误！");
	// return false;
	//
	// }

	public void TotalCount(Workbook workbook) {
		Sheet eSheet = workbook.getSheetAt(0);// 单词签到页
		// int num = 0;

		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			if (eRow.getCell(1) == null) {
				break;
			}
			String name = rowNum + eRow.getCell(1).toString();// 限定学生姓名格式
			if (!t.LeaveOrNot(name)) {
				stuName.put(name, 0);
			} else {
				leave++;
			}
			if (eRow != null) {
				int num = 0;// 计量未交作业次数
				for (int cellsNum = initial; cellsNum < (initial + interval); cellsNum++) {
					// System.out.println(xssfRow.getCell(cellsNum));
					int rindex = cellsNum - initial;// 索引位置
					int minsize = rindex + 1;// 记录星期几
					if (Result.size() < minsize) {
						Result.add(0);
					}
					// 判断是否有提交作业的记录
					if (eRow.getCell(cellsNum) == null) {
						num++;
					} else if (eRow.getCell(cellsNum).toString() == "") {
						num++;
					} else {
						num = 0;
						if (!stuName.containsKey(name)) {
							System.out.println(name + "不在班级中。");
							break;
						} else if (eRow.getCell(cellsNum).toString().contains("加入班级")) {
							int[] change = new int[2];
							change[0] = rowNum;
							change[1] = minsize;
							stuChange.add(change);
						} else if (eRow.getCell(cellsNum).toString().contains("请假")) {
							String[] rl = new String[2];
							rl[0] = name;
							rl[1] = "周" + minsize;
							rlStu.add(rl);
						} else {
							Result.set(rindex, Result.get(rindex) + 1);
						}
					}
				}
				if (num >= standard) {
					dcResult.add(name);
				}
			}
		}

		Sheet dSheet = workbook.getSheetAt(1);//打卡页
		for (int rowNum = 1; rowNum <= dSheet.getLastRowNum(); rowNum++) {
			Row dRow = dSheet.getRow(rowNum);
			if (dRow.getCell(1) == null) {
				break;
			}
			String name = rowNum + dRow.getCell(1).toString();// 限定学生姓名格式
			if (!t.LeaveOrNot(name)) {
				String[] daily = new String[2];
				daily[0] = name;
				int count = 0;
				for (int cellsNum = initial; cellsNum < (initial + interval); cellsNum++) {
					if (dRow.getCell(cellsNum) != null && dRow.getCell(cellsNum).toString() != "") {
						count++;
					}
				}
				daily[1] = count + "";
				stuDaily.add(daily);
			}
		}

		// 综合作业统计
		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			for (int column = initial; column < (initial + interval); column++) {
				int rindex = column - initial;
				if (rowNum == 1) {
					Result.add(0);
				}
				int daycount = 0;//确保一天四门科目未提交次数只统计一次

				for (int numSheet = 2; numSheet < workbook.getNumberOfSheets(); numSheet++) {
					Sheet zSheet = workbook.getSheetAt(numSheet);
					if (zSheet == null) {
						continue;
					}
					Row zRow = zSheet.getRow(rowNum);
					// stuName.add(xssfRow.getCell(1).toString());
					if (zRow.getCell(1) == null) {
						break;
					}
					String name = rowNum + zRow.getCell(1).toString();
					if (!stuName.containsKey(name)) {
						break;
					}
					int tmp = stuName.get(name);
					if (zRow != null) {
						if (zRow.getCell(column) == null || zRow.getCell(column).toString() == "") {
							if ((tmp + 1) < column && daycount < 1) {
								stuName.put(name, tmp + 1);
								daycount++;
							} else {
								continue;
							}
						} else {
							stuName.put(name, 0);
							// if (!zRow.getCell(column).toString().contentEquals("加入班级")) {
							Result.set(rindex + interval, Result.get(rindex + interval) + 1);
							// }
							if (zRow.getCell(column).toString().contains("请假")) {
								int minsize = rindex + 1;// 记录星期几
								String[] rl = new String[2];
								rl[0] = name;
								int day = minsize + 5;
								if (day > 7) {
									day = day - 7;
								}
								rl[1] = "周" + day;
								rlStu.add(rl);
							}
							break;
						}
						if (numSheet == 2 && cmon == 1) {// 提取小组成员 && zRow.getCell(9).toString() != ""
							if (zRow.getCell(9) != null && zRow.getCell(9).toString().contains("组员")) {
								if (!groupStu.contains(name)) {
									groupStu.add(name);
								}
							}
						}
					}
				}
			}
		}
		for (String stu : stuName.keySet()) {
			if (stuName.get(stu) >= standard) {// 存储综合未达标的学生列表
				zhResult.add(stu);
			}
		}

	}

	public void printBadStuList() {	//未达标学生名单
		// 排序
		dcResult.sort(t.sc);
		zhResult.sort(t.sc);

		System.out.println("单词签到未达标（连续" + standard + "天以上未签到）名单：");
		for (String r : dcResult) {
			System.out.println(r);
		}
		System.out.println("共有" + dcResult.size() + "同学未签到\n");
		System.out.println("综合作业提交未达标（连续" + standard + "天以上未提交）名单：");
		for (String r : zhResult) {
			if (!groupStu.contains(r)) {
				System.out.println(r);
			}

		}
		System.out.println("共有" + (zhResult.size() - groupStu.size()) + "同学未提交作业\n");
	}

	public void printRatio() {	//打印打卡率
		System.out.println("本周英语打卡率如下：");
		for (int d = 0; d < (Result.size() / 2); d++) {
			System.out.println("本周第" + (d + 1) + "天，共 人，打卡人数" + Result.get(d));
		}
		System.out.println("本周综合打卡率如下：");
		for (int d = (Result.size() / 2); d < Result.size(); d++) {
			System.out.println("本周第" + (d - (Result.size() / 2) + 1) + "天，共 人，打卡人数" + Result.get(d));
		}
		System.out.println("本周人员变动情况：");
		for (int[] r : stuChange) {
			if (r[1] > 1) {
				System.out.println("本周第" + (r[1] - 1) + "天及之前学生总数为" + (r[0] - leave - 1));
			} else {
				System.out.println("本周第" + r[1] + "天学生总数为" + (r[0] - leave));
			}
		}
		System.out.println("目前学生总数量为" + stuName.size());
	}

	public void printDaily() {	//打印打卡次数
		System.out.println("本周打卡次数统计：");
		for (String[] ds : stuDaily) {
			System.out.println(ds[0] + "\t:" + ds[1]);
		}
	}

	public void printReqForLeave() {	//打印请假名单
		String name = "";
		for (String[] rl : rlStu) {
			if (!rl[0].equals(name)) {
				name = rl[0];
				System.out.println();
				System.out.print(rl[0] + " 请假：" + rl[1]);
			} else {
				System.out.print(" " + rl[1]);
			}
		}
		System.out.println();
	}

	public void printGroupMember() {	//打印请假名单
		System.out.println(groupStu.size() + "名小组成员：");
		for (String name : groupStu) {
			System.out.println(name);
		}
	}

	public void setCMON(int b) {//设定是否统计小组成员；默认统计
		cmon = b;
	}
}
