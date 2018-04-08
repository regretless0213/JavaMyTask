package task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int initial = 2;// Excel����г�ʼ��
		int interval = 5;// ѡ�����ڼ��
		int standard = 3;//δ���ı�׼
		
		for (int i = 0; i < args.length; i++) {// ������ļ���·��
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				DirFilter df = new DirFilter();
				df.TotalCount(filePath);
			} else {
				if (initial + interval > 9) {
					System.out.println("initial��interval��ʼֵ����");
					return;
				}
				ExcelProcess ep = new ExcelProcess(initial,interval,standard);	
				Workbook myWorkbook = null;
				InputStream is = new FileInputStream(args[i]);
				@SuppressWarnings("resource")
				String excelType = args[i].split("\\.")[1];
				if (excelType.equals("xlsx")) {
					myWorkbook = new XSSFWorkbook(is);
				} else if (excelType.equals("xls")) {
					myWorkbook = new HSSFWorkbook(is);
				} else {
					System.out.println("�����ļ���ʽ����");
				}
				ep.TotalCount(myWorkbook);
				ep.print();
			}
		}

	}

}
