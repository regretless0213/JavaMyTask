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
	public static Map<String, int[]> getResult(Map<String, int[]> rl, Map<String, Integer> sl) {//�����ܳɼ���
		if (rl.isEmpty() || sl.isEmpty()) {
			System.out.println("��������");
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
		 * C:\Users\Regretless\Desktop\��ҵ\�¶�ͳ��
		 */
		Map<String, int[]> stuRL = new HashMap<String, int[]>();//�ճ������б�
		Map<String, Integer> stuSL = new HashMap<String, Integer>();//���Գɼ��б�
		//�ض��������
		String path = "C:\\Users\\Regretless\\Desktop\\��ҵ\\���ֽ��.txt";
//		System.out.println(path);
		PrintStream ps = new PrintStream(new FileOutputStream(path));
		System.setOut(ps);

		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				String fp = args[i];
				String[] tmpath = fp.split("\\\\");//����ַ���
				int tmpsize = tmpath.length - 1;
//				String path = "";
//				for (int n = 0; n < tmpsize; n++) {
//					path = path + tmpath[n] + "\\";
//				}
//				int kind = -1;
				if (tmpath[tmpsize].contentEquals("�¶�ͳ��")) {
//					kind = 0;
					ExStuStatistics ess = new ExStuStatistics();
					ess.TotalCount(filePath);
					ess.print();
					ess.printDetails();
					stuRL = ess.getStaticResult();
				}
				if (tmpath[tmpsize].contentEquals("���Գɼ�")) {
//					kind = 1;
					TestScore ts = new TestScore();
					ts.TotalCount(filePath);
					ts.print();
					stuSL = ts.getScoreResult();
				}
				// �ض���
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

		int num = 1;//����С���Ա����ͨ��Ա����ӡ�ɼ���
		System.out.println("���ճɼ�������");
		for (Map.Entry<String, int[]> me : list) {

			if (me.getValue()[1] == 0) {
				System.out.println(
						(list.indexOf(me) + 1) + ".\t" + me.getKey() + "\t���֣�" + me.getValue()[0] + "\t" + num);
				num++;
			} else {
				System.out.println((list.indexOf(me) + 1) + ".\t" + me.getKey() + "\t���֣�" + me.getValue()[0] + "\tС��");
			}
		}
	}

}
