package task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * C:\Users\Regretless\OneDrive\�����ĵ�\����\ÿ��ͳ��\����-�������-�Ž���-0402-0408
		 * C:\Users\Regretless\OneDrive\�����ĵ�\����\ÿ��ͳ��\����VIP�������򿨺���ҵ�ύ������0402-0408.xlsx
		 */
		int initial = 2;// Excel����г�ʼ��
		int interval = 7;// ѡ�����ڼ��
		int standard = 7;// δ���ı�׼
		boolean[] index = { false, false };// 0.�Ƿ�ͳ�����ܽ���ܼƻ���1.�Ƿ�ͳ��ѧ����ҵ�ύ���
		boolean CreateOrNot = true;

		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);// �����·��
			if (filePath.isDirectory() && index[0]) {// ������ļ��У�ͳ�����ܽ���ܼƻ�
				DirFilter df = new DirFilter();
				df.TotalCount(filePath);
			} else {// ������ļ���ͳ����ҵ�ύ���
				if (initial + interval > 9) {
					System.out.println("initial��interval��ʼֵ����");
					return;
				}
				ExcelProcess ep = new ExcelProcess(initial, interval, standard);
				Workbook myWorkbook = null;
				InputStream is = new FileInputStream(args[i]);
				@SuppressWarnings("resource")
				String excelType = args[i].split("\\.")[1];// �����ļ�����
				if (excelType.equals("xlsx")) {
					myWorkbook = new XSSFWorkbook(is);
				} else if (excelType.equals("xls")) {
					myWorkbook = new HSSFWorkbook(is);
				} else {
					System.out.println("�����ļ���ʽ����");
				}
				if (index[1]) {
					ep.TotalCount(myWorkbook);
					ep.print();
				}
				if (CreateOrNot) {
					String fp = args[i];
					String[] tmpath = fp.split("\\\\");
					int tmpsize = tmpath.length - 1;// δ���
					String path = "";
					for (int n = 0; n < tmpsize; n++) {
						path = path + tmpath[n] + "\\";
					}
					System.out.println(path);
					CreateDir cd = new CreateDir(path);
					// String todayDir = nm.createDir();
					ArrayList<String> namelist = cd.readNameListFromExcel(myWorkbook);
					// nm.createDirByDateAndName(todayDir, namelist);
					// nm.createWeekTaskDir(mainDir+sparator+"������", namelist);
					cd.mkWeekDir();
					// nm.tryToCopyOldWeek();
					cd.mkStuDir(namelist);
					// nm.mkdirs(NormalManagement.mainDir + sparator + "������", namelist);
				}
			}
		}

	}

}
