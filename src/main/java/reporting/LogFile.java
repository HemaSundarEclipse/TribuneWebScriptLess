package reporting;
import java.io.File;
import org.apache.log4j.*;


public class LogFile {

  private static Logger log = Logger.getLogger("LOG");
       
  
/*********************************************************************************************
Step action name: 		prepareLogger	
Description:			Prepare a logger with a new file name in the Execution_Log folder										
*********************************************************************************************/	  
  
  //Prepare a logger with a new file name.
  public void prepareLogger(String FilePath){
	  try{
		  log.setLevel(Level.INFO);
		  File myLogFile = new File(FilePath);
		  String path = myLogFile.getAbsolutePath();
		  if(!myLogFile.exists()){
			  path.getBytes();
			  //myLogFile.mkdir();
			  boolean isCreated = myLogFile.createNewFile();
			  System.out.println(isCreated);
		  }
		  
		  Layout layout = new PatternLayout("...");
		  FileAppender appender  = new FileAppender(new PatternLayout(), myLogFile.getAbsolutePath(),true);
		  log.removeAllAppenders();
		  log.addAppender(appender);
	  }
	  catch(Exception e){
		  System.out.println(e.getMessage());
	  }
  }
  
/*********************************************************************************************
Step action name: 		prepareHeader	
Description:			Create header for logging											
*********************************************************************************************/		
  
  public void prepareHeader()
  {
	log.info("TestCaseID" + "\t\t\t\t\t\t\t" + "TestStep" + "\t\t\t\t\t\t\t" + "BDD_Step" + "\t\t\t\t\t\t\t" + "Status" + "\t\t\t\t\t\t\t" + "Remarks" + "\n");  
  }
  
/*********************************************************************************************
Step action name: 		writeStepLog	
Description:			Log Teststep info and result									
*********************************************************************************************/		  
  
  public void writeStepLog(String TestCaseID,String TestStep,String Status,String Remarks,String BDDStep,boolean flag){
	//  if (flag) {log.info(TestCaseID + "\t\t\t\t\t\t\t" + TestStep + "\t\t\t\t\t\t\t" + BDDStep + "\t\t\t\t\t\t\t" +  Status + "\t\t\t\t\t\t\t"+ Remarks + "\n");}
	  if (flag) {log.info(TestCaseID + "\t\t" + TestStep + "\t\t" + BDDStep + "\t\t" +  Status + "\t\t"+ Remarks + "\n");}

  }
  
 /*********************************************************************************************
Step action name: 		logMessageConsole	
Description:			Log messages									
*********************************************************************************************/		 

  public void logMessageConsole(String Message){
		System.out.println(Message);
	
	}
}
