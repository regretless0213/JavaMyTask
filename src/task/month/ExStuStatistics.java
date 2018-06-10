package task.month;

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

import task.Tools;

public class ExStuStatistics {
	private Map<String, int[]> stuCredit;// ͳ���ܷ�
	private Map<String, int[]> Details;
	private static int time = 100;

	private Tools t;

	public ExStuStatistics() {// ��ʼ�����캯��
		stuCredit = new HashMap<String, int[]>();
		Details = new HashMap<String, int[]>();

		t = new Tools();
	}

	public void TotalCount(File fs) {
		File[] tmplist = fs.listFiles();
		for (int n = 0; n < tmplist.length; n++) {// �����ļ�����ÿ��ѧ���ĵ�
			if (!tmplist[n].isDirectory()) {
				Workbook mywb = t.getWorkbook(tmplist[n].getAbsolutePath());// �����ļ�����������
				ExcelCount(mywb);
			} else {
				System.out.println(tmplist[n].getName());
			}
		}
	}

	public void ExcelCount(Workbook workbook) {// �����ļ�����
		for (int in = 0; in < workbook.getNumberOfSheets(); in++) {
			Sheet eSheet = workbook.getSheetAt(in);

			for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
				Row eRow = eSheet.getRow(rowNum);
				if (eRow != null) {
					String name = rowNum + eRow.getCell(1).toString();// �޶�ѧ��������ʽ

					if (!t.LeaveOrNot(name)) {
						if (in == 0 && !stuCredit.containsKey(name)) {
							int[] tmp = { 0, 0, 0 };// 0λ�����ܷ֣�1λ�����ϯ������2λ�����Ƿ���С���Ա
							stuCredit.put(name, tmp);
							int[] dtmp = { 0, 0, 0, 0, 0, 0, 0, 0 };// ���ʡ��򿨡���ѧ��Ӣ�רҵ�Ρ����Ρ���١�����
							Details.put(name, dtmp);
						}
					}
					int[] cdtmp = { 0, 0 };
					int[] dtmp = { 0, 0, 0, 0, 0, 0, 0, 0 };

					for (int cellsNum = 2; cellsNum < 9; cellsNum++) {
						// System.out.println(xssfRow.getCell(cellsNum));
						// int rindex = cellsNum - 2;// ����λ��
						// int minsize = rindex + 1;// ��1��ʼ�ĺ�����λ��

						if (eRow.getCell(cellsNum) != null && eRow.getCell(cellsNum).toString() != "") {
							if (in == 0) {
								cdtmp[0]++;
							}
							if (!stuCredit.containsKey(name)) {
								System.out.println(name + "���ڰ༶�С�");
								break;
							} else if (eRow.getCell(cellsNum).toString().contains("-")) {
								cdtmp[1]--;
							} else if (eRow.getCell(cellsNum).toString().contains("����༶")) {
								continue;
							} else if (eRow.getCell(cellsNum).toString().contains("���")) {// �����ύ��2�֣���
								cdtmp[0] += 2;
								dtmp[6]++;
							} else if (eRow.getCell(cellsNum).toString().contains("��")) {// ���֣�������
								cdtmp[0] += 1;
							} else if (eRow.getCell(cellsNum).toString().contains("�ʼ�")) {
								cdtmp[0] += 12;
							} else if (eRow.getCell(cellsNum).toString().contains("С��")) {
								cdtmp[0] += 5;
							} else if (eRow.getCell(cellsNum).toString().contains("��ʽ")) {
								cdtmp[0] += 3;
							} else if (eRow.getCell(cellsNum).toString().contains("ֱ��")) {
								cdtmp[0] += 6;
							} else if (eRow.getCell(cellsNum).toString().contains("����")) {
								cdtmp[0] += 5;
							} else if (eRow.getCell(cellsNum).toString().contains("д��")) {
								cdtmp[0] += 10;
							} else if (eRow.getCell(cellsNum).toString().contains("����")) {
								cdtmp[0] += 9;
							} else if (eRow.getCell(cellsNum).toString().contains("С��")) {
								cdtmp[0] += 20;
							} else if (eRow.getCell(cellsNum).toString().contains("���")) {
								cdtmp[0] += 11;
							} else { //
								float ctmp = Float.parseFloat(eRow.getCell(cellsNum).toString());
								if (in == 0) {
									if (ctmp > 60) {
										ctmp = 60;
									}
									cdtmp[0] += ctmp;
								} else {
									if (ctmp > 20) {
										ctmp = 20;
									}
									cdtmp[0] += ctmp;
								}

							}
						}
						cdtmp[1]++;
					}

					if (stuCredit.containsKey(name)) {
						dtmp[in] += cdtmp[0];
						int[] cd = new int[3];
						switch (in) { // ӳ�乫ʽ
						case 0:
							cd[0] = stuCredit.get(name)[0] + cdtmp[0] * 5 / 12;
							break; // ���ʴ�ӳ��
						case 1:
							cd[0] = stuCredit.get(name)[0] + cdtmp[0] * 10 / 7;
							break; // �𴲴�
						case 2:
							cd[0] = stuCredit.get(name)[0] + cdtmp[0] * 2 / 5;
							break; // ��ѧ��ҵ
						case 3:
							cd[0] = stuCredit.get(name)[0] + cdtmp[0] * 3 / 10;
							break; // Ӣ��
						case 4:
							cd[0] = stuCredit.get(name)[0] + cdtmp[0] * 3 / 10;
							break; // רҵ��
						case 5:
							cd[0] = stuCredit.get(name)[0] + cdtmp[0] / 4;
							break; // ����
						default:
							System.out.println("����ӳ��ϵͳ����");
						}

						if (in == 0) {// ��������
							cd[1] = stuCredit.get(name)[1] + cdtmp[1];
						} else {
							cd[1] = stuCredit.get(name)[1];
						}
						cd[2] = stuCredit.get(name)[2];
						if (in == 2 && eRow.getCell(9) != null && eRow.getCell(9).toString() != "") {
							cd[2] = 1;
						}
						stuCredit.put(name, cd);
					}
					if (Details.containsKey(name)) {
						int[] d = new int[8];
						for (int i = 0; i < d.length - 1; i++) {
							d[i] = Details.get(name)[i] + dtmp[i];
						}
						d[7] = stuCredit.get(name)[1];
						Details.put(name, d);
					}
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
				float o1 = getFraction(arg0);
				float o2 = getFraction(arg1);
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

		float max = getFraction(list.get(0));
		// System.out.println(max);
		int num = 1;
		for (Map.Entry<String, int[]> me : list) {
			float ctmp = getFraction(me);
			// System.out.println(me.getValue()[0] + " " + me.getValue()[1]);
			int credit = (int) (Math.sqrt(ctmp) / Math.sqrt(max) * 60);// ӳ�������100��

			if (me.getValue()[2] == 0) {
				System.out.println((list.indexOf(me) + 1) + ".\t" + me.getKey() + "\t���֣�" + credit + "\t" + num);
				num++;
			} else {
				System.out.println((list.indexOf(me) + 1) + ".\t" + me.getKey() + "\t���֣�" + credit + "\tС��");
			}
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
		System.out.println("����\t����\t��\t��ѧ\tӢ��\tרҵ��\t����\t���\t����");
		for (Map.Entry<String, int[]> me : list) {
			System.out.print(me.getKey() + "\t");
			for (int i = 0; i < me.getValue().length; i++) {
				System.out.print(me.getValue()[i] + "\t");
			}
			System.out.println();
		}
	}

	private float getFraction(Map.Entry<String, int[]> e) {
		return e.getValue()[0] * time / e.getValue()[1];

	}

}
