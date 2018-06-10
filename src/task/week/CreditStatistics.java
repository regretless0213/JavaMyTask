package task.week;

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

	public CreditStatistics(ArrayList<String> al) {// ��ʼ��
		stuName = new ArrayList<String>();
		stuMap = new HashMap<String, String>();
		credit = new ArrayList<String[]>();
		low = new String[2];

		stuName = al;// ��ȡѧ���б�
		// System.out.println(stuName.size());
	}

	public int IndexContain(String s) {
		for (int index = 0; index < stuName.size(); index++) {
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



}
