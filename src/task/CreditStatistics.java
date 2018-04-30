package task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class CreditStatistics {
	private ArrayList<String> stuName;// ѧ���б�
	private Map<String, String> stuMap;// �û�����ѧ������
	private ArrayList<String[]> credit;// ѧ���б�
	private String[] low;

	public CreditStatistics(Workbook workbook) {// ��ʼ��
		stuName = new ArrayList<String>();
		stuMap = new HashMap<String, String>();
		credit = new ArrayList<String[]>();
		low = new String[2];

		Tools t = new Tools();
		stuName = t.readNameListFromExcel(workbook);// ��ȡѧ���б�
		// System.out.println(stuName.size());
	}

	public int IndexContain(String s) {
		int index = 0;
		for (; index < stuName.size(); index++) {
			if (stuName.get(index).contains(s)) {
				return index;
			}
		}
		return -1;
	}

	public void Collection(Workbook workbook) {
		Sheet uSheet = workbook.getSheetAt(0);// ��ȡ�û���
		for (int rowNum = 0; rowNum <= uSheet.getLastRowNum(); rowNum++) {
			Row eRow = uSheet.getRow(rowNum);
			if (eRow != null) {
				String un = eRow.getCell(0).toString();
				String sn = eRow.getCell(2).toString();
				int id = IndexContain(sn);
				if (id > -1) {
					stuMap.put(un, stuName.get(id));
				}
			}
		}
		// System.out.println(stuMap.size());

		Sheet cSheet = workbook.getSheetAt(1);// ͳ��ѧ��
		for (int rowNum = 0; rowNum <= cSheet.getLastRowNum(); rowNum++) {
			Row eRow = cSheet.getRow(rowNum);
			if (eRow != null) {
				String untmp = eRow.getCell(1).toString();
				if (stuMap.containsKey(untmp)) {
					// System.out.println(untmp);
					String[] ctmp = new String[2];
					ctmp[0] = untmp;
					ctmp[1] = eRow.getCell(3).toString();
					credit.add(ctmp);
				}
			}
		}
	}

	public float Average() {// ����ƽ���֡�������ͷ�ѧ��
		float sum = 0;
		low = credit.get(0);
		for (String[] score : credit) {
			float s = Float.parseFloat(score[1]);
			float ltmp = Float.parseFloat(low[1]);
			if (s < ltmp) {
				low = score;
			}
			sum += s;

		}
		float a = sum / credit.size();
		return a;

	}

	public void printLow() {
		System.out.println("�ɼ���͵�ѧ����" + stuMap.get(low[0]) + " ����Ϊ��" + low[1]);
	}

//	public static Workbook getWorkbook(String path) throws Exception {
//		Workbook wbtmp = null;
//		InputStream is = new FileInputStream(path);
//		String excelType = path.split("\\.")[1];// �����ļ�����
//		if (excelType.equals("xlsx")) {
//			wbtmp = new XSSFWorkbook(is);
//		} else if (excelType.equals("xls")) {
//			wbtmp = new HSSFWorkbook(is);
//		} else {
//			System.out.println("�����ļ���ʽ����");
//		}
//		return wbtmp;
//
//	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * C:\Users\Regretless\OneDrive\�����ĵ�\����\ÿ��ͳ��\����VIP�������򿨺���ҵ�ύ������0416-0422.xlsx
		 * D:\Download\ѧ��֤��ѧ��.xls
		 */
		Tools t = new Tools();
		Workbook myWorkbook = t.getWorkbook(args[0]);
		CreditStatistics cs = new CreditStatistics(myWorkbook);
		myWorkbook = t.getWorkbook(args[1]);
		cs.Collection(myWorkbook);
		System.out.println(cs.Average());
		cs.printLow();
	}

}
