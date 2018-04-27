package task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExStuStatistics {
	private Map<String, Integer> stuCredit;//ͳ���ܷ�

	public ExStuStatistics() {//��ʼ�����캯��
		stuCredit = new HashMap<String, Integer>();
	}

	public void TotalCount(File fs) throws Exception {
		File[] tmplist = fs.listFiles();
		System.out.println("ѧ������" + tmplist.length);
		for (int n = 0; n < tmplist.length; n++) {// �����ļ�����ÿ��ѧ���ĵ�
			if (!tmplist[n].isDirectory()) {
				Workbook mywb = getWorkbook(tmplist[n].getAbsolutePath());// �����ļ�����������
				ExcelCount(mywb);
			} else {
				System.out.println(tmplist[n].getName());
			}
		}
	}

	public Workbook getWorkbook(String path) throws Exception {//�ж�Excel�������
		Workbook wbtmp = null;
		InputStream is = new FileInputStream(path);
		String excelType = path.split("\\.")[1];// �����ļ�����
		if (excelType.equals("xlsx")) {
			wbtmp = new XSSFWorkbook(is);
		} else if (excelType.equals("xls")) {
			wbtmp = new HSSFWorkbook(is);
		} else {
			System.out.println("�����ļ���ʽ����");
		}
		return wbtmp;

	}

	public void ExcelCount(Workbook workbook) {//�����ļ�����
		Sheet eSheet = workbook.getSheetAt(0);// ����ǩ��ҳ

		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			String name = rowNum + eRow.getCell(1).toString();// �޶�ѧ��������ʽ
			if (!stuCredit.containsKey(name)) {
				if (eRow.getCell(2) == null || !eRow.getCell(2).toString().contentEquals("ת��")) {
					stuCredit.put(name, 0);
				}
			}
			if (eRow != null) {
				for (int cellsNum = 2; cellsNum < 9; cellsNum++) {
					// System.out.println(xssfRow.getCell(cellsNum));
					int rindex = cellsNum - 2;// ����λ��
					int minsize = rindex + 1;// ��1��ʼ�ĺ�����λ��

					
					if (eRow.getCell(cellsNum) != null){//switch������
						if (eRow.getCell(cellsNum).toString().contentEquals("ת��")) {

						} else if (eRow.getCell(cellsNum).toString().contentEquals("����༶")) {

						} else {
						}
					}
				}
			}
		}
		
		
		
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				ExStuStatistics ess = new ExStuStatistics();
				ess.TotalCount(filePath);
			}
		}
	}

}
