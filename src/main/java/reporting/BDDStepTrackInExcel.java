/**
 * 
 */
package reporting;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import Common.Property;


/**
 * Create BDD Step in Excel Format.
 *
 */
public class BDDStepTrackInExcel implements IBddStepFormation {

	private XSSFWorkbook wb = new XSSFWorkbook();
	private XSSFSheet sheet;
	private String Workbook = "";
	private static int rowNumber = 0;
	private FileOutputStream out = null;

/*********************************************************************************************
Class Name: 			createFile
Description:			Create File for BDD log sheet	
						Property.BDDFile_Location is used from Property.java file
*********************************************************************************************/
	
	public void createFile(String Filename){
		
		Workbook = Property.BDDFile_Location + Filename;
		CreateWorkBook(Workbook);
		//CreateSheet();
	}
	
/*********************************************************************************************
Class Name: 			CreateHeader		
Description:			Create Header in BDD log sheet								
*********************************************************************************************/	
	
	public void CreateHeader(){
		// Create a row and put some cells in it. Rows are 0 based.
	    XSSFRow row = sheet.createRow(rowNumber);
	    rowNumber ++ ;
	    // Create a cell and put a value in it.
	    XSSFCell cellTestCaseID = row.createCell(0);
	    XSSFCell cellTestCaseStep = row.createCell(1);
	    XSSFCell cellBDDStep = row.createCell(2);
	    XSSFCell cellStatus = row.createCell(3);
	    XSSFCell cellRemarks = row.createCell(4);
	    XSSFCell cellExecutionTime = row.createCell(5);
	    XSSFCellStyle style =   wb.createCellStyle();
	    
	    
	    RichTextString TestCaseId = BoldContent("TestCaseID");
	    RichTextString TestStep = BoldContent("TestStep");
	    RichTextString BDDStepName = BoldContent("BDDStepName");
	    RichTextString Status = BoldContent("Status");
	    RichTextString Remarks = BoldContent("Remarks");
	    RichTextString ExecutionTime = BoldContent("ExecutionTime");
	    
	    
	    
	    cellTestCaseID.setCellValue(TestCaseId);
	    
	    cellTestCaseStep.setCellValue(TestStep);
	    cellBDDStep.setCellValue(BDDStepName);
	    cellStatus.setCellValue(Status);
	    cellRemarks.setCellValue(Remarks);
	    cellExecutionTime.setCellValue(ExecutionTime);
	    //SaveWorkBook();
	    }
	
/*********************************************************************************************
Class Name: 			BoldContent		
Description:			Set font and color for headers									
*********************************************************************************************/	
	
	private RichTextString BoldContent(String content){
		XSSFFont font = wb.createFont();
		font.setBoldweight(font.BOLDWEIGHT_BOLD);
		
		RichTextString string = new XSSFRichTextString(content);
		string.applyFont(font);
		return string;
	}

/*********************************************************************************************
Class Name: 			setGreen		
Description:			Set font and color for a failed step									
*********************************************************************************************/		
	private RichTextString setGreen(String content){
		XSSFFont font = wb.createFont();
		
		font.setColor(HSSFColor.DARK_GREEN.index);
		RichTextString string = new XSSFRichTextString(content);
		string.applyFont(font);
		return string;
		
	}
	
/*********************************************************************************************
Class Name: 			setRed		
Description:			Set font and color for a failed step									
*********************************************************************************************/		
	private RichTextString setRed(String content){
		XSSFFont font = wb.createFont();
		
		font.setColor(HSSFColor.DARK_RED.index);
		RichTextString string = new XSSFRichTextString(content);
		string.applyFont(font);
		return string;
		
	}	
	
/*********************************************************************************************
Class Name: 			CreateContentRow		
Description:			Create a cell in Excel and put a value in it										
*********************************************************************************************/	
	
	public void CreateContentRow(String TestCaseID,String TestStep,String BDDStep,String Status,String Remarks,String ExecutionTime,boolean ISWrite)
	{
		if(ISWrite)
		{
			XSSFRow row = sheet.createRow(rowNumber);
			rowNumber ++ ;
			
			RichTextString status = null;
			if(Status.toLowerCase().equals(Property.PASS)) {
				status = setGreen(Status);
			}
			else if(Status.toLowerCase().equals(Property.FAIL)) {
				status = setRed(Status);
			}
			
			// Create a cell and put a value in it.
			XSSFCell cellTestCaseID = row.createCell(0);
			XSSFCell cellTestCaseStep = row.createCell(1);
			XSSFCell cellBDDStep = row.createCell(2);
			XSSFCell cellStatus = row.createCell(3);
			XSSFCell cellRemarks = row.createCell(4);
			XSSFCell cellExecutionTime = row.createCell(5);
			cellTestCaseID.setCellValue(TestCaseID);
			cellTestCaseStep.setCellValue(TestStep);
			cellBDDStep.setCellValue(BDDStep);
			cellStatus.setCellValue(status);
			cellRemarks.setCellValue(Remarks);
			cellExecutionTime.setCellValue(ExecutionTime);
	    }
	    
		
	}
	
/*********************************************************************************************
Class Name: 			CreateWorkBook		
Description:			Create Workbook In BDD log file
*********************************************************************************************/		
	private void CreateWorkBook(String WorkbookName){
		try {	
			
			//OPCPackage fileSystems = OPCPackage.open(file.getAbsolutePath(),PackageAccess.READ);
			sheet = wb.createSheet("Steps");
			out = new FileOutputStream(WorkbookName);
			//CreateHeader();
			
			//wb.write(out);
			//out.close();
			} 
		catch (Exception e) {
			
		}
	}
	/*********************************************************************************************
Class Name: 			SaveFile		
Description:			Save BDD log file
*********************************************************************************************/	

	public void SaveFile(){
		try{
		//FileOutputStream out = new FileOutputStream(Workbook);
		wb.write(out);
		//out.close();
		}
		catch(Exception e){
			
		}
	}
	
}
