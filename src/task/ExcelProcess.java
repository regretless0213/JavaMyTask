package task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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

	static Comparator<String> sc = new Comparator<String>() {
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

	public ExcelProcess(int initialNum, int intervalNum, int standardNum) throws Exception {
		initial = initialNum;
		interval = intervalNum;
		standard = standardNum;
		leave = 0;

		// 初始化
		stuName = new HashMap<String, Integer>();
		dcResult = new ArrayList<String>();
		zhResult = new ArrayList<String>();
		Result = new ArrayList<Integer>();
		stuChange = new ArrayList<int[]>();

	}

	public void TotalCount(Workbook workbook) {

		Sheet eSheet = workbook.getSheetAt(0);// 单词签到页
		// int num = 0;

		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			String name = rowNum + eRow.getCell(1).toString();// 限定学生姓名格式
			stuName.put(name, 0);
			if (eRow != null) {
				int num = 0;// 计量未交作业次数
				for (int cellsNum = initial; cellsNum < (initial + interval); cellsNum++) {
					// System.out.println(xssfRow.getCell(cellsNum));
					int rindex = cellsNum - initial;// 索引位置
					int minsize = rindex + 1;//
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
						if (eRow.getCell(cellsNum).toString().contentEquals("转班")) {
							stuName.remove(name);
							leave++;
							break;
						} else if (eRow.getCell(cellsNum).toString().contentEquals("加入班级")) {
							int[] change = new int[2];
							change[0] = rowNum;
							change[1] = minsize;
							stuChange.add(change);
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

		for (int column = initial; column < (initial + interval); column++) {
			int rindex = column - initial;
			Result.add(0);

			for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
				int daycount = 0;

				for (int numSheet = 1; numSheet < workbook.getNumberOfSheets(); numSheet++) {
					Sheet zSheet = workbook.getSheetAt(numSheet);
					if (zSheet == null) {
						continue;
					}
					Row zRow = zSheet.getRow(rowNum);
					// stuName.add(xssfRow.getCell(1).toString());

					String name = rowNum + zRow.getCell(1).toString();
					if (!stuName.containsKey(name)) {
						break;
					}
					int tmp = stuName.get(name);
					if (zRow != null) {
						if (zRow.getCell(column) == null) {
							if ((tmp + 1) < column && daycount < 1) {
								stuName.put(name, tmp + 1);
								daycount++;
							} else {
								continue;
							}
						} else if (zRow.getCell(column).toString() == "") {
							if ((tmp + 1) < column && daycount < 1) {
								stuName.put(name, tmp + 1);
								daycount++;
							} else {
								continue;
							}
						} else {
							stuName.put(name, 0);
							if (!zRow.getCell(column).toString().contentEquals("加入班级")) {
								Result.set(rindex + interval, Result.get(rindex + interval) + 1);
							}
							break;
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

	public void print() {
		// 排序
		dcResult.sort(sc);
		zhResult.sort(sc);

		System.out.println("单词签到未达标（连续" + standard + "天以上未签到）名单：");
		for (String r : dcResult) {
			System.out.println(r);
		}
		System.out.println("共有" + dcResult.size() + "同学未签到\n");
		System.out.println("综合作业提交未达标（连续" + standard + "天以上未提交）名单：");
		for (String r : zhResult) {
			System.out.println(r);
		}
		System.out.println("共有" + zhResult.size() + "同学未提交作业");
		System.out.println("本周英语打卡率如下：");
		for (int d = 0; d < (Result.size() / 2); d++) {
			System.out.println("第" + (d + 1) + "天" + Result.get(d));
		}
		System.out.println("本周综合打卡率如下：");
		for (int d = (Result.size() / 2); d < Result.size(); d++) {
			System.out.println("第" + (d - (Result.size() / 2) + 1) + "天" + Result.get(d));
		}
		System.out.println("本周人员变动情况：");
		for (int[] r : stuChange) {
			System.out.println("第" + r[1] + "天学生总数为" + (r[0] - leave));
		}
		System.out.println("目前学生总数量为" + stuName.size());
	}

}
