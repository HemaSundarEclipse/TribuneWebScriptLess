package Common;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

/**
 * All Utility functionality used in the framework listed here.
 * @author
 *
 */

public class Utility {
	
	public static Properties prop = new Properties();
	
	// Initializing an instance of ArrayList 'driverOptionlist'. Another instance of ArrayList 'optionsList' will be assigned to this in Actions.java.
	// 'optionsList' var will have list of options like partialmatch, ignorecase etc. from the excel sheet.
	public static ArrayList<String> driverOptionList = new ArrayList<String>();

	
	/**
	 * Load the Property (uiautomation.properties file) for the framework.
	 * @throws Exception
	 */
	public static void collectKeyValuePair() throws Exception {
		try {
			prop.load(new FileInputStream(Property.PropertyFileLocation));
					} catch (Exception e) {
			throw e;
		}
	}
    
	
	/**
     * Generate and return current time stamp.
     * @return String - Current time stamp in format like 'Fri Dec 28 125258 IST 2012'.
     */
	public static String getCurrentTimeStamp(){
		Date currentDate = new Date();
		return currentDate.toString().replace(":", "");
	}

	/**
	 * Populate Global hash map with the Key/Value given in uiautomation.properties file.
	 * Here we are creating a HashMap and data is supplied to it via an external file called uiautomation.properties. 
	 * @throws Exception
	 */
	public static void populateGlobalMap() throws Exception {
		try {
			Enumeration em = prop.keys();
			Set keySet = prop.keySet();
			Object[] keys = keySet.toArray();
			int i = 0;
			while (em.hasMoreElements()) {
				String element = (String) em.nextElement();
				// System.out.println("KEY : " + keys[i].toString() + "VALUE : "
				// + prop.getProperty(element));
				
				Property.globalVarMap.put(keys[i].toString().toLowerCase(),
						prop.getProperty(element));
				i++;
			}
			Property.globalVarMap.put("timestamp", getCurrentTimeStamp());
		} catch (Exception e) {
			throw e;
		}
	}


	
	
	/**
	 * Get the value of key from property file (uiautomation.properties).
	 * @param key
	 * @return String value for given key
	 */
	public static String getProperty(String key) {
		try {

			return prop.getProperty(key);

		} catch (Exception e) {
			return "";
		}
	}

	
	/**
	 * Set the key/value to property file. If the key does not match to an existing key then it adds a new property in form of key/value, otherwise it updates/changes the value
	 * of existing key.
	 * @param Key to set
	 * @param Value to set
	 */
	public static void setProperty(String Key, String Value) {
		try {
			prop.setProperty(Key, Value);
		}

		catch (Exception e) {
			//Nothing to throw.
		}

	}


	/**
	 * Set the key/value to global hash map used in the framework. Key will automatically be converted into lower case
	 * before keeping it into hash map.
	 * @param Key to set
	 * @param Value to set
	 * @throws Exception
	 */
	public static void setKeyValueInGlobalVarMap(String Key, String Value) throws Exception {
		try {
			Key = Key.toLowerCase();
			Property.globalVarMap.put(Key, Value);

		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Get the value of the key from global hash map.
	 * <b>Note : Key is case independent.</b>
	 * @param Key 
	 * @return Value of specified key.
	 * @throws Exception
	 */
	public static String getValueFromGlobalVarMap(String Key) throws Exception {
		try {
			Key = Key.toLowerCase();
			return Property.globalVarMap.get(Key);
		} catch (Exception e) {
			return Key;
		}
	}

	
	/**
	 * Check whether actual and expected matches based on matching options. 
	 * like {ignorecase} {ignorespace} {partialmatch} etc
	 * @param ExpectedValue
	 * @param ActualValue
	 * @return Boolean value based on matching results.
	 */
	public static boolean doMatchBasedOnOptions(String ExpectedValue,
			String ActualValue) {
		if (driverOptionList.contains("ignorespace")) {
			ExpectedValue = ExpectedValue.replace(" ", "");
			ActualValue = ActualValue.replace(" ", "");
		}
		if (driverOptionList.contains("ignorecase")) {
			ExpectedValue = ExpectedValue.toLowerCase();
			ActualValue = ActualValue.toLowerCase();
		}

		if (!driverOptionList.contains("partialmatch")
				|| driverOptionList.contains("exactmatch")) {
			return ActualValue.equals(ExpectedValue);
		} else {
			return ActualValue.contains(ExpectedValue);
		}
		
	}

	
	/**
	 * Replace all the occurrences of string like '{$KeyVar}' to its actual value stored in global hash map.
	 * For eg. if string is 'Test {$KeyVar} and KeyVar has value 'ABC' then resulting string would be 
	 * like Test ABC. If no value is present for a key then string returned as it is. If there is NO value for a key/variable in global hash map then it remains as it is.
	 * @param dataValue string that may contain format '{$KeyVar} 
	 * @return Resultant string with all occurrence of '{$KeyVar} replaced with its value.
	 */
	public static String replaceVariableInString(String dataValue) {
		try {

			for (int v = 0;; v++) {

				if (dataValue.contains("{$")) {
					int stindex = dataValue.indexOf("{$");
					int endindex = dataValue.indexOf('}');
					
					if (stindex < 0 || endindex < 0) {
						break;
					}
					String keyVariable = dataValue.substring((stindex + 2),
							endindex);
					String value = getValueFromGlobalVarMap(keyVariable);
					if (!value.equalsIgnoreCase(keyVariable)) {
						dataValue = dataValue.replace("{$" + keyVariable + "}",
								value);
					}

				} else {
					break;
				}
			}

		} catch (Exception e) {
			// Nothing to throw.
		}
		return dataValue;

	}

	
	/**
	 * Fetch all the external parameters given as parameters in maven command and save it to global map.
	 * <b>Note : If parameter is already present in global map, its value gets replaced with the input parameter. This is how you can change your parameters at run time</b> 
	 * @param ContentString that contains all parameter.
	 */
	public static void fetchAndSaveExternalParameters(String ContentString){
				
		try{
		for(int i=0;;i++){
			if(ContentString.contains("-D")){
				int stIndex = ContentString.indexOf("-D");
				
				int endIndex = ContentString.indexOf(" ", stIndex+1);
				if(endIndex == -1) { endIndex = ContentString.length();}
				if(stIndex < 0 || endIndex < 0){
					break;
				}
				String KeyValueString = ContentString.substring(stIndex+2,endIndex).trim();
				if(KeyValueString.contains("=")){
					String[] keyValuePair = KeyValueString.split("=");
					String Key = keyValuePair[0].trim().toLowerCase();
					String Value  = keyValuePair[1].trim();
					System.out.println("Key = " + Key + " And Value = "+Value );
					Utility.setKeyValueInGlobalVarMap(Key, Value);
				}
				
				StringBuilder string = new StringBuilder(ContentString);
				string.delete(stIndex, stIndex+2);
				ContentString = string.toString();
				
			}

			else{
				break;
			}
		}
		}
		catch(Exception e){
			reporting.ReportCreation.WriteLogMessage("Exception in fetching external parameters");
		}

}
	
	/**
	 * Returns current date and time in 'yyyy/MM/dd HH:mm:ss' format
	 * This method is called to set Property.StepExecutionTime in 'Actions.java' via
	 * Property.StepExecutionTime = Common.Utility.getCurrentDateAndTime();
	 */
	public static String getCurrentDateAndTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Store all the external parameters in global HashMap.
	 * @pro1 Instance of Properties class
	 */	
	public static void addExternalProperties(Properties prop1){
		try {
			 Set Keyset = prop1.keySet();
			// Set ExistingKeySet = Property.globalVarMap.keySet();
			// Keyset.retainAll(ExistingKeySet);
			
			  Object[] AllKeys = Keyset.toArray();
			 
			 for (Object key : AllKeys) {
				String keystring = (String) key;
				String value = prop1.getProperty(keystring);
							
				 setKeyValueInGlobalVarMap(keystring, value);
			}
			
		} catch (Exception e) {
			
		}
	}
	
	
	
}