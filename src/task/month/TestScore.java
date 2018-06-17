package task.month;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import task.Tools;

public class TestScore {
	private Map<String, Float> stuScore;// 成绩单

	private Tools t;

	public TestScore() {//初始化构造函数
		stuScore = new HashMap<String, Float>();

		t = new Tools();
	}

	public void TotalCount(File fs) {
		File[] tmplist = fs.listFiles();
		for (int n = 0; n < tmplist.length; n++) {// 遍历文件夹中每个学生文档
			if (!tmplist[n].isDirectory()) {
				Workbook mywb = t.getWorkbook(tmplist[n].getAbsolutePath());// 处理文件、迭代分数
				ExcelCount(mywb);
			} else {
				System.out.println(tmplist[n].getName());
			}
		}
	}

	private void ExcelCount(Workbook workbook) {
		// for (int in = 0; in < workbook.getNumberOfSheets(); in++) {
		if (workbook.getNumberOfSheets() != 1) {
			System.out.println("工作表排版有误，请检查表格排版！");
			System.exit(0);
		}

		Sheet eSheet = workbook.getSheetAt(0);
		Row inRow = eSheet.getRow(0);
		int nameCell = -1;
		ArrayList<Integer> scoreCell = new ArrayList<Integer>();
		int[] check = { 0, 0 };// 用来判断表格排版中是否包含“姓名”和“成绩”。其中“成绩”可以不止一列
		if (inRow != null) {
			// System.out.println("PhysicalNumberOfCells:" +
			// inRow.getPhysicalNumberOfCells());
			// System.out.println("PhysicalNumberOfRows:" +
			// eSheet.getPhysicalNumberOfRows());
			
			//获取哪一列是姓名列表、哪一列是成绩列表
			for (int cellsNum = 0; cellsNum < inRow.getPhysicalNumberOfCells(); cellsNum++) {
				if (inRow.getCell(cellsNum) != null) {

					if (inRow.getCell(cellsNum).toString().contains("姓名") && check[0] == 0) {
						nameCell = cellsNum;
						check[0]--;
					}
					if (inRow.getCell(cellsNum).toString().contains("成绩") && check[1] <= 0) {
						scoreCell.add(cellsNum);
						check[1]--;
					}

				}
			}
			int result = check[0] + check[1];
			if (result >= -1 || nameCell == -1) {
				System.out.println("表格标头排版有误，请检查表格排版！");
				System.exit(0);
			}
			for (int rowNum = 1; rowNum < eSheet.getPhysicalNumberOfRows(); rowNum++) {// 获取成绩
				Row eRow = eSheet.getRow(rowNum);
				if (eRow != null) {
					if (eRow.getCell(nameCell) != null && eRow.getCell(nameCell).toString() != "") {
						String name = eRow.getCell(nameCell).toString();
						float score = 0;
						for (int i : scoreCell) {// 测试成绩有可能不止一列
							if (eRow.getCell(i) != null && eRow.getCell(i).toString() != "") {// 合计分数

								score += Float.parseFloat(eRow.getCell(i).toString());
							}
						}
						if (!stuScore.containsKey(name)) {
							stuScore.put(name, score);
						} else {
							float tmp = stuScore.get(name) + score;
							stuScore.put(name, tmp);
						}
					}
				}

			}

		} else {
			System.out.println("inRow初始行出错！");
		}
		// }
	}

	private float getMax() {//获取最大值
		float max = 0;
		for (Map.Entry<String, Float> me : stuScore.entrySet()) {
			if (max < me.getValue()) {
				max = me.getValue();
			}
		}
		return max;
	}

	public Map<String, Integer> getScoreResult() {// 降序排序；标准化
		Map<String, Integer> stuSR = new HashMap<String, Integer>();
		float max = getMax();
		for (Map.Entry<String, Float> me : stuScore.entrySet()) {
			if (me.getValue() == 0) {
				continue;
			} else {
				int score = (int) (me.getValue() / max * 10);
				stuSR.put(me.getKey(), score);
			}
		}
		return stuSR;

	}

	public void print() {// 输出成绩排名
		System.out.println("测试成绩得分：");
		for (Map.Entry<String, Integer> me : getScoreResult().entrySet()) {
			System.out.println(me.getKey() + "\t" + me.getValue());
		}
		System.out.println(stuScore.size() + "人");
	}

//	public static void main(String[] args) {
//		for (int i = 0; i < args.length; i++) {
//			File filePath = new File(args[i]);
//			if (filePath.isDirectory()) {
//				TestScore ts = new TestScore();
//				ts.TotalCount(filePath);
//				ts.print();
//			}
//		}
//	}
}
