package task.week;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;

import task.Tools;

public class SummaryMain {
	/*
	 * C:\Users\Regretless\OneDrive\�����ĵ�\����\ÿ��ͳ��\����-�������-�Ž���-0409-0415
	 * C:\Users\Regretless\OneDrive\�����ĵ�\����\ÿ��ͳ��\����VIP�������򿨺���ҵ�ύ������0409-0415.xlsx
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tools t = new Tools();
		int initial = 2;// Excel����г�ʼ��
		int interval = 7;// ѡ�����ڼ��
		int standard = 7;// δ���ı�׼
		
		boolean[] index = { true, true, true };
		// false ,false 0.�Ƿ�ͳ�����ܽ���ܼƻ���1.�Ƿ�ͳ��ѧ����ҵ�ύ�����2.�Ƿ��ӡƽ���ּ���ͷ�
		boolean CreateOrNot = false;// true
		
		String creditpath = "C:\\Users\\Regretless\\Desktop\\��ҵ\\ѧ��֤��ѧ��.xls";//���ֱ��·��

		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);// �����·��
			if (!filePath.exists()) {
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
				
				Workbook myWorkbook = t.getWorkbook(args[i]);
				if (index[1]) {
					ep.setCMON(0);//�Ƿ�ͳ��С���Ա��1��0��Ӱ�쵽���ϸ�����
					ep.TotalCount(myWorkbook);
					
					
					ep.printBadStuList();
					System.out.println();
					
					ep.printRatio();
					System.out.println();
					
					ep.printDaily();
					System.out.println();
					
					ep.printReqForLeave();
					
//					ep.printGroupMember();
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
					// String todayDir = nm.createDir();
					// nm.createDirByDateAndName(todayDir, namelist);
					// nm.createWeekTaskDir(mainDir+sparator+"������", namelist);
					// nm.tryToCopyOldWeek();
					t.mkStuDir(path, myWorkbook);
					// nm.mkdirs(NormalManagement.mainDir + sparator + "������", namelist);
				}
				if(index[2]) {
					CreditStatistics cs = new CreditStatistics(t.readNameListFromExcel(myWorkbook));

					myWorkbook = t.getWorkbook(creditpath);
					cs.Collection(myWorkbook);
					System.out.println(cs.Average());
					cs.printLow();
				}
			}
		}

	}

}
