package com.tribune.uiautomation.testscripts;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


import junit.framework.Assert;

import org.dbunit.dataset.ITable;

import org.junit.Before;
import org.junit.Test;

import reporting.BddStepCreationControler;
import reporting.LogFile;
import reporting.ReportCreation;
import testDriver.Actions;
import Common.Property;
import Common.Utility;
import dataReader.ReaderUtility;


/**
 * This class has JUnit methods like @Before, @Test. When we run our project as jUnit test, then this
 * is the class that gets executed first.
 *     
 */
public class TestEngine {
	
	Boolean IsAnyStepFailed = false;
	ITable TestORSet = null;
	public Actions objAction = null;
	private ReaderUtility objReader = null;
    public String StepAction;
    public LogFile objLog = new LogFile(); // An instance of LogFile to create a log file for every test suite run (Ex. SearchTestSuite_Wed Apr 16 140047 IST 2014.txt)
    public static ArrayList<String> TestCaseIDsForExecution = new ArrayList<String>(); // List to contain TestCase Ids that will be executed.
    boolean IsWriteStep = false; 
    BddStepCreationControler objController;
    
	/**
	 * @Before Method that is run by JUnit before @Test method.
	 * Populates Global hash map with the Key/Value given in uiautomation.properties file and initializes different static variables defined in Property.java like:
	 * OSString, TestSuite, BrowserName, SyncTimeOut, IsRemoteExecution, IsSauceLabExecution, ApplicationURL, RemoteURL, BDD_FileFormat, BDD_FileName
	 * Also initializes variables required for BDDType reporting 
	 */
    @Before
	public void beforeTest(){
		try{	
			
		Utility.collectKeyValuePair();
		Utility.populateGlobalMap();
		
		Properties prop = System.getProperties();
		Utility.addExternalProperties(prop);
		
		String mavenString =  System.getProperty("sun.java.command");
		System.out.println(mavenString);
		Utility.fetchAndSaveExternalParameters(mavenString);

		IsWriteStep = true;
		
		
		Property.OSString = System.getProperty("os.name") + " " + System.getProperty("os.version");
		Property.TestSuite = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("TestSuite"));
		Property.BrowserName = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("browserType"));
		Property.SyncTimeOut = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("MaximumTimeout"));
		Property.IsRemoteExecution = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("RemoteExecution"));
		Property.IsSauceLabExecution = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("SaucelabExecution"));
		Property.ApplicationURL = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("application.url"));
		Property.RemoteURL = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("RemoteUrl"));
		Property.BDD_FileFormat = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("BDDFileFormat"));
		Property.BDD_FileName = Utility.replaceVariableInString(Utility.getValueFromGlobalVarMap("BDDFileName"));
		//Update key value pair passed as an arguments(update paramMAp).
		

		Property.dbConnString = Utility.getValueFromGlobalVarMap("db.conn.string");
		Property.dbName = Utility.getValueFromGlobalVarMap("db.name");
		Property.dbUsername = Utility.getValueFromGlobalVarMap("db.username");
		Property.dbPassword = Utility.getValueFromGlobalVarMap("db.password");
		
		
				
		//Set All PRoperty variable using setParameter();
		
		//Use dataReader class to get the data for the framework.
		
		//Getting a controller object to record BDD Step.
		
		objController = new BddStepCreationControler(Property.BDD_FileFormat);
		
		objController.buildStepCreationObject();
		
		//Preparing the name of the file for BDD step recording.
		
		//String BDDFileName = Property.TestSuite + "_" +  Utility.getCurrentTimeStamp();
		
		
		//Creating a File
		BddStepCreationControler.objStepFormation.createFile(Property.BDD_FileName + Property.BDD_FileExtension);
		//Create a header.
		BddStepCreationControler.objStepFormation.CreateHeader();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

    /**
     * Fetches OR data based on 'parent' and 'testobject' 
     * @param parent refers to 'page'
     * @param testObject refers to logical name of 'locator'
     * @return HashMap containing parent, testobject, how, what related to locator info specified in a row in OR sheet.
     * @throws Exception
     */
    public HashMap<String, String> getORData(String parent,String testObject) throws Exception{
		HashMap<String, String> objDef = new HashMap<String, String>();
		try{
			int rowCount = TestORSet.getRowCount();
			int iterativeRow = 0;
		while(iterativeRow < rowCount){
			if(TestORSet.getValue(iterativeRow, "parent").toString().equals(parent) && TestORSet.getValue(iterativeRow, "testObject").toString().equals(testObject)){
				String how = TestORSet.getValue(iterativeRow, "how").toString();
				String what = TestORSet.getValue(iterativeRow, "what").toString();
				how  = Utility.replaceVariableInString(how);
				what = Utility.replaceVariableInString(what);
				
				objDef.put("parent", parent);
				objDef.put("testObject", testObject);
				objDef.put("how", how);
				objDef.put("what", what);
				
				break;
				
			}
			iterativeRow++ ;
		}
		
		return objDef;
		
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
	
	/**
	 * Executes given testcaseid. Once it finds the matching TestCaseID in test case sheet, it executes all
	 *  the steps defined in that case in the testcase sheet.
	 * @param TestCaseID Text Value of TestCaseID that needs to be executed.
	 * @param reusableFlag boolean value that determines which file to be used for reading testcase and testdata info.
	 * @throws Exception
	 */
	public void ExecuteTestCase(String TestCaseID,boolean reusableFlag) throws Exception{
		try{
		 //objReader = new ReaderUtility(TestCaseID);
		objReader.setFileLocation(reusableFlag);
		String TestCaseLog = "";
		if(!reusableFlag) { TestCaseLog = TestCaseID;}
		
		// Get OR Data, TestCase Data and Test Data in tabular format 
		TestORSet  = objReader.getORData();
		ITable TestCaseSet = objReader.getTestCaseData();
		ITable TestDataSet = objReader.getTestData();
		
		objAction = null;
		Boolean IsTestidFound = false;
		int contentRow = 0;
		int testCaseRowCount = TestCaseSet.getRowCount();
		
		while(contentRow < testCaseRowCount){
			String testid = "";
			try{
				testid = TestCaseSet.getValue(contentRow, "testcase_id").toString();}
			catch (NullPointerException ne) {
				testid = "";
			}
			//if(testid == null){testid = "";}
			if(testid.equalsIgnoreCase(TestCaseID)){
				IsTestidFound = true;
			}
			
			if(IsTestidFound){
				// Check if 'testid' is not blank but it is not equal to given 'TestCaseID' 			
				if(!testid.equals("") && !testid.equals(TestCaseID)){
					IsTestidFound = false;
					
					break;
				}
				else{
					Property.StepStatus = "";
					String BDD_Step = "";
					String parent ="" ;
					String testObject = "";
					String DataContent = "";
					String StepAction = "" ;
					String options = "";
					// Read values of parent, testobject, data, stepaction, options and step from the row where we have found presence of given 'TestCaseID'
					try{parent = TestCaseSet.getValue(contentRow, "parent").toString();}catch(NullPointerException ne){}
					try{testObject = TestCaseSet.getValue(contentRow, "testObject").toString();}catch(NullPointerException ne){}
					try{DataContent = TestCaseSet.getValue(contentRow, "data").toString();}catch(NullPointerException ne){}
					try{StepAction = TestCaseSet.getValue(contentRow, "stepaction").toString();}catch(NullPointerException ne){}
					try{options = TestCaseSet.getValue(contentRow, "options").toString();}catch(NullPointerException ne){}
				    try{BDD_Step = TestCaseSet.getValue(contentRow, "step").toString();}catch(NullPointerException ne){	}
					
				    DataContent = Utility.replaceVariableInString(DataContent);
					parent = Utility.replaceVariableInString(parent);
					testObject = Utility.replaceVariableInString(testObject);
					options = Utility.replaceVariableInString(options);
					
					System.out.println("Page : " + parent);
						
					int rownum = 0;//Will use row num logic later.

// Gaurav commented block
/*						if(DataContent == null || DataContent == ""){ // Like in case of 'openbrowser, click on a button etc
							try
							{
								//while(TestDataSet.next()){
								//	System.out.println(TestDataSet.getString("edtSearch"));
								//}
								//TestDataSet.findColumn(testObject);
								//TestDataSet.absolute(rownum);
								
								DataContent =  TestDataSet.getValue(rownum, testObject).toString(); // Fetch the test data from 'test_data' sheet
							    Utility.setKeyValueInGlobalVarMap(testObject, DataContent); // Putting testObject and DataContent in global hash map to use it across the f/w
							}
							catch(NullPointerException e){
								DataContent = null;
							}
							catch(SQLException e){
								DataContent = null;
								}
							catch(NoSuchColumnException ce){
								DataContent = null;
							}
						
						
						}*/
						
					    
						
						//TODO : Will add code to parse data.
						
						Actions.objDataRow = getORData(parent, testObject);
						
						objAction = new Actions();
						BDD_Step = Utility.replaceVariableInString(BDD_Step);
						//if(!reusableFlag){
						//ReportCreation.WriteLogMessage("Stepction : '" + StepAction + "' \n");
						//ReportCreation.WriteLogMessage("Executed on '" + parent + "' :: '" + testObject + "' \n");
						//}
						
						
					   						
					    try{
					    	
					    	objAction.DO(StepAction,DataContent,parent,testObject,options);
					    	
					    }
					    catch(Exception e){
					    	  if(e.getMessage().equalsIgnoreCase("No Such Action")){
					    		
					    		  		//If datacontent would have contained {$var} then it should have replaced so checking for arguments only.		    		  
					    		 if(DataContent != null){
					    		  if(DataContent.contains("{") && DataContent.contains("}")){  
					    		   String[] arguments = DataContent.split(",");
					    		   for(int i=0;i<= arguments.length-1;i++){
					    				arguments[i] = arguments[i].replace("{", "");
					    				arguments[i] = arguments[i].replace("}", "");
					    			   Utility.setKeyValueInGlobalVarMap("argument"+i, arguments[i]);
					    			  }
					    			 				    		  
					    		}
					    	  }
					    		  ExecuteTestCase(StepAction, true);
					    		  
					    		  //Need to loook if arguments need to be deleted.
					    		  IsWriteStep = false;
					    		  
					    	  }
					    }
					    //ReportCreation.WriteLogMessage("Remarks : " + Property.Remarks);
					    //ReportCreation.WriteLogMessage("Status" + Property.StepStatus);
					    //objLog.logMessageConsole("Remarks : " + Property.Remarks);
					    //objLog.logMessageConsole("Status" + Property.StepStatus);
					    if(Property.StepStatus == Property.FAIL) { IsAnyStepFailed = true;}
					    objLog.writeStepLog(TestCaseID, StepAction, Property.StepStatus,Property.Remarks,BDD_Step,IsWriteStep);
					    BddStepCreationControler.objStepFormation.CreateContentRow(TestCaseLog, StepAction, BDD_Step, Property.StepStatus,Property.Remarks,Property.StepExecutionTime,IsWriteStep);
					    IsWriteStep = true;
					    TestCaseLog = "";
					    
				}
			}
			
			contentRow++;
			//System.out.println("Status of '" + StepAction +"' =" + Property.StepStatus);
			//System.out.println("Remarks : " + Property.Remarks);
			//Assert.assertEquals(Property.StepStatus, Property.PASS);
	}
		
		
		
		
		}
		catch(NullPointerException e){
		    throw e;
			//throw new NullPointerException("Execution has unexpectedly broken.");
		}
		catch(Exception e){
			throw e;
		}
		
		
	}

	

	/**
	 * Executes all test cases or list of test cases specified by user.
	 * @throws Exception
	 */
	@Test
	public void Execution() throws Exception {
		
		try{
		String TestSuite = Utility.getValueFromGlobalVarMap("TestSuite");
		String TestCaseID = Utility.getValueFromGlobalVarMap("TestCase");
		//String CurrentTestCaseID = TestCaseIDsForExecution.get(i);
		String LogFileName = TestSuite + "_" + Utility.getCurrentTimeStamp() +".txt";
		String LogFile = Property.LogFile.replace("{0}", LogFileName);
		objLog.prepareLogger(LogFile);
		objLog.prepareHeader();
		
		
		objReader = new ReaderUtility();
		objReader.setFileLocation(false);
		 
		// If TestSuite is not null and user has not specified anything in TestCase in uiautomation.properties then read all rows of TestCase sheet and add
		// all Test Case Ids to list 'TestCaseIDsForExecution'
		if(!(TestSuite.equals(null)) && (TestCaseID == null || TestCaseID == "") )
		{
			ITable rs = objReader.getTestCaseData();
			int tcRowCount = rs.getRowCount();
			int tcIndex = 0;
			while(tcIndex < tcRowCount){
				
				Object tid = rs.getValue(tcIndex, "testcase_id");
				if(!(tid == null)){
					TestCaseIDsForExecution.add(tid.toString());
					
				}
				tcIndex++;
			}
			
			
		}
		// User has specified comma separated values of TestCaseIDs
		else
		{
			if(TestCaseID.contains(",")){
			String[] TCids = 	TestCaseID.split(",");
			for (String ID : TCids) {
				ID = ID.trim();
				TestCaseIDsForExecution.add(ID);
				}
			}
			// User has specified just one TestCaseID
			else
			{
				TestCaseIDsForExecution.add(TestCaseID);
			}
			
		}
		
		Property.ExecutionStartTime = Common.Utility.getCurrentDateAndTime();
		for(int i=0;i <= (TestCaseIDsForExecution.size()-1);i++) {
			String CurrentTestCaseID = TestCaseIDsForExecution.get(i);
		//	String LogFileName = CurrentTestCaseID + "_" + Utility.getCurrentTimeStamp() +".txt";
		//	String LogFile = Property.LogFile.replace("{0}", LogFileName);
		//	objLog.prepareLogger(LogFile);
			objLog.logMessageConsole("Execution start for " + CurrentTestCaseID);
			//System.out.println("Execution start for " + CurrentTestCaseID);
			
			ExecuteTestCase(CurrentTestCaseID,false);		
			
			//ResultSet BDDSet = objReader.getRequiredRows(Property.BDDFile_Location + Property.BDD_FileName + Property.BDD_FileExtension,Property.JDBCConnectionStringBDDFile, Property.BDDFileQuery);
			try{objAction.DO("closeallbrowsers", "", "", "",null);} catch(Exception e){}
			objLog.logMessageConsole("Execution ends for " + CurrentTestCaseID);
			
		}
		Property.ExecutionEndTime = Common.Utility.getCurrentDateAndTime();
		BddStepCreationControler.objStepFormation.SaveFile();
		}
		catch(Exception e){
			ReportCreation.WriteLogMessage(e.getMessage());
		}
		
		if(IsAnyStepFailed){
			Assert.assertEquals(IsAnyStepFailed, Boolean.FALSE);
		}
		
	}
	
	

}
