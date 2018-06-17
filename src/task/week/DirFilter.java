package task.week;

import java.io.File;
import java.util.ArrayList;

import task.Tools;

public class DirFilter {
	// private static ArrayList<Integer> tmp;
	private ArrayList<String> badstu;
	private Tools t;
	// private static ArrayList<String> hardstu;

	

	public String process(String path) {//从文件路径中提取学生姓名
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
		badstu.sort(t.sc);
		System.out.println("共有" + badstu.size() + "位同学未提交周任务");
		for (String stuname : badstu) {
			System.out.println(stuname);
		}

	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		// 初始化
//
//	}

}
