package task.month;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RedirectionMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * C:\Users\Regretless\Desktop\��ҵ\�¶�ͳ��
		 */
		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				String fp = args[i];
				String[] tmpath = fp.split("\\\\");
				int tmpsize = tmpath.length - 1;// δ���
				String path = "";
				for (int n = 0; n < tmpsize; n++) {
					path = path + tmpath[n] + "\\";
				}
				path = path + "���ֽ��.txt";
				System.out.println(path);
				PrintStream ps = new PrintStream(new FileOutputStream(path));
				System.setOut(ps);
				ExStuStatistics ess = new ExStuStatistics();
				ess.TotalCount(filePath);
				ess.print();
				ess.printDetails();
			}
		}
	}

}
