package task.month;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RedirectionMain {
	public static Map<String, int[]> getResult(Map<String, int[]> rl, Map<String, Integer> sl) {//保存总成绩单
		if (rl.isEmpty() || sl.isEmpty()) {
			System.out.println("数据有误！");
		} else {
			for (Map.Entry<String, int[]> re : rl.entrySet()) {
				for (Map.Entry<String, Integer> se : sl.entrySet()) {
					if(re.getKey().contains(se.getKey())) {
						int[] tmp = new int[2];
						tmp[0] = re.getValue()[0] + se.getValue();
						tmp[1] = re.getValue()[1];
						re.setValue(tmp);
					}
				}
			}
		}
		return rl;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * C:\Users\Regretless\Desktop\作业\月度统计
		 */
		Map<String, int[]> stuRL = new HashMap<String, int[]>();//日常评分列表
		Map<String, Integer> stuSL = new HashMap<String, Integer>();//测试成绩列表
		//重定向输出流
		String path = "C:\\Users\\Regretless\\Desktop\\作业\\评分结果.txt";
//		System.out.println(path);
		PrintStream ps = new PrintStream(new FileOutputStream(path));
		System.setOut(ps);

		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				String fp = args[i];
				String[] tmpath = fp.split("\\\\");//拆分字符串
				int tmpsize = tmpath.length - 1;
//				String path = "";
//				for (int n = 0; n < tmpsize; n++) {
//					path = path + tmpath[n] + "\\";
//				}
//				int kind = -1;
				if (tmpath[tmpsize].contentEquals("月度统计")) {
//					kind = 0;
					ExStuStatistics ess = new ExStuStatistics();
					ess.TotalCount(filePath);
					ess.print();
					ess.printDetails();
					stuRL = ess.getStaticResult();
				}
				if (tmpath[tmpsize].contentEquals("测试成绩")) {
//					kind = 1;
					TestScore ts = new TestScore();
					ts.TotalCount(filePath);
					ts.print();
					stuSL = ts.getScoreResult();
				}
				// 重定向
//				if (kind == 0) {
					
//				}
//				if (kind == 1) {
					
//				}
			}
		}
		Map<String, int[]> stuResult = getResult(stuRL, stuSL);
		ListSort ls = new ListSort(stuResult);
		List<Entry<String, int[]>> list = new ArrayList<Map.Entry<String, int[]>>(stuResult.entrySet());
		Collections.sort(list, ls);

		int num = 1;//区分小组成员与普通成员；打印成绩单
		System.out.println("最终成绩排名：");
		for (Map.Entry<String, int[]> me : list) {

			if (me.getValue()[1] == 0) {
				System.out.println(
						(list.indexOf(me) + 1) + ".\t" + me.getKey() + "\t评分：" + me.getValue()[0] + "\t" + num);
				num++;
			} else {
				System.out.println((list.indexOf(me) + 1) + ".\t" + me.getKey() + "\t评分：" + me.getValue()[0] + "\t小组");
			}
		}
	}

}
