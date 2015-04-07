/**************************************
 testDriver.Actions.java : Kind of interface that prepare objects and data before execution goes
 to perform application's operations.
 **************************************/


package testDriver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.WebElement;

import seleniumdriver.SeleniumImplementation;
import Common.Property;
import Common.Utility;
import dataReader.DBReader;

public class Actions {
	SeleniumImplementation objSeleniumDriver = null; 
	public static HashMap<String, String> objDataRow = new HashMap<String, String>();
	public ArrayList<String> optionsList = new ArrayList<String>();
	public Actions(){
		
		objSeleniumDriver = new SeleniumImplementation(null);
		
	}
	
	public void DO(String stepAction,String Data,String parent,String child,String modifier) throws Exception{
		boolean verification = true;
		boolean enable = true;
		String stepStatus = "";
		String TestObject = child;
		Property.Remarks = "";
		
		//----Parse Options field----
		optionsList.clear();
		int stIndex;
		int endIndex;
		if(modifier != null){
			modifier = modifier.toLowerCase().trim();
			for(int v=0;;v++){
				if(modifier.contains("{")){
					stIndex = modifier.indexOf('{');
					modifier = modifier.replaceFirst("\\{", "");
					endIndex = modifier.indexOf('}');
					String KeyVariable = modifier.substring(stIndex, endIndex);
					optionsList.add(KeyVariable);
					modifier = modifier.replaceFirst("\\}", "");
					}
				else {
					break;
				}
			}
			
		}
		Common.Utility.driverOptionList = null;
		Common.Utility.driverOptionList = optionsList;
		
		//----Parse Data field----
		// Multiple values are separated with # in Excel sheet)
		
		String[] DataContents = null;
		String dataContentFirst = Data;
		String dataContentSecond = "";
		String dataContentThird = "";
		if(Data != null){
		DataContents = Data.split(Property.SEPERATOR);
		switch (DataContents.length) {
		case 1:
			dataContentFirst = DataContents[0];
			dataContentFirst = dataContentFirst.trim();
			break;
		case 2:
			dataContentFirst = DataContents[0];
			dataContentSecond = DataContents[1];
			dataContentFirst = dataContentFirst.trim();
			dataContentSecond = dataContentSecond.trim();
			break;
		case 3:
			dataContentFirst = DataContents[0];
			dataContentSecond = DataContents[1];
			dataContentThird = DataContents[2];
			dataContentFirst = dataContentFirst.trim();
			dataContentSecond = dataContentSecond.trim();
			dataContentThird = dataContentThird.trim();

		default:
			break;
		}
		dataContentFirst = Common.Utility.replaceVariableInString(dataContentFirst);
		dataContentSecond = Common.Utility.replaceVariableInString(dataContentSecond);
		dataContentThird = Common.Utility.replaceVariableInString(dataContentThird);
		}
		
		//----Get current Date and Time for logging
		
		Property.StepExecutionTime = Common.Utility.getCurrentDateAndTime();

	/*********************************************************************************************
Step action name: 		Ignore Test Step		
Description:			When option column contains '{IgnoreStep}' the Test Step will be ignored											
Input variables:		None
Content of variable:	NA
Output:					Test step is ignored and will be passed
*********************************************************************************************/			
		
		if(optionsList.contains(Property.KeyIgnoreStep.toLowerCase())){
			Property.Remarks = "(Non-Runnable)This step was marked to be ignored.";
			Property.StepStatus = Property.PASS;
			return;
		}
		
		
		try{
		objSeleniumDriver.setObjectDataRow(objDataRow);

		if(stepAction.toLowerCase().equals("openbrowser")){
		
/*********************************************************************************************
Step action name: 		openBrowser			
Description:			Open a new browser and navigate to URL												
Input variables:		dataContentFirst(Testcase Excel sheet)
						OR
						Property.ApplicationURL (uiautomation.properties file)
Content of variable:	URL
Output:					openBrowser(dataContentFirst)
*********************************************************************************************/		
	
			if(dataContentFirst == null || dataContentFirst == ""){
				dataContentFirst = Property.ApplicationURL;
			}
			//call init method of seleniumImplemention here.
			Property.StepDescription = "Open a new browser and navigate to URL '" + dataContentFirst + "'";
			objSeleniumDriver.openBrowser(dataContentFirst);
			//Maximize  browser window.
			//objSeleniumDriver.ExecuteScript(null, "window.moveTo(0,0); window.resizeTo(screen.width-10,screen.height-10);");
					
		}
		
/*********************************************************************************************
Step action name: 		goback			
Description:			Go one page backward in browser											
Input variables:		None
Content of variable:	NA
Output:					goBack()
*********************************************************************************************/		

		else if(stepAction.equalsIgnoreCase("goback")){
			Property.StepDescription = "Go backward in browser";
			objSeleniumDriver.goBack();
		}
				
/*********************************************************************************************
Step action name: 		goforward			
Description:			Go one page forward in browser											
Input variables:		None
Content of variable:	NA
Output:					goForward()
*********************************************************************************************/		

		else if(stepAction.equalsIgnoreCase("goforward")){
			Property.StepDescription = "Go forward in browser";
			objSeleniumDriver.goForward();
		}
						
/*********************************************************************************************
Step action name: 		navigateurl			
Description:			Navigate to URL in currently opened browser											
Input variables:		dataContentFirst(Testcase Excel sheet)
Content of variable:	URL
Output:					NavigateUrl(dataContentFirst)
*********************************************************************************************/		

		else if(stepAction.equalsIgnoreCase("navigateurl")){
			Property.StepDescription = "Navigate to Url '" + dataContentFirst + "' in currently opened browser";
			objSeleniumDriver.NavigateUrl(dataContentFirst);
		}
						
/*********************************************************************************************
Step action name: 		refresh			
Description:			Refresh currently opened browser											
Input variables:		None
Content of variable:	NA
Output:					refreshBrowser()
*********************************************************************************************/		
		
		else if(stepAction.equalsIgnoreCase("refresh")){
			Property.StepDescription = "Refresh browser";
			objSeleniumDriver.refreshBrowser();
		}

/*********************************************************************************************
Step action name: 		check			
Description:			Check Checkbox GUI item										
Input variables:		TestObject
Content of variable:	Testobject name of Checkbox
Output:					check()
*********************************************************************************************/		
			
		else if(stepAction.equalsIgnoreCase("check")){
			Property.StepDescription = "Check '" + TestObject + "'";
			objSeleniumDriver.check();
		}

/*********************************************************************************************
Step action name: 		uncheck			
Description:			Uncheck Checkbox GUI item										
Input variables:		TestObject
Content of variable:	Testobject name of Checkbox
Output:					uncheck()
*********************************************************************************************/		
				
		else if(stepAction.equalsIgnoreCase("uncheck")){
			Property.StepDescription = "Uncheck '" + TestObject + "'";
			objSeleniumDriver.Uncheck();
		}

/*********************************************************************************************
Step action name: 		closebrowser			
Description:			Close currently opened browser									
Input variables:		None
Content of variable:	NA
Output:					closeBrowser()
*********************************************************************************************/		
					
		else if(stepAction.toLowerCase().equals("closebrowser")){
			Property.StepDescription = "Close Browser";
			objSeleniumDriver.closeBrowser();
		}
		
/*********************************************************************************************
Step action name: 		closeallbrowsers			
Description:			Close all currently opened browsers									
Input variables:		None
Content of variable:	NA
Output:					closeAllBrowsers()
*********************************************************************************************/		
					
		else if(stepAction.equalsIgnoreCase("closeallbrowsers")){
			objSeleniumDriver.closeAllBrowsers();
		}

/*********************************************************************************************
Step action name: 		click			
Description:			Click on GUI item								
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					click()
*********************************************************************************************/		
		
		else if(stepAction.toLowerCase().equals("click")){
			Property.StepDescription = "Click on '" + TestObject + "'";
			objSeleniumDriver.click();
		}
		
		else if(stepAction.toLowerCase().equals("mouseover")){
			Property.StepDescription = "Mouse overing on '" + TestObject + "'";
			objSeleniumDriver.mouseOver();
		}
/*********************************************************************************************
Step action name: 		doubleclick			
Description:			Doubleclick on GUI item								
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					doubleclick()
*********************************************************************************************/	
		
		else if(stepAction.toLowerCase().equals("doubleclick")){
			Property.StepDescription = "Double click on '" + TestObject + "'";
			objSeleniumDriver.DoubleClick();
		}
		
/*********************************************************************************************
Step action name: 		enterdata
						OR
						type
Description:			Enter/type data in GUI item								
Input variables:		TestObject
						AND
						dataContentFirst
Content of variable:	Testobject name of GUI item
						Value that will be entered in GUI item
Output:					sendKeys(dataContentFirst)
						AND
						setVariable(TestObject, dataContentFirst)
*********************************************************************************************/	
				
		else if(stepAction.toLowerCase().equals("enterdata") || stepAction.toLowerCase().equals("type")){
			Property.StepDescription = "Enter Text '" + dataContentFirst + "' in '" + TestObject + "'";
			objSeleniumDriver.sendKeys(dataContentFirst);
			Utility.setKeyValueInGlobalVarMap(TestObject, dataContentFirst);
			
		}
		
/*********************************************************************************************
Step action name: 		fireevent
Description:			Select data in GUI item								
Input variables:		TestObject
						AND
						dataContentFirst
						AND
						IsPlain_JS
Content of variable:	Testobject name of GUI item
						Name of item that will be selected in GUI
Output:					FireEvent(dataContentFirst,IsPlain_JS)
*********************************************************************************************/	
		
		else if(stepAction.equalsIgnoreCase("fireevent")){
			Property.StepDescription = "Fire '" + dataContentFirst + "' on '" + TestObject + "'";
			Boolean IsPlain_JS = false;
			if(objDataRow.size() == 0){
				IsPlain_JS = true;
			}
			objSeleniumDriver.FireEvent(dataContentFirst,IsPlain_JS);
			System.out.println("done");
		}
		
/*********************************************************************************************
Step action name: 		enteruniquedata			
Description:			?							
Input variables:		None
Content of variable:	NA
Output:					None
*********************************************************************************************/	
				
		else if(stepAction.equalsIgnoreCase("enteruniquedata")){
		}
		
/*********************************************************************************************
Step action name: 		keypress
Description:			Press specific key on GUI item							
Input variables:		TestObject
						AND
						dataContentFirst
Content of variable:	Testobject name of GUI item
						Keyboard key that will be used in GUI
Output:					KeyPress(dataContentFirst)
*********************************************************************************************/	
				
		else if(stepAction.equalsIgnoreCase("keypress")){
			Property.StepDescription = "Press Keyboard key '" + dataContentFirst + "' on '" + TestObject + "'";
			objSeleniumDriver.KeyPress(dataContentFirst);
		}
		
/*********************************************************************************************
Step action name: 		wait
Description:			?							
Input variables:		None
Content of variable:	NA
Output:					None
*********************************************************************************************/	
				
		else if(stepAction.equalsIgnoreCase("wait")){
			Thread.sleep(Long.parseLong(dataContentFirst));
			//Thread.sleep(3000);
		}
		
/*********************************************************************************************
Step action name: 		selectitem
Description:			Select specific item from dropdown menu							
Input variables:		TestObject
						AND
						dataContentFirst
Content of variable:	Testobject name of dropdown menu
						Data that need the be selected from menu
Output:					selectItem(dataContentFirst)
*********************************************************************************************/	
						
		else if(stepAction.equalsIgnoreCase("selectitem")){
			Property.StepDescription = "Select '" + dataContentFirst + "' in '" + TestObject + "'";
			objSeleniumDriver.selectItem(dataContentFirst);
		}
		
/*********************************************************************************************
Step action name: 		selectitembyindex
Description:			Select specific item by index number
Input variables:		TestObject
						AND
						dataContentFirst
Content of variable:	Testobject name of GUI item
						Item number of data that need the be selected
Output:					SelectElementByIndex(dataContentFirst)
*********************************************************************************************/	
		
		else if(stepAction.equalsIgnoreCase("selectitembyindex")){
			Property.StepDescription = "Select '" + dataContentFirst +"th' item from '"+TestObject;
			String optionValue = objSeleniumDriver.SelectElementByIndex(dataContentFirst);
			Utility.setKeyValueInGlobalVarMap(TestObject, optionValue);
		}
		
/*********************************************************************************************
Step action name: 		getobjectproperty
						OR
						getattribute
Description:			Get object property 
Input variables:		TestObject
						AND
						dataContentFirst
						AND
						dataContentSecond
Content of variable:	Testobject name of GUI item
						Item number of data that need the be selected
Output:					GetObjectProperty(property)
*********************************************************************************************/	
		
		else if(stepAction.equalsIgnoreCase("getobjectproperty") || stepAction.equalsIgnoreCase("getattribute")){
			String property = dataContentFirst.trim();
			String propertyvariable = TestObject + "." + property;
			if(dataContentSecond != "" && dataContentSecond != null)
				propertyvariable = dataContentSecond.trim();
			Property.StepDescription = "Get Property of '" + TestObject + "' and set its value in '" + propertyvariable + "'";
			String propertyValue =  objSeleniumDriver.GetObjectProperty(property);
			Utility.setKeyValueInGlobalVarMap(propertyvariable, propertyValue);
		}
		
/*********************************************************************************************
Step action name: 		setvariable
Description:			Set variable (used in function: enterdata \ getobjectproperty \ selectitembyindex
Input variables:		dataContentFirst
						AND
						dataContentSecond
Content of variable:	Testobject name of GUI item
						Testobject value of GUI item
Output:					Utility.setVariable(dataContentFirst, dataContentSecond)
*********************************************************************************************/	
		
		else if(stepAction.equalsIgnoreCase("setvariable")){
			Property.StepDescription = "Set the variable '" + dataContentFirst + "' with value '" + dataContentSecond + "'";
			Utility.setKeyValueInGlobalVarMap(dataContentFirst, dataContentSecond);
		}
		
/*********************************************************************************************
Step action name: 		getpageproperty
Description:			Get Page property 
Input variables:		property
Content of variable:	title
						url
Output:					property value
# Fixed by Jurian Driesten
*********************************************************************************************/			
		
		else if(stepAction.equalsIgnoreCase("getpageproperty")){
			String property = dataContentFirst.trim();
			String propertyValue =  objSeleniumDriver.GetPageProperty(property);
			Property.StepDescription = "Get Page Property of '" + property + "' and propertyValue '" + propertyValue + "'";
			Utility.setKeyValueInGlobalVarMap(property, propertyValue);
		}

/*********************************************************************************************
Step action name: 		verifytextpresent 
						verifyTextPresentOnPage (don't need to specify OR testobject)
						or 
						verifyObjectProperty (need to specify OR testobject)
Description:			Verify if given text is present on browser screen
Input variables:		objDataRow.size
						dataContentFirst
						optionsList
Content of variable:	Check content of value
						Value of Text Property 
						{partialmatch}{ignorecase}
Output:					verifyTextPresentOnPage(dataContentFirst,optionsList)
						OR
						verifyObjectProperty("text", dataContentFirst,optionsList)
*********************************************************************************************/			
		
		else if(stepAction.equalsIgnoreCase("verifytextpresent")){
		
			if(objDataRow.size() == 0){
				verification = objSeleniumDriver.verifyTextPresentOnPage(dataContentFirst,optionsList);
			}
			else{
				verification = objSeleniumDriver.verifyObjectProperty("text", dataContentFirst,optionsList);
				}
			Property.StepDescription = "Verify if text '"+ dataContentFirst + "' with '"+ optionsList + "' and size '"+ verification + "' is present";				
		}
		
		
/*********************************************************************************************
Step action name: 		CompareDBResults 
Description:			Compare two DB Results
Input variables:		text
						optionsList
Content of variable:	Value of Text Property 
									{partialmatch}{ignorecase}{ignorestep}
Output:					objSeleniumDriver.CompareDBResults
*********************************************************************************************/		

		else if(stepAction.equalsIgnoreCase("CompareDBResults")){
			
			verification = objSeleniumDriver.CompareDBResults(dataContentFirst,dataContentSecond, optionsList);
			
			Property.StepDescription = "Verify if text '"+ dataContentFirst + "' is the same as  '"+ dataContentSecond + "'";				
		}		
		
		
/*********************************************************************************************
Step action name: 		InputFileName 
Description:			Enter filename of file to upload
Input variables:		text
Content of variable:	Filename 
Output:					objSeleniumDriver.FileUpload
*********************************************************************************************/		
			else if(stepAction.equalsIgnoreCase("InputFileName")){
						objSeleniumDriver.FileUpload(dataContentFirst);
						Property.StepDescription = "Enter filename '"+ dataContentFirst + "' for uploading '";	
					}		
		
/*********************************************************************************************
Step action name: 		RandomNameGenerator 
Description:			Generates a Random name
Input variables:		lenght of name
Content of variable:	number 
Output:					objSeleniumDriver.RandonName
*********************************************************************************************/		
			else if(stepAction.equalsIgnoreCase("RandomNameGenerator")){
				
				Utility.setKeyValueInGlobalVarMap(TestObject, objSeleniumDriver.RandomNameGenerator());
				System.out.println(Utility.getValueFromGlobalVarMap(TestObject));
				Property.StepDescription = "Generate a random name of'"+ dataContentFirst + "' characters '";	
			}			
		
		
/*********************************************************************************************
Step action name: 		verifyobjectproperty 
Description:			Verify if Object Property is present on browser screen
Input variables:		dataContentFirst
						dataContentSecond 
						optionsList
Content of variable:	Name of Object Property (for example: )
						Value of Object Property (for example: )
						{partialmatch}{ignorecase}
Output:					verifyObjectProperty(dataContentFirst, dataContentSecond,optionsList)
*********************************************************************************************/			
		
		else if(stepAction.equalsIgnoreCase("verifyobjectproperty")){
			Property.StepDescription = "Verify Object Properties '" + dataContentFirst + "'-'" + dataContentSecond + "'-'" + optionsList + "' are present";
			verification = objSeleniumDriver.verifyObjectProperty(dataContentFirst, dataContentSecond, optionsList);
		}

/*********************************************************************************************
Step action name: 		verifypageproperty 
Description:			Verify if Page Property is present on browser screen
Input variables:		dataContentFirst
						dataContentSecond 
						optionsList
Content of variable:	Name of Page Property (for example: URL, title)
						Value of Page Property (for example: name of URL, name of URL)
						{partialmatch}{ignorecase}
Output:					verifyPageProperty(dataContentFirst, dataContentSecond,optionsList)
*********************************************************************************************/	
		
		else if(stepAction.equalsIgnoreCase("verifypageproperty")){
			Property.StepDescription = "Verify Page Properties '" + dataContentFirst + "'-'" + dataContentSecond + "'-'" + optionsList + "' are present";
			verification = objSeleniumDriver.verifyPageProperty(dataContentFirst, dataContentSecond,optionsList);
					
		}
		
/*********************************************************************************************
Step action name: 		verifyobjectpresent 
						(and waitAndGetElement)
Description:			Verify if Test Object is present on browser screen
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					verification = false 
						verification = true
*********************************************************************************************/	
		
		else if(stepAction.equalsIgnoreCase("verifyobjectpresent")){
			Property.StepDescription = "Verify object '" + TestObject + "' present";
			WebElement Element = objSeleniumDriver.waitAndGetElement();
			if(Element == null) { verification = false;}
			else {verification = true;}
		}

/*********************************************************************************************
Step action name: 		verifyobjectnotpresent 
						(and waitAndGetElement)
Description:			Verify if Test Object is notpresent on browser screen
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					verification = false 
						verification = true
*********************************************************************************************/	
				
		else if(stepAction.equalsIgnoreCase("verifyobjectnotpresent")){
			Property.StepDescription = "Verify object '" + TestObject + "' is not present";
			WebElement Element = objSeleniumDriver.waitAndVerifyObjectNotPresent();
			if(Element == null) { verification = true;}
			else {verification = false;}
		}		
		
/*********************************************************************************************
Step action name: 		waitforelement
Description:			Wait for webelement to display
Input variables:		TestObject
Content of variable:	NA
Output:					None (wait time)
*********************************************************************************************/			
		
		else if(stepAction.equalsIgnoreCase("waitforelement")){
			Property.StepDescription = "Wait for the'" + TestObject + "' element";
			objSeleniumDriver.waitAndGetElement();
		}
		
/*********************************************************************************************
Step action name: 		getResultFromDB
Description:			Execute SQL Queries
Input variables:		None
Content of variable:	NA
Output:					Query result
*********************************************************************************************/	

		else if(stepAction.equalsIgnoreCase("getResultFromDB")){
			Property.StepDescription = "Get the result from db";
			
			DBReader db = new DBReader();
			try {
				Utility.setKeyValueInGlobalVarMap(TestObject, db.getQueryResult(dataContentFirst));
				
				System.out.println(Utility.getValueFromGlobalVarMap(TestObject));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
/*********************************************************************************************
Step action name: 		updateDB
Description:			Execute Update SQL Queries
Input variables:		None
Content of variable:	NA
Output:					NA
*********************************************************************************************/	
		else if(stepAction.equalsIgnoreCase("UpdateDB")){
			Property.StepDescription = "Update value in db";
			
			DBReader db = new DBReader();
			try {
				Utility.setKeyValueInGlobalVarMap(TestObject, db.updateQuery(dataContentFirst));
				
				System.out.println(Utility.getValueFromGlobalVarMap(TestObject));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
				
		
		
/*********************************************************************************************
Step action name: 		Unknown step action
Description:			Fail teststep and throw exception when action does not exists
Input variables:		None
Content of variable:	NA
Output:					NoSuchMethodException("No Such Action") - > FAIL
*********************************************************************************************/
		
		else{
			throw new NoSuchMethodException("No Such Action");
		}
		
		
		if(verification){
			stepStatus = Property.PASS;
		}

		else{
			if(optionsList.contains("optional")) { stepStatus = Property.PASS;}
			else{stepStatus = Property.FAIL;}
		}
		
		
		}
		//catch (NoSuchMethodError e) {
		//	throw e;
		//}
		catch(NoSuchMethodException exception){
		  	  
		  stepAction = Property.FAIL;
		  throw exception;
		}
		catch(Exception e){
			Property.Remarks = e.getMessage();
			if(optionsList.contains("optional")){stepStatus = Property.PASS;} 
			else { stepStatus = Property.FAIL;}
		}
		
		Property.StepStatus = stepStatus;
		
		// Enable line below for easy debug
		// JOptionPane.showMessageDialog (null, "'" + Property.StepDescription + "' status: '" + stepStatus +"'");	
			
	}
}
