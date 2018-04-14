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
	private static int initial;// Excel����г�ʼ��
	private static int interval;// ѡ�����ڼ��
	private static int standard;// δ���ı�׼

	private Map<String, Integer> stuName;// ѧ���б�����ͳ����ҵ�ύ���
	private ArrayList<String> dcResult;// ������¼����δ���ѧ��
	private ArrayList<String> zhResult;// ������¼�ۺ�δ���ѧ��

	private ArrayList<Integer> Result;// ��¼ÿ���ж���ѧ��ǩ�����ύ��ҵ
	private ArrayList<int[]> stuChange;// ��¼��Ա�䶯
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

		// ��ʼ��
		stuName = new HashMap<String, Integer>();
		dcResult = new ArrayList<String>();
		zhResult = new ArrayList<String>();
		Result = new ArrayList<Integer>();
		stuChange = new ArrayList<int[]>();

	}

	public void TotalCount(Workbook workbook) {

		Sheet eSheet = workbook.getSheetAt(0);// ����ǩ��ҳ
		// int num = 0;

		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			String name = rowNum + eRow.getCell(1).toString();// �޶�ѧ��������ʽ
			stuName.put(name, 0);
			if (eRow != null) {
				int num = 0;// ����δ����ҵ����
				for (int cellsNum = initial; cellsNum < (initial + interval); cellsNum++) {
					// System.out.println(xssfRow.getCell(cellsNum));
					int rindex = cellsNum - initial;// ����λ��
					int minsize = rindex + 1;//
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
						if (eRow.getCell(cellsNum).toString().contentEquals("ת��")) {
							stuName.remove(name);
							leave++;
							break;
						} else if (eRow.getCell(cellsNum).toString().contentEquals("����༶")) {
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
							if (!zRow.getCell(column).toString().contentEquals("����༶")) {
								Result.set(rindex + interval, Result.get(rindex + interval) + 1);
							}
							break;
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

	public void print() {
		// ����
		dcResult.sort(sc);
		zhResult.sort(sc);

		System.out.println("����ǩ��δ��꣨����" + standard + "������δǩ����������");
		for (String r : dcResult) {
			System.out.println(r);
		}
		System.out.println("����" + dcResult.size() + "ͬѧδǩ��\n");
		System.out.println("�ۺ���ҵ�ύδ��꣨����" + standard + "������δ�ύ��������");
		for (String r : zhResult) {
			System.out.println(r);
		}
		System.out.println("����" + zhResult.size() + "ͬѧδ�ύ��ҵ");
		System.out.println("����Ӣ��������£�");
		for (int d = 0; d < (Result.size() / 2); d++) {
			System.out.println("��" + (d + 1) + "��" + Result.get(d));
		}
		System.out.println("�����ۺϴ������£�");
		for (int d = (Result.size() / 2); d < Result.size(); d++) {
			System.out.println("��" + (d - (Result.size() / 2) + 1) + "��" + Result.get(d));
		}
		System.out.println("������Ա�䶯�����");
		for (int[] r : stuChange) {
			System.out.println("��" + r[1] + "��ѧ������Ϊ" + (r[0] - leave));
		}
		System.out.println("Ŀǰѧ��������Ϊ" + stuName.size());
	}

}
