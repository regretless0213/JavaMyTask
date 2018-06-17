package task.week;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import task.Tools;

public class ExcelProcess {
	private static int initial;// Excel����г�ʼ��
	private static int interval;// ѡ�����ڼ��
	private static int standard;// δ���ı�׼

	private Map<String, Integer> stuName;// ѧ���б�����ͳ����ҵ�ύ���
	private ArrayList<String> dcResult;// ������¼����δ���ѧ��
	private ArrayList<String> zhResult;// ������¼�ۺ�δ���ѧ��

	private ArrayList<Integer> Result;// ��¼ÿ���ж���ѧ��ǩ�����ύ��ҵ
	private ArrayList<int[]> stuChange;// ��¼��Ա�䶯
	private int leave;

	private int cmon;// ��ǰ�Ƿ�ͳ��С���Ա���������ϸ�ѧ�������Ƿ�С���Ա��������
	private ArrayList<String> groupStu;// ������¼С���Ա
	private ArrayList<String[]> rlStu;// ������¼���ѧ��
	private ArrayList<String[]> stuDaily;// ����ͳ��ÿλѧ�����ܴ���

	private Tools t;


	public ExcelProcess(int initialNum, int intervalNum, int standardNum) {//��ʼ�����캯��
		initial = initialNum;
		interval = intervalNum;
		standard = standardNum;
		leave = 0;
		cmon = 1;

		// ��ʼ��
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
	// {//�滻��tools.LeaveOrNot###########�����
	// Sheet eSheet = wb.getSheetAt(0);
	// for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
	// Row eRow = eSheet.getRow(rowNum);
	// String name = rowNum + eRow.getCell(1).toString();
	// if (n.equals(name)) {
	// if (eRow.getCell(2) != null && eRow.getCell(2).toString().contains("ת��")) {
	// return true;
	// }
	// }
	// }
	// // System.out.println(name+"������������");
	// return false;
	//
	// }

	public void TotalCount(Workbook workbook) {
		Sheet eSheet = workbook.getSheetAt(0);// ����ǩ��ҳ
		// int num = 0;

		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			if (eRow.getCell(1) == null) {
				break;
			}
			String name = rowNum + eRow.getCell(1).toString();// �޶�ѧ��������ʽ
			if (!t.LeaveOrNot(name)) {
				stuName.put(name, 0);
			} else {
				leave++;
			}
			if (eRow != null) {
				int num = 0;// ����δ����ҵ����
				for (int cellsNum = initial; cellsNum < (initial + interval); cellsNum++) {
					// System.out.println(xssfRow.getCell(cellsNum));
					int rindex = cellsNum - initial;// ����λ��
					int minsize = rindex + 1;// ��¼���ڼ�
					if (Result.size() < minsize) {
						Result.add(0);
					}
					// �ж��Ƿ����ύ��ҵ�ļ�¼
					if (eRow.getCell(cellsNum) == null) {
						num++;
					} else if (eRow.getCell(cellsNum).toString() == "") {
						num++;
					} else {
						num = 0;
						if (!stuName.containsKey(name)) {
							System.out.println(name + "���ڰ༶�С�");
							break;
						} else if (eRow.getCell(cellsNum).toString().contains("����༶")) {
							int[] change = new int[2];
							change[0] = rowNum;
							change[1] = minsize;
							stuChange.add(change);
						} else if (eRow.getCell(cellsNum).toString().contains("���")) {
							String[] rl = new String[2];
							rl[0] = name;
							rl[1] = "��" + minsize;
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

		Sheet dSheet = workbook.getSheetAt(1);//��ҳ
		for (int rowNum = 1; rowNum <= dSheet.getLastRowNum(); rowNum++) {
			Row dRow = dSheet.getRow(rowNum);
			if (dRow.getCell(1) == null) {
				break;
			}
			String name = rowNum + dRow.getCell(1).toString();// �޶�ѧ��������ʽ
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

		// �ۺ���ҵͳ��
		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			for (int column = initial; column < (initial + interval); column++) {
				int rindex = column - initial;
				if (rowNum == 1) {
					Result.add(0);
				}
				int daycount = 0;//ȷ��һ�����ſ�Ŀδ�ύ����ֻͳ��һ��

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
							// if (!zRow.getCell(column).toString().contentEquals("����༶")) {
							Result.set(rindex + interval, Result.get(rindex + interval) + 1);
							// }
							if (zRow.getCell(column).toString().contains("���")) {
								int minsize = rindex + 1;// ��¼���ڼ�
								String[] rl = new String[2];
								rl[0] = name;
								int day = minsize + 5;
								if (day > 7) {
									day = day - 7;
								}
								rl[1] = "��" + day;
								rlStu.add(rl);
							}
							break;
						}
						if (numSheet == 2 && cmon == 1) {// ��ȡС���Ա && zRow.getCell(9).toString() != ""
							if (zRow.getCell(9) != null && zRow.getCell(9).toString().contains("��Ա")) {
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
			if (stuName.get(stu) >= standard) {// �洢�ۺ�δ����ѧ���б�
				zhResult.add(stu);
			}
		}

	}

	public void printBadStuList() {	//δ���ѧ������
		// ����
		dcResult.sort(t.sc);
		zhResult.sort(t.sc);

		System.out.println("����ǩ��δ��꣨����" + standard + "������δǩ����������");
		for (String r : dcResult) {
			System.out.println(r);
		}
		System.out.println("����" + dcResult.size() + "ͬѧδǩ��\n");
		System.out.println("�ۺ���ҵ�ύδ��꣨����" + standard + "������δ�ύ��������");
		for (String r : zhResult) {
			if (!groupStu.contains(r)) {
				System.out.println(r);
			}

		}
		System.out.println("����" + (zhResult.size() - groupStu.size()) + "ͬѧδ�ύ��ҵ\n");
	}

	public void printRatio() {	//��ӡ����
		System.out.println("����Ӣ��������£�");
		for (int d = 0; d < (Result.size() / 2); d++) {
			System.out.println("���ܵ�" + (d + 1) + "�죬�� �ˣ�������" + Result.get(d));
		}
		System.out.println("�����ۺϴ������£�");
		for (int d = (Result.size() / 2); d < Result.size(); d++) {
			System.out.println("���ܵ�" + (d - (Result.size() / 2) + 1) + "�죬�� �ˣ�������" + Result.get(d));
		}
		System.out.println("������Ա�䶯�����");
		for (int[] r : stuChange) {
			if (r[1] > 1) {
				System.out.println("���ܵ�" + (r[1] - 1) + "�켰֮ǰѧ������Ϊ" + (r[0] - leave - 1));
			} else {
				System.out.println("���ܵ�" + r[1] + "��ѧ������Ϊ" + (r[0] - leave));
			}
		}
		System.out.println("Ŀǰѧ��������Ϊ" + stuName.size());
	}

	public void printDaily() {	//��ӡ�򿨴���
		System.out.println("���ܴ򿨴���ͳ�ƣ�");
		for (String[] ds : stuDaily) {
			System.out.println(ds[0] + "\t:" + ds[1]);
		}
	}

	public void printReqForLeave() {	//��ӡ�������
		String name = "";
		for (String[] rl : rlStu) {
			if (!rl[0].equals(name)) {
				name = rl[0];
				System.out.println();
				System.out.print(rl[0] + " ��٣�" + rl[1]);
			} else {
				System.out.print(" " + rl[1]);
			}
		}
		System.out.println();
	}

	public void printGroupMember() {	//��ӡ�������
		System.out.println(groupStu.size() + "��С���Ա��");
		for (String name : groupStu) {
			System.out.println(name);
		}
	}

	public void setCMON(int b) {//�趨�Ƿ�ͳ��С���Ա��Ĭ��ͳ��
		cmon = b;
	}
}
