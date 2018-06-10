package task.month;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RedirectionMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * C:\Users\Regretless\Desktop\作业\月度统计
		 */
		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				String fp = args[i];
				String[] tmpath = fp.split("\\\\");
				int tmpsize = tmpath.length - 1;// 未完成
				String path = "";
				for (int n = 0; n < tmpsize; n++) {
					path = path + tmpath[n] + "\\";
				}
				path = path + "评分结果.txt";
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
