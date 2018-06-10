package task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class MonthlyStatistics {
	// private String spath;//Դ�ļ�·��
	// private String dpath;//Ŀ���ļ���·��

	// public void createFiles(ArrayList<String> al,String path) {
	//
	// }

	public void copyFiles(ArrayList<String> al, String spath) throws IOException {

		String[] pathtmp = spath.split("\\\\");
		int tmpsize = pathtmp.length - 1;// δ���
		String path = "";
		for (int n = 0; n < tmpsize; n++) {
			path = path + pathtmp[n] + "\\";
		}
		String type = pathtmp[pathtmp.length - 1].split("\\.")[1];// ��ȡ�ļ�����
		// System.out.println(type);
		path = path + "ѧԱ�¶�ͳ�Ʊ�" + "\\";
		// System.out.println(path);

		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		for (String name : al) {
			String dpath = path + name + "_ѧϰ��������." + type;
			System.out.println(dpath);

			File source = new File(spath);
			File dest = new File(dpath);
			Files.copy(source.toPath(), dest.toPath());
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tools nl = new Tools();
		MonthlyStatistics ms = new MonthlyStatistics();

		try {
			ms.copyFiles(nl.readNameListFromExcel(nl.getWorkbook(args[0])), args[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
