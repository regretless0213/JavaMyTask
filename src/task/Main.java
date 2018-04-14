package task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	/*
	 * C:\Users\Regretless\OneDrive\�����ĵ�\����\ÿ��ͳ��\����-�������-�Ž���-0409-0415
	 * C:\Users\Regretless\OneDrive\�����ĵ�\����\ÿ��ͳ��\����VIP�������򿨺���ҵ�ύ������0409-0415.xlsx
	 */

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		int initial = 2;// Excel����г�ʼ��
		int interval = 5;// ѡ�����ڼ��
		int standard = 5;// δ���ı�׼
		boolean[] index = { true, true };// false ,false 0.�Ƿ�ͳ�����ܽ���ܼƻ���1.�Ƿ�ͳ��ѧ����ҵ�ύ���
		boolean CreateOrNot = false;// true

		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);// �����·��
			if(!filePath.exists()) {
				System.out.println("���ļ�����δ������");
				CreateOrNot = true;
				continue;
			}
			if (filePath.isDirectory()) {// ������ļ��У�ͳ�����ܽ���ܼƻ�
				DirFilter df = new DirFilter();
				if (index[0]) {
					df.TotalCount(filePath);
				}
			} else {// ������ļ���ͳ����ҵ�ύ���
				if (initial + interval > 9) {
					System.out.println("initial��interval��ʼֵ����");
					return;
				}
				ExcelProcess ep = new ExcelProcess(initial, interval, standard);
				Workbook myWorkbook = null;
				InputStream is = new FileInputStream(args[i]);
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
					CreateDir cd = new CreateDir(path, myWorkbook);
					// String todayDir = nm.createDir();
					// nm.createDirByDateAndName(todayDir, namelist);
					// nm.createWeekTaskDir(mainDir+sparator+"������", namelist);
					// nm.tryToCopyOldWeek();
					cd.mkStuDir();
					// nm.mkdirs(NormalManagement.mainDir + sparator + "������", namelist);
				}
			}
		}

	}

}
