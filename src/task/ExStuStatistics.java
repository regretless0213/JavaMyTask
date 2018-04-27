package task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExStuStatistics {
	private Map<String, Integer> stuCredit;//统计总分

	public ExStuStatistics() {//初始化构造函数
		stuCredit = new HashMap<String, Integer>();
	}

	public void TotalCount(File fs) throws Exception {
		File[] tmplist = fs.listFiles();
		System.out.println("学生总数" + tmplist.length);
		for (int n = 0; n < tmplist.length; n++) {// 遍历文件夹中每个学生文档
			if (!tmplist[n].isDirectory()) {
				Workbook mywb = getWorkbook(tmplist[n].getAbsolutePath());// 处理文件、迭代分数
				ExcelCount(mywb);
			} else {
				System.out.println(tmplist[n].getName());
			}
		}
	}

	public Workbook getWorkbook(String path) throws Exception {//判断Excel表格类型
		Workbook wbtmp = null;
		InputStream is = new FileInputStream(path);
		String excelType = path.split("\\.")[1];// 区分文件类型
		if (excelType.equals("xlsx")) {
			wbtmp = new XSSFWorkbook(is);
		} else if (excelType.equals("xls")) {
			wbtmp = new HSSFWorkbook(is);
		} else {
			System.out.println("输入文件格式错误！");
		}
		return wbtmp;

	}

	public void ExcelCount(Workbook workbook) {//单个文件处理
		Sheet eSheet = workbook.getSheetAt(0);// 单词签到页

		for (int rowNum = 1; rowNum <= eSheet.getLastRowNum(); rowNum++) {
			Row eRow = eSheet.getRow(rowNum);
			String name = rowNum + eRow.getCell(1).toString();// 限定学生姓名格式
			if (!stuCredit.containsKey(name)) {
				if (eRow.getCell(2) == null || !eRow.getCell(2).toString().contentEquals("转班")) {
					stuCredit.put(name, 0);
				}
			}
			if (eRow != null) {
				for (int cellsNum = 2; cellsNum < 9; cellsNum++) {
					// System.out.println(xssfRow.getCell(cellsNum));
					int rindex = cellsNum - 2;// 索引位置
					int minsize = rindex + 1;// 从1开始的横坐标位置

					
					if (eRow.getCell(cellsNum) != null){//switch函数？
						if (eRow.getCell(cellsNum).toString().contentEquals("转班")) {

						} else if (eRow.getCell(cellsNum).toString().contentEquals("加入班级")) {

						} else {
						}
					}
				}
			}
		}
		
		
		
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < args.length; i++) {
			File filePath = new File(args[i]);
			if (filePath.isDirectory()) {
				ExStuStatistics ess = new ExStuStatistics();
				ess.TotalCount(filePath);
			}
		}
	}

}
