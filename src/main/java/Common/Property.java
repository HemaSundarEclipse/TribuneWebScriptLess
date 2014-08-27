package Common;

import java.util.HashMap;

/**
 * @author Property : All static data content in framework.
 * 
 */
public class Property {

	// Get the file separator as they may vary from environment to environment.
	public static String FileSeperator = System.getProperty("file.separator");

	// Name of sheet that has details about locators / Object Repository.
	public static String ObjectRepositorySheet = "object_definition";

	// Name of sheet that details about test cases / test steps / test data.
	public static String TestCaseSheet = "test_flow";

	// Name of Test Data sheet
	public static String TestDataSheet = "test_data";

	public static String BDDFileQuery = "select * from [Steps$]";

	// Dynamic JDBC Connection strings declarations.
	public static String JDBCConnectionStringObjectRepository = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ={0};READONLY=TRUE";

	public static String JDBCConnectionStringTestCase = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ="
			+ "{0}" + ";READONLY=TRUE";

	public static String JDBCConnectionStringTestData = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ={0};READONLY=TRUE";

	public static String JDBCConnectionStringBDDFile = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ={0};READONLY=TRUE";

	public static String IsSauceLabExecution = "true";

	public static String IsRemoteExecution = "true";

	public static String RemoteURL = "";

	public static HashMap<String, String> paramMap = new HashMap<String, String>();

	// Name and location of Property file location (uiautomation.properties)
	public static String PropertyFileLocation = "src" + FileSeperator + "main"
			+ FileSeperator + "resources" + FileSeperator
			+ "uiautomation.properties";

	// Name and location of Object Repository. 
	public static String ObjectRepositoryFileLocation = "src" + FileSeperator
			+ "main" + FileSeperator + "resources" + FileSeperator
			+ "ObjectRepository" + FileSeperator + "ObjectRepository.xls";

	public static String BrowserName = "firefox";

	public static String TestSuite = "";

	// HashMap that will have data from uiautomation.properties. It will be populated through Utility.populateGlobalMap() method.	
	public static HashMap<String, String> globalVarMap = new HashMap<String, String>();

	public static String REUSABLE_INVOKE_KEYWORD = "runreusabletestcase";

	public static String REUSABLE_FILE_NAME = "GlobalActionFile";

	public static String FILE_EXTENSION = ".xls";

	// Location of test cases. Will be used in setFileLocation() method of ReaderUtility.java
	public static String TESTCASE_LOC = "src" + FileSeperator + "main"
			+ FileSeperator + "resources" + FileSeperator + "TestCase"
			+ FileSeperator;

	public static String Remarks = "";

	public static final String PASS = "pass";

	public static final String FAIL = "fail";

	public static String StepStatus = "";

	public static String ApplicationURL = "";

	public static String SEPERATOR = "#";

	public static String StepDescription = "";

	public static String SyncTimeOut = "";

	public static String LogFile = "src" + FileSeperator + "main"
			+ FileSeperator + "resources" + FileSeperator + "Execution_Log"
			+ FileSeperator + "{0}";

	public static String BDD_StepName = "";

	public static String BDD_FileFormat = "";

	public static String BDDFile_Location = "src" + FileSeperator + "main"
			+ FileSeperator + "resources" + FileSeperator + "BDDStepRecord"
			+ FileSeperator;

	public static String BDD_FileName = "";

	public static String BDD_FileExtension = ".xlsx";

	public static String ExecutionStartTime = "";

	public static String ExecutionEndTime = "";

	public static String OSString = "";

	public static String StepExecutionTime = "";

	public static String KeyIgnoreStep = "ignorestep";
	
	public static String dbDB2Driver = "com.ibm.db2.jcc.DB2Driver";
	public static String dbPostgresDriver = "org.postgresql.Driver";
	public static String dbOracleDriver = "";
	public static String dbSQLDriver = "";
	
	
	public static String dbConnString = "";
	public static String dbName = "";
	public static String dbUsername = "";
	public static String dbPassword = "";

	public static String dbQuery = "";

}
