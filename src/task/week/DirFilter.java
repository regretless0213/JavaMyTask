package task.week;

import java.io.File;
import java.util.ArrayList;

import task.Tools;

public class DirFilter {
	// private static ArrayList<Integer> tmp;
	private ArrayList<String> badstu;
	private Tools t;
	// private static ArrayList<String> hardstu;

	

	public String process(String path) {//���ļ�·������ȡѧ������
		String reg = ".*\\\\(.*)";
		String s = path.replaceAll(reg, "$1");
		return s;

	}

	public DirFilter() {
		badstu = new ArrayList<String>();
		
		t = new Tools();
	}

	public void TotalCount(File fs) {
		File[] tmplist = fs.listFiles();
		System.out.println("ѧ������" + tmplist.length);
		for (int n = 0; n < tmplist.length; n++) {// �����ļ�����ÿ��ѧ���ĵ�
			if (tmplist[n].isDirectory()) {
				File[] filelist = tmplist[n].listFiles();
				if (filelist.length == 0) {// �ж��ĵ����Ƿ�����ҵ
					String name = process(tmplist[n].getAbsolutePath());

					badstu.add(name);
				} else {
					// �ĵ��а����ܼƻ������ܽ�
				}
			} else {
				System.out.println(tmplist[n].getName());
			}
		}
		badstu.sort(t.sc);
		System.out.println("����" + badstu.size() + "λͬѧδ�ύ������");
		for (String stuname : badstu) {
			System.out.println(stuname);
		}

	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		// ��ʼ��
//
//	}

}
