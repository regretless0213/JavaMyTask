package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import jxl.Sheet;  
import jxl.Workbook;  
import jxl.read.biff.BiffException; 

public class ExcelProcess_xls {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelProcess_xls obj = new ExcelProcess_xls();  
	        // �˴�Ϊ�Ҵ���Excel·����E:/zhanhj/studysrc/jxl��  
	        File file = new File(args[0]);  
	        obj.readExcel(file);  
	    }  
	    // ȥ��Excel�ķ���readExcel���÷�������ڲ���Ϊһ��File����  
	    public void readExcel(File file) {  
	        try {  
	            // ��������������ȡExcel  
	            InputStream is = new FileInputStream(file.getAbsolutePath());  
	            // jxl�ṩ��Workbook��  
	            Workbook wb = Workbook.getWorkbook(is);  
	            // Excel��ҳǩ����  
	            int sheet_size = wb.getNumberOfSheets();  
	            for (int index = 0; index < sheet_size; index++) {  
	                // ÿ��ҳǩ����һ��Sheet����  
	                Sheet sheet = wb.getSheet(index);  
	                // sheet.getRows()���ظ�ҳ��������  
	                for (int i = 1; i < sheet.getRows(); i++) {  
	                    // sheet.getColumns()���ظ�ҳ��������  
	                    for (int j = 2; j < sheet.getColumns(); j++) {  
	                        String cellinfo = sheet.getCell(j, i).getContents();  
	                        System.out.println(cellinfo);  
	                    }  
	                }  
	            }  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (BiffException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}

}
