package task;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirFilter {
	// private static ArrayList<Integer> tmp;
	private static ArrayList<String> badstu;
	// private static ArrayList<String> hardstu;
	static Comparator<Integer> c = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			// TODO Auto-generated method stub
			if ((int) o1 > (int) o2)
				return 1;
			else
				return -1;
		}
	};
	static Comparator<String> sc = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			// TODO Auto-generated method stub
			Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
			Matcher m1 = p.matcher(o1);
			Matcher m2 = p.matcher(o2);
			int n1 = 0;
			int n2 = 0;
			if (m1.find() && m2.find()) {
				n1 = Integer.parseInt(m1.group(0));
				n2 = Integer.parseInt(m2.group(0));
			}
			if (n1 > n2)
				return 1;
			else
				return -1;
		}
	};

	public static String process(String path) {
		String reg = ".*\\\\(.*)";
		String s = path.replaceAll(reg, "$1");
		return s;

	}

	public DirFilter() {
		badstu = new ArrayList<String>();
	}

	public void TotalCount(File fs) {
		File[] tmplist = fs.listFiles();
		System.out.println("学生总数" + tmplist.length);
		for (int n = 0; n < tmplist.length; n++) {// 遍历文件夹中每个学生文档
			if (tmplist[n].isDirectory()) {
				File[] filelist = tmplist[n].listFiles();
				if (filelist.length == 0) {// 判断文档中是否有作业
					String name = process(tmplist[n].getAbsolutePath());

					badstu.add(name);
				} else {
					// 文档中包含周计划或周总结
				}
			} else {
				System.out.println(tmplist[n].getName());
			}
		}
		badstu.sort(sc);
		System.out.println("共有" + badstu.size() + "位同学未提交周任务");
		for (String stuname : badstu) {
			System.out.println(stuname);
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 初始化

	}

}
