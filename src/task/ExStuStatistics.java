package task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExStuStatistics {
	private Map<String, int[]> stuCredit;// 统计总分
	private Map<String, int[]> Details;
	private static int time = 100;

	public ExStuStatistics() {// 初始化构造函数
		stuCredit = new HashMap<String, int[]>();
		Details = new HashMap<String, int[]>();
	}

	public void TotalCount(File fs) throws Exception {
		File[] tmplist = fs.listFiles();
		for (int n = 0; n < tmplist.length; n++) {// 遍历文件夹中每个学生文档
			if (!tmplist[n].isDirectory()) {
				Tools t = new Tools();
				Workbook mywb = t.getWorkbook(tmplist[n].getAbsolutePath());// 处理文件、迭代分数
				ExcelCount(mywb);
			} else {
				System.out.println(tmplist[n].getName());
			}
		}
	}

	public void ExcelCount(Workbook workbook) {// 单个文件处理
		for (int in = 0; in < workbook.getNumberOfSheets(); in++) {
			Sheet eSheet = workbook.getSheetAt(in);

			for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
				Row eRow = eSheet.getRow(rowNum);
				String name = rowNum + eRow.getCell(1).toString();// 限定学生姓名格式

				if (eRow.getCell(2) == null || !eRow.getCell(2).toString().contains("转班")) {

					if (in == 0 && !stuCredit.containsKey(name)) {
						int[] tmp = { 0, 0 };
						stuCredit.put(name, tmp);
						int[] dtmp = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
						Details.put(name, dtmp);
					}
				}
				int[] cdtmp = { 0, 0 };
				int[] dtmp = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				if (eRow != null) {
					for (int cellsNum = 2; cellsNum < 9; cellsNum++) {
						// System.out.println(xssfRow.getCell(cellsNum));
						// int rindex = cellsNum - 2;// 索引位置
						// int minsize = rindex + 1;// 从1开始的横坐标位置

						if (eRow.getCell(cellsNum) != null && eRow.getCell(cellsNum).toString() != "") {
							if (in == 0) {
								cdtmp[0]++;
								dtmp[8]++;
							}
							if (eRow.getCell(cellsNum).toString().contains("转班")) {
								stuCredit.remove(name);
								Details.remove(name);
								break;
							} else if (eRow.getCell(cellsNum).toString().contains("-")) {
								cdtmp[1]--;
							} else if (eRow.getCell(cellsNum).toString().contains("加入班级")) {
								continue;
							} else if (eRow.getCell(cellsNum).toString().contains("请假")) {
								cdtmp[1]--;
							} else if (eRow.getCell(cellsNum).toString().contains("打卡")) {
								cdtmp[0] += 1;
								dtmp[0]++;
							} else if (eRow.getCell(cellsNum).toString().contains("笔记")) {
								cdtmp[0] += 5;
								dtmp[1]++;
							} else if (eRow.getCell(cellsNum).toString().contains("直播")) {
								cdtmp[0] += 2;
								dtmp[2]++;
							} else if (eRow.getCell(cellsNum).toString().contains("APP签到")) {
								cdtmp[0] += 6;
								dtmp[3]++;
							} else if (eRow.getCell(cellsNum).toString().contains("单词")) {
								cdtmp[0] += 3;
								dtmp[4]++;
							} else if (eRow.getCell(cellsNum).toString().contains("小组")) {
								cdtmp[0] += 7;
								dtmp[5]++;
							} else { //
								float ctmp = Float.parseFloat(eRow.getCell(cellsNum).toString());
								if (in == 0) {
									if (ctmp >= 30) {
										cdtmp[0] += 5;
									} else {
										cdtmp[0] += ctmp / 6;
									}
									dtmp[6] += ctmp;
								} else {
									cdtmp[0] += ctmp / 4;
									dtmp[7] += ctmp;
								}

							}
						}
						cdtmp[1]++;
					}

				}
				if (stuCredit.containsKey(name)) {
					int[] cd = new int[2];
					cd[0] = stuCredit.get(name)[0] + cdtmp[0];
					if (in == 0) {
						cd[1] = stuCredit.get(name)[1] + cdtmp[1];
					} else {
						cd[1] = stuCredit.get(name)[1];
					}
					stuCredit.put(name, cd);
				}
				if (Details.containsKey(name)) {
					int[] d = new int[9];
					for (int i = 0; i < d.length; i++) {
						d[i] = Details.get(name)[i] + dtmp[i];
					}
					Details.put(name, d);
				}
			}

		}

	}

	public void print() {
		List<Entry<String, int[]>> list = new ArrayList<Map.Entry<String, int[]>>(stuCredit.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, int[]>>() {
			@Override
			public int compare(Entry<String, int[]> arg0, Entry<String, int[]> arg1) {
				// TODO Auto-generated method stub
				if (arg0.getValue()[1] == 0) {
					System.out.println(arg0.getKey());
				}
				if (arg1.getValue()[1] == 0) {
					System.out.println(arg1.getKey());
				}
				int o1 = arg0.getValue()[0] * time / arg0.getValue()[1];
				int o2 = arg1.getValue()[0] * time / arg1.getValue()[1];
				if (o1 > o2) {
					return -1;
				} else if (o1 == o2) {
					Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
					Matcher m1 = p.matcher(arg0.getKey());
					Matcher m2 = p.matcher(arg1.getKey());
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
				} else {
					return 1;
				}
			}

		});

		float max = list.get(0).getValue()[0] * time / list.get(0).getValue()[1];
		System.out.println(max);
		for (Map.Entry<String, int[]> me : list) {
			float ctmp = me.getValue()[0] * time / me.getValue()[1];
			System.out.println(me.getValue()[0] + "  " + me.getValue()[1]);
			int credit = (int) (Math.sqrt(ctmp) / Math.sqrt(max) * 100);
			System.out.println(me.getKey() + " 评分：" + credit);
		}
	}

	public void printDetails() {
		List<Entry<String, int[]>> list = new ArrayList<Map.Entry<String, int[]>>(Details.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, int[]>>() {
			@Override
			public int compare(Entry<String, int[]> arg0, Entry<String, int[]> arg1) {
				// TODO Auto-generated method stub

				Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
				Matcher m1 = p.matcher(arg0.getKey());
				Matcher m2 = p.matcher(arg1.getKey());
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
		});
		for (Map.Entry<String, int[]> me : list) {
			System.out.println(me.getKey());
			System.out.println("起床\t笔记\t直播\tAPP签到\t单词\t小组\t词汇\t题量\t打卡");
			for (int i = 0; i < me.getValue().length; i++) {
				System.out.print(me.getValue()[i] + "\t");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				ExStuStatistics ess = new ExStuStatistics();
				ess.TotalCount(filePath);
				ess.print();
				ess.printDetails();
			}
		}
	}

}
