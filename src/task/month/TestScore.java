package task.month;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import task.Tools;

public class TestScore {
	private Map<String, Float> stuScore;// �ɼ���

	private Tools t;

	public TestScore() {//��ʼ�����캯��
		stuScore = new HashMap<String, Float>();

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

	private void ExcelCount(Workbook workbook) {
		// for (int in = 0; in < workbook.getNumberOfSheets(); in++) {
		if (workbook.getNumberOfSheets() != 1) {
			System.out.println("�������Ű������������Ű棡");
			System.exit(0);
		}

		Sheet eSheet = workbook.getSheetAt(0);
		Row inRow = eSheet.getRow(0);
		int nameCell = -1;
		ArrayList<Integer> scoreCell = new ArrayList<Integer>();
		int[] check = { 0, 0 };// �����жϱ���Ű����Ƿ�������������͡��ɼ��������С��ɼ������Բ�ֹһ��
		if (inRow != null) {
			// System.out.println("PhysicalNumberOfCells:" +
			// inRow.getPhysicalNumberOfCells());
			// System.out.println("PhysicalNumberOfRows:" +
			// eSheet.getPhysicalNumberOfRows());
			
			//��ȡ��һ���������б���һ���ǳɼ��б�
			for (int cellsNum = 0; cellsNum < inRow.getPhysicalNumberOfCells(); cellsNum++) {
				if (inRow.getCell(cellsNum) != null) {

					if (inRow.getCell(cellsNum).toString().contains("����") && check[0] == 0) {
						nameCell = cellsNum;
						check[0]--;
					}
					if (inRow.getCell(cellsNum).toString().contains("�ɼ�") && check[1] <= 0) {
						scoreCell.add(cellsNum);
						check[1]--;
					}

				}
			}
			int result = check[0] + check[1];
			if (result >= -1 || nameCell == -1) {
				System.out.println("����ͷ�Ű������������Ű棡");
				System.exit(0);
			}
			for (int rowNum = 1; rowNum < eSheet.getPhysicalNumberOfRows(); rowNum++) {// ��ȡ�ɼ�
				Row eRow = eSheet.getRow(rowNum);
				if (eRow != null) {
					if (eRow.getCell(nameCell) != null && eRow.getCell(nameCell).toString() != "") {
						String name = eRow.getCell(nameCell).toString();
						float score = 0;
						for (int i : scoreCell) {// ���Գɼ��п��ܲ�ֹһ��
							if (eRow.getCell(i) != null && eRow.getCell(i).toString() != "") {// �ϼƷ���

								score += Float.parseFloat(eRow.getCell(i).toString());
							}
						}
						if (!stuScore.containsKey(name)) {
							stuScore.put(name, score);
						} else {
							float tmp = stuScore.get(name) + score;
							stuScore.put(name, tmp);
						}
					}
				}

			}

		} else {
			System.out.println("inRow��ʼ�г���");
		}
		// }
	}

	private float getMax() {//��ȡ���ֵ
		float max = 0;
		for (Map.Entry<String, Float> me : stuScore.entrySet()) {
			if (max < me.getValue()) {
				max = me.getValue();
			}
		}
		return max;
	}

	public Map<String, Integer> getScoreResult() {// �������򣻱�׼��
		Map<String, Integer> stuSR = new HashMap<String, Integer>();
		float max = getMax();
		for (Map.Entry<String, Float> me : stuScore.entrySet()) {
			if (me.getValue() == 0) {
				continue;
			} else {
				int score = (int) (me.getValue() / max * 10);
				stuSR.put(me.getKey(), score);
			}
		}
		return stuSR;

	}

	public void print() {// ����ɼ�����
		System.out.println("���Գɼ��÷֣�");
		for (Map.Entry<String, Integer> me : getScoreResult().entrySet()) {
			System.out.println(me.getKey() + "\t" + me.getValue());
		}
		System.out.println(stuScore.size() + "��");
	}

//	public static void main(String[] args) {
//		for (int i = 0; i < args.length; i++) {
//			File filePath = new File(args[i]);
//			if (filePath.isDirectory()) {
//				TestScore ts = new TestScore();
//				ts.TotalCount(filePath);
//				ts.print();
//			}
//		}
//	}
}
