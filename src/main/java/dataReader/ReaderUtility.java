package dataReader;

import java.io.File;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;

import Common.Property;

/**
 * All Utility functions that assist in communicating with external files like TestCase sheet, DataData sheet and Object Repository sheet etc are listed here.
 * @author
 *
 */
public class ReaderUtility {
	
	private String TestCaseFileLocation;
	
	private String TestDataFileLocation;
	
	private String ObjectRepositoryFileLocation;
	
	private String CurrentTestSuiteID;
	
	
	/**
	 * Constructor method that assigns current TestSuite name located in the uiautomation.properties file.
	 * This is the name of the file available under TestCase dir.
	 */
	public ReaderUtility(){
		CurrentTestSuiteID = Property.TestSuite;
	}
	
	
	/**
	 * Set the location of all the external files (TestCase, TestData & ObjectRepository) needed for the framework.
	 * @param reusableFlag a boolean value used to locate the file when reusable scenarios are in execution.
	 * Property.FileSeperator and Property.ObjectRepositoryFileLocation values are used from Property.java file
	 */
	public void setFileLocation(boolean reusableFlag){
		
		TestCaseFileLocation = Property.TESTCASE_LOC  + CurrentTestSuiteID + Property.FILE_EXTENSION;
		
		// Check whether reusableFlag is true. If yes, use 'GlobalActionFile.xls' file available under TestCase dir.
		// In this case 'TestCaseFileLocation' will NOT point to a particular test suite.
		if(reusableFlag){			
			TestCaseFileLocation = Property.TESTCASE_LOC + Property.REUSABLE_FILE_NAME + Property.FILE_EXTENSION;
		}
		
		// TestData and TestCase info are in same sheet so their location is same.
		TestDataFileLocation = TestCaseFileLocation;	
		
		// 'ObjectRepositoryFileLocation' pointing to src/main/resources/ObjectRepository/ObjectRepository.xls
		ObjectRepositoryFileLocation = Property.ObjectRepositoryFileLocation;
	}
	
	/**
	 * Helper function that takes a file (excel) and name of sheet in that file
	 * and Returns a table representing data from given sheet in tabular format.
	 * 
	 */
	
	public ITable getRequiredRows(String FilePath,String Sheet) throws Exception{
		
		ITable objSheet = null; // ITable: An interface that represents a collection of tabular data.
		try {
			File inFile = new File(FilePath);
			XlsDataSet ds = new XlsDataSet(inFile);
			
			/* XlsDataSet : This dataset implementation can read and write MS Excel documents. Each
			 * sheet represents a table. The first row of a sheet defines the columns names
			 * and remaining rows contains the data.
			 */
			objSheet = ds.getTable(Sheet);
			
		} 
		catch(DataSetException de){
			throw new DataSetException("No such sheet,please check the file again.");
		}
		catch (Exception e) {
			throw e;
		}
		return objSheet;
	}

	
	/**
	 * Get the data from Object Repository, implicitly calls 'getRequiredRows()'
	 * @return <b>ResultSet object of Data.</b>
	 * @throws Exception
	 */
	public ITable getORData() throws Exception{
		try{
		ITable objTb = getRequiredRows(ObjectRepositoryFileLocation, Property.ObjectRepositorySheet);
		
		return objTb;
		}
		catch(Exception e){
			throw e;
		}
		
	}
	
	
	/**
	 * Get the data from Test case file.
	 * @return <b>ResultSet object of the data </b>
	 * @throws Exception
	 */
	public ITable getTestCaseData() throws Exception{
		try{
			ITable rs = getRequiredRows(TestCaseFileLocation,Property.TestCaseSheet);
			
			return rs;
					
			
		}
		catch(Exception e){
			throw e;
		}
	}

	
	/**
	 * Get the data from Test data file.
	 * @return <b>ResultSet object of data.</b>
	 * @throws Exception
	 */
	public ITable getTestData() throws Exception{
		try{
			ITable rs = getRequiredRows(TestDataFileLocation,Property.TestDataSheet);
			
			return rs;
			
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
	

}
