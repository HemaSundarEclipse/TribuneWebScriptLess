/*********************************************
 seleniumdriver.SeleniumImplementation.java : Contains all selenium implementation functionalities.
 ********************************************/

package seleniumdriver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Common.Property;
import Common.Utility;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
//import org.openqa.selenium.internal.selenesedriver.TakeScreenshot;

public class SeleniumImplementation {

	private static String browserName;
	public static WebDriver driver;
	private String attribute;
	private String attributeType;
	private Selenium selenium;
	private static WebDriverWait wait = null;
	WebElement testObject;
	List<WebElement> testObjects = null;
	private HashMap<String, String> objDataRow = new HashMap<String, String>();

/*********************************************************************************************
Function name: 			SeleniumImplementation
Description:			Initialize Webdriver based on browser
Input variables:		while creating an instance of SeleniumImplementation, you will pass on browserType (Chrome/FF/IE)
Processing:				It will initiate a webdriver instance
Output:					You have an instance of SeleniumImplementation
*********************************************************************************************/	
	
	public SeleniumImplementation(String bName) {
		if (bName != null) {
			this.browserName = bName;
			initDriver();

			wait = new WebDriverWait(driver,
					Integer.parseInt(Property.SyncTimeOut));
		}
	}


/*********************************************************************************************
Function name: 			initDriver
Description:			Initialize the driver for the current (remote) Browser 
Input variables:		None
Content of variable:	NA
Output:					driver
*********************************************************************************************/			
	
	public void initDriver() {
		System.out.println("Calling init");
		System.out.println("Remote value = " + Property.IsRemoteExecution);
		try {
			if (Property.IsRemoteExecution.equalsIgnoreCase("true")) {
				System.out.println("Remote execution is true");
				String remoteURL = Property.RemoteURL + "/wd/hub";
				URL uri = new URL(remoteURL);
				DesiredCapabilities capabilities = new DesiredCapabilities();
				
				
				if (Property.RemoteURL.toLowerCase().contains("saucelabs")) {
					System.out.println("Remote Execution through saucelabs");
					
					if (browserName.equalsIgnoreCase("internetexplorer")) {
						System.out.println("Remote browser is internetxplorer");
						capabilities.setCapability(CapabilityType.BROWSER_NAME, "internet explorer");
						capabilities.setCapability(CapabilityType.VERSION, "8");
						capabilities.setCapability(CapabilityType.PLATFORM, "Windows 7");
						
						
					} else if (browserName.equalsIgnoreCase("safari")) {
						System.out.println("Remote browser is safari");
						//capabilities = DesiredCapabilities.safari();
						capabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
						capabilities.setCapability(CapabilityType.VERSION, "7");
						capabilities.setCapability(CapabilityType.PLATFORM, "OS X 10.9");
												
					}
										
					else {
						System.out.println("Remote browser is either Firefox or chrome");
						capabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
						capabilities.setCapability("name", "TribuneSanityCheck1");
						//capabilities.setCapability(CapabilityType.PLATFORM, "Windows 7");
					}
					 	

				        
				        
				}

				if (browserName.equalsIgnoreCase("firefox")) {
					System.out.println("Creating profile for remote FF browser");
					FirefoxProfile remoteProfile = new FirefoxProfile();
					remoteProfile.setPreference(
							"webdriver_assume_untrusted_issuer", false);
					remoteProfile.setAcceptUntrustedCertificates(true);
					remoteProfile.setEnableNativeEvents(true);
					capabilities.setBrowserName("firefox");
					capabilities.setCapability("firefox_profile", remoteProfile
							.toString().toString());
					driver = new RemoteWebDriver(uri, capabilities);
					
					
					
				} else if (browserName.equalsIgnoreCase("internetexplorer")) {
					System.out.println("Creating profile for remote IE browser");
					capabilities.setBrowserName("internet explorer");
					capabilities.setCapability("ignoreProtectedModeSettings",
							true);
					driver = new RemoteWebDriver(uri, capabilities);
				}
				
				else if (browserName.equalsIgnoreCase("safari")) {
					System.out.println("Creating profile for remote Safari browser");
					driver = new RemoteWebDriver(uri, capabilities);
				}
		
				
			} else {
				System.out.println("Remote execution is false");
				if (browserName.equalsIgnoreCase("firefox")) {
					FirefoxProfile ffprofile = new FirefoxProfile();
					ffprofile.setPreference(
							"webdriver_assume_untrusted_issuer", "false");
					driver = new FirefoxDriver(ffprofile);
				
				} else if (browserName.equalsIgnoreCase("internetexplorer")) {
					DesiredCapabilities iecapabilities = DesiredCapabilities
							.internetExplorer();
					iecapabilities.setCapability("ignoreProtectedModeSettings",
							"true");
					iecapabilities
							.setCapability(
									InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
									"true");
					File file = new File("IEDriverServer.exe");
					System.setProperty("webdriver.ie.driver", file.getPath());				
					driver = new InternetExplorerDriver(iecapabilities);
				
				} else if (browserName.equalsIgnoreCase("chrome")) {

					DesiredCapabilities chromeCapabilities = DesiredCapabilities
							.chrome();
					/*chromeCapabilities.setCapability("chrome.switches", Arrays
							.asList("--start-maximized",
									"--ignore-certificate-errors"));*/
					chromeCapabilities.setCapability("chrome.switches", Arrays
							.asList("--start-maximized"));
					
					// chromeCapabilities.setCapability("chrome.switches",
					// Arrays.asList("--ignore-certificate-errors"));
					ChromeDriverService service = new ChromeDriverService.Builder()
							.usingAnyFreePort()
							.usingDriverExecutable(new File("chromedriver.exe"))
							.build();
					service.start();
					driver = new ChromeDriver(service, chromeCapabilities);

				}
			}
		} catch (Exception e) {
			System.out.println("exception aaa");
			System.out.println(e.getStackTrace());
			
			System.out.println(e.getMessage());
		}
		driver.manage().timeouts().implicitlyWait(15000, TimeUnit.MILLISECONDS);
		System.out.println("ending init");
	}

/*********************************************************************************************
Step action name: 		openBrowser			
Description:			Open a new browser and navigate to URL												
Input variables:		Url
						BrowserName
Content of variable:	URL
Output:					objSeleniumImplementation
*********************************************************************************************/		
	
	public SeleniumImplementation openBrowser(String Url) {
		SeleniumImplementation objSeleniumImplementation = null;
		try {
			objSeleniumImplementation = new SeleniumImplementation(
					Property.BrowserName);
			System.out.println("Opening..." + Url );
			driver.get(Url);
			System.out.println("Opened..." + Url );
			this.DeleteAllCookies();
			driver.manage().window().maximize();
			System.out.println("browser window maximized");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return objSeleniumImplementation;
	}

/*********************************************************************************************
Step action name: 		getElement			
Description:			Get TestObject 									
Input variables:		attribute
						attributeType
Content of variable:	attribute is locator strategy (css/xpath/id/name/ 
						attributeType is locator.
Output:					WebElement
*********************************************************************************************/			
	
	public WebElement getElement() throws Exception {
		try {
			testObject = getElementByAttribute(attribute, attributeType);
		} catch (Exception e) {
			throw e;
		}
		return testObject;
	}
	
	/*********************************************************************************************
	Function name: 			getElementByAttribute()			
	Description:			Locate UI Element attribute by type (id\name\xpath\class\text)
	Input variables:		Attribute
							AttributeType
	Content of variable:	testobject name
							testobject value
	Output:					TestObject
	*********************************************************************************************/		
		
		public WebElement getElementByAttribute(String Attribute,
				String AttributeType) {
			try {
				if (AttributeType.equalsIgnoreCase("css")) {
					testObjects = driver.findElements(By.cssSelector(Attribute));
				} else if (AttributeType.equalsIgnoreCase("id")) {
					testObjects = driver.findElements(By.id(Attribute));
				} else if (AttributeType.equalsIgnoreCase("name")) {
					testObjects = driver.findElements(By.name(Attribute));
				} else if (AttributeType.equalsIgnoreCase("xpath")) {
					testObjects = driver.findElements(By.xpath(Attribute));
				} else if (AttributeType.equalsIgnoreCase("class")) {
					testObjects = driver.findElements(By.className(Attribute));
				} else if (AttributeType.equalsIgnoreCase("text")) {
					testObjects = driver.findElements(By.linkText(Attribute));
				} else {
					throw new Exception("Incorrect Attribute type mentioned");
				}

				if (testObjects.size() == 0) {
					throw new NoSuchElementException("Element couldn't be located");
				} else {
					testObject = testObjects.get(0);
					// TODO add code for parsing between different objects returned.
				}
				return testObject;

			} catch (NoSuchElementException e) {
				return null;
			}

			catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}	

		/*********************************************************************************************
		Function name: 			waitAndGetElement			
		Description:			Wait until WebElement is shown on screen and get WebElement										
		Input variables:		TestObject
		Content of variable:	Properties of Webelement
		Output:					testObject
		*********************************************************************************************/			
			
			public WebElement waitAndGetElement() throws Exception {
				try {
					SwitchToMostRecentWindow(); //Switching to most recent window.
					wait.until(new ExpectedCondition<WebElement>() {
						public WebElement apply(WebDriver d) {
							try {
								testObject = getElement();
								return testObject;
							} catch (Exception e) {
								return null;
							}

						}
					});
					return testObject;

				} catch (TimeoutException exception) {
					throw new NoSuchElementException("Unable to locate element");
				} catch (Exception e) {
					throw e;
				}
			}
			
/*********************************************************************************************
Function name: 			DoubleClick()			
Description:			Doubleclick on GUI item								
Input variables:		attribute
Content of variable:	Testobject name of GUI item
Output:					selenium.doubleClick(attribute);
*********************************************************************************************/		
	
	public void DoubleClick() throws Exception {
		try {

			waitAndGetElement();

			testObject.click();
			testObject.click();
		} catch (Exception e) {
			throw e;
		}
	}



/*********************************************************************************************
Function name: 			keypress
Description:			Press specific key on GUI item							
Input variables:		TestObject
						AND
						String Key
Content of variable:	Testobject name of GUI item
						Keyboard key that will be used in GUI
Output:					testObject.sendKeys(Keys.ENTER);
						testObject.sendKeys(Keys.ARROW_DOWN);
						testObject.sendKeys(Keys.BACK_SPACE);
*********************************************************************************************/		
	
	public void KeyPress(String Key) throws Exception {
		testObject = waitAndGetElement();
		if (Key.equalsIgnoreCase("enter")) {
			testObject.sendKeys(Keys.ENTER);
		} else if (Key.equalsIgnoreCase("arrowdown")) {
			testObject.sendKeys(Keys.ARROW_DOWN);
		} else if (Key.equalsIgnoreCase("backspace")) {
			testObject.sendKeys(Keys.BACK_SPACE);
		}
	}

/*********************************************************************************************
Function name: 			check()			
Description:			Check Checkbox GUI item										
Input variables:		TestObject
Content of variable:	Testobject name of Checkbox
Output:					testObject.click();
*********************************************************************************************/		
	
	public void check() throws Exception {
		try {
			testObject = waitAndGetElement();
			if (!testObject.isSelected())
				testObject.click();

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			Uncheck()		
Description:			Uncheck Checkbox GUI item										
Input variables:		TestObject
Content of variable:	Testobject name of Checkbox
Output:					testObject.click();
*********************************************************************************************/			
	
	public void Uncheck() throws Exception {
		try {
			testObject = waitAndGetElement();
			if (testObject.isSelected())
				testObject.click();

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			sendKeys
Description:			Enter/type data in GUI item								
Input variables:		String Text
Content of variable:	Value that will be entered in GUI item
Output:					this.check();
						this.Uncheck();
						this.ExecuteScript(testObject, "arguments[0].value=\"\";");
						testObject.sendKeys(Text);
						testObject = waitAndGetElement();
						testObject.clear();
						testObject.click();
*********************************************************************************************/	
	
	public void sendKeys(String Text) throws Exception {
		try {
			testObject = waitAndGetElement();
			// Check ON and OFF condition for radio button and check box.
			if (Text.equals("ON") || Text.equals("OFF")) {
				String objType = testObject.getAttribute("type");
				if (objType.equalsIgnoreCase("radio")
						|| objType.equalsIgnoreCase("checkbox")) {
					if (Text.equals("ON")) {
						this.check();
					} else if (Text.equals("OFF")) {
						this.Uncheck();
					}
				}

			} else {
					try {
						this.ExecuteScript(testObject, "arguments[0].value=\"\";");

						} catch (Exception e) { }
					testObject.sendKeys(Text);
					}

		} catch (StaleElementReferenceException e) {
			testObject = waitAndGetElement();
			testObject.clear();
			testObject.click();
		}

		catch (Exception e) {
			if (e.getMessage().contains(
					"Element is no longer attached to the DOM")) {
				testObject = waitAndGetElement();
				testObject.clear();
				testObject.click();
			}

			throw e;
		}
	}

/*********************************************************************************************
Function name: 			click			
Description:			Click on GUI item								
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					waitAndGetElement();
						testObject.click();
*********************************************************************************************/		
	
	public void click() throws Exception {
		try {
			waitAndGetElement();

			testObject.click();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public void mouseOver() throws Exception {
		try {
			waitAndGetElement();

			Actions action = new Actions(driver);
			//action.moveToElement(testObject).click().build().perform();
			action.moveToElement(testObject).build().perform();
			
		} catch (Exception e) {
			throw e;
		}
	}
	

/*********************************************************************************************
Function name: 			DeleteAllCookies			
Description:			Get and delete cookies in selected browser							
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					waitAndGetElement();
						testObject.click();
*********************************************************************************************/		
	
	public void DeleteAllCookies() {
		try {
			driver.manage().deleteAllCookies();
			Set<Cookie> cookies = driver.manage().getCookies();
			for (Cookie cookie : cookies) {
				driver.manage().deleteCookie(cookie);
			}

		} catch (Exception e) {
			// Nothing to throw;
		}

	}
	
	
/*********************************************************************************************
Function name: 			fireevent
Description:			Select data in GUI item								
Input variables:		TestObject
						AND
						String EventName
						AND
						Boolean IsPlain_JS
Content of variable:	Testobject name of GUI item
						Name of item that will be selected in GUI
Output:					((JavascriptExecutor) driver).executeScript(EventName);

						onEventName = EventName;
						EventName = EventName.substring(2);
						
						ExecuteScript(testObject, script);
*********************************************************************************************/		

	public void FireEvent(String EventName,Boolean IsPlain_JS) throws Exception {
		String script;
		String onEventName;
		try {
			if(IsPlain_JS){
				((JavascriptExecutor) driver).executeScript(EventName);
			}
			else{
			testObject = waitAndGetElement();
			EventName = EventName.trim().toLowerCase();
			// Event name should starts with on for internet explorer.
			if (EventName.startsWith("on")) {
				onEventName = EventName;
				EventName = EventName.substring(2);
			} else {
				onEventName = "on" + EventName;
			}
			script = "var canBubble = false;var element = arguments[0];if (document.createEventObject()) {var evt = document.createEventObject();arguments[0].fireEvent('"
					+ onEventName
					+ "', evt);}    else {var evt = document.createEvent(\"HTMLEvents\");evt.initEvent('"
					+ EventName
					+ "', true, true);arguments[0].dispatchEvent(evt);}";

			// Firefox and other browser has to force this script.
			if (!browserName.equalsIgnoreCase("internetexplorer")) {
				script = "var evt = document.createEvent(\"HTMLEvents\"); evt.initEvent(\""
						+ EventName
						+ "\", true, true );return !arguments[0].dispatchEvent(evt);";
			}
			ExecuteScript(testObject, script);
			}
		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Step action name: 		verifyTextPresentOnPage 
Description:			Verify if given text is present on browser screen
Input variables:		text
						optionsList
Content of variable:	Value of Text Property 
						{partialmatch}{ignorecase}{ignorestep}
Output:					isTextVerified
*********************************************************************************************/			
	
	public boolean verifyTextPresentOnPage(String text,
			ArrayList<String> options) throws Exception {
		try {
			boolean isTextVerified = false;
			text = text.trim();
			text = Utility.replaceVariableInString(text);
			WebElement objPage = driver.findElement(By.xpath("//html"));
			String pageText = objPage.getText();

				System.out.println(pageText);
				
			// if options contain either of {partialmatch}{ignorecase}{ignorestep} then doMatchBasedOnOptions 
			if (!options.isEmpty()) {
				isTextVerified = Utility.doMatchBasedOnOptions(text, pageText);
			} else {
				isTextVerified = pageText.contains(text);
			}
						
			if (!isTextVerified) {
				Property.Remarks = "Text : " + text + " is not found on page";
			}
			return isTextVerified;
		} catch (WebDriverException e) {
			if (e.getMessage().contains("No response from server for url")) {
				throw new Exception("No response from Driver");
			}
			throw new Exception(
					"No response from the Driver. Session not exists.");
		}

		catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Step action name: 		CompareDBResults 
Description:			Compare two DB Results (strings)
Input variables:		text
						optionsList
Content of variable:	Value of Text Property 
						{partialmatch}{ignorecase}{ignorestep}
Output:					isPropertyVerified
*********************************************************************************************/		
	public boolean CompareDBResults(String Property_one, String Property_two,
			ArrayList<String> options) throws Exception {
		try {
			boolean isPropertyVerified = false;
			
			if (!options.isEmpty()) {
				isPropertyVerified = Utility.doMatchBasedOnOptions(
						Property_one, Property_two);
			} else {
				isPropertyVerified = Property_one.equals(Property_two);
			}
			if (!isPropertyVerified) {
				Common.Property.Remarks = "' actual value - '" + Property_one
						+ "' doesn't match with expected value - '"
						+ Property_two + "'.";
			}
			return isPropertyVerified;

		} catch (Exception e) {
			throw e;
		}
	}	
	
	
	
/*********************************************************************************************
Function name: 			SetAttribute 
Description:			?
Input variables:		Property
						PropertyValue						
Content of variable:	?
Output:					?
*********************************************************************************************/				
	
	public void SetAttribute(String Property, String PropertyValue)
			throws Exception {
		try {
			waitAndGetElement();
			String JavaScript = "return arguments[0]." + Property + " = \""
					+ PropertyValue + "\";";
			this.ExecuteScript(testObject, JavaScript);

		} catch (Exception e) {
			throw e;
		}

	}

	/*
	 * //@SuppressWarnings("unchecked") public WebElement waitAndGetElement()
	 * throws Exception{ try{ WebDriverWait wait = new WebDriverWait(driver,
	 * 30);
	 * 
	 * Function<WebDriver, WebElement> funcObjectLoaded; funcObjectLoaded =
	 * (Function<WebDriver, WebElement>) this.getElement();
	 * wait.until(funcObjectLoaded); return testObject; } catch(TimeoutException
	 * e){ throw new
	 * NoSuchElementException("Unable to locate element: {method: "+
	 * attributeType + ", selector: " + attribute + " }"); } catch(Exception e){
	 * throw e; } }
	 */
	 
/*********************************************************************************************
Function name: 			getWindowHandles 
Description:			Get WindowHandles unique name
Input variables:		None					
Content of variable:	NA
Output:					windowHandles
*********************************************************************************************/			 
	 
	public Set<String> getWindowHandles() {

		try {

			Set<String> windowHandles = driver.getWindowHandles();
			return windowHandles;

		} catch (Exception e) {
			return null;
		}

	}

/*********************************************************************************************
Function name: 			SwitchToMostRecentWindow 
Description:			Switch to most recent Window
Input variables:		None					
Content of variable:	NA
Output:					driver.getWindowHandle
*********************************************************************************************/		
	
	public String SwitchToMostRecentWindow() {
		try {

			Set<String> windowHandles = getWindowHandles();
			for (String window : windowHandles) {
				driver.switchTo().window(window);
			}

		} catch (Exception e) {
			return "";

		}

		return driver.getWindowHandle();
	}

/*********************************************************************************************
Function name: 			goback			
Description:			Go one page backward in browser											
Input variables:		None
Content of variable:	NA
Output:					driver.navigate().back();
*********************************************************************************************/			
	
	public void goBack() throws Exception {
		try {
			driver.navigate().back();

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			goforward			
Description:			Go one page forward in browser											
Input variables:		None
Content of variable:	NA
Output:					driver.navigate().forward();
*********************************************************************************************/		
	
	public void goForward() throws Exception {
		try {
			driver.navigate().forward();

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			refreshBrowser			
Description:			Refresh currently opened browser											
Input variables:		None
Content of variable:	NA
Output:					driver.navigate().refresh();
*********************************************************************************************/		
	
	public void refreshBrowser() throws Exception {
		try {
			driver.navigate().refresh();

		} catch (Exception e) {
			throw e;
		}
	}


	
	/*********************************************************************************************
	Function name: 			waitAndVerifyObjectNotPresent			
	Description:			Wait 5 seconds to verify WebElement is NOT shown on screen										
	Input variables:		TestObject
	Content of variable:	Properties of Webelement
	Output:					testObject
	*********************************************************************************************/			
		
		public WebElement waitAndVerifyObjectNotPresent() throws Exception {
			// ExpectedCondition e = new ExpectedCondition<T>() {
			
			wait = new WebDriverWait(driver,5);
			
			try {
				SwitchToMostRecentWindow(); //Switching to most recent window.
				wait.until(new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver d) {
						try {
							testObject = getElement();
							return testObject;
						} catch (Exception e) {
							return null;
						}
						
					}
				});
				return testObject;

			} catch (TimeoutException exception) {
				return null;
			} catch (Exception e) {
				throw e;
			}
		}	
/*********************************************************************************************
Function name: 			closebrowser			
Description:			Close currently opened browser									
Input variables:		None
Content of variable:	NA
Output:					closeBrowser()
*********************************************************************************************/		
	
	public void closeBrowser() throws Exception {
		try {
			SwitchToMostRecentWindow();
			driver.close();
		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			closeallbrowsers			
Description:			Close all currently opened browsers									
Input variables:		None
Content of variable:	NA
Output:					driver.quit()
						driver.close();
*********************************************************************************************/		
	
	public void closeAllBrowsers() throws Exception {
		try {

			try {
				driver.quit();

			} catch (Exception e) {
				Set<String> windwHandles = getWindowHandles();
				for (String Handle : windwHandles) {
					driver.switchTo().window(Handle);
					try {
						driver.close();
					} catch (Exception ex) {
					}
				}
			}

		} catch (WebDriverException we) {
			// Nothing to throw;
		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			verifyObjectPresent 
Description:			Verify if Test Object is present on browser screen
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					status
*********************************************************************************************/	
	
	public boolean verifyObjectPresent() throws Exception {
		try {
			waitAndGetElement();
			if (testObject == null) {
				Property.Remarks = "Element is not present";
				return false;
			}

			WebElement element = (WebElement) testObject;
			boolean status = element.isDisplayed();

			if (!status) {
				Property.Remarks = "Element is present but not displayed.";
			}
			return status;

		} catch (NoSuchElementException e) {
			Property.Remarks = e.getMessage();
			return false;
		}

		catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			verifyObjectNotPresent 
Description:			Verify if Test Object is not present on browser screen
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					status
*********************************************************************************************/		
	
	public boolean verifyObjectNotPresent() throws Exception {
		try {
			boolean status = verifyObjectPresent();
			if (status) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			verifyObjectProperty
Description:			Verify if given text is present on browser screen
Input variables:		Property
						PropertyValue
						ArrayList
Content of variable:	Check content of value
						Value of Text Property 
						Options like: {partialmatch}{ignorecase}{ignorestep}
Output:					isPropertyVerified
*********************************************************************************************/		
	
	public boolean verifyObjectProperty(String Property, String PropertyValue,
			ArrayList<String> options) throws Exception {
		try {
			boolean isPropertyVerified = false;
			String actualPropertyValue = this.GetObjectProperty(Property);
			
			if (!options.isEmpty()) {
				isPropertyVerified = Utility.doMatchBasedOnOptions(
						PropertyValue, actualPropertyValue);
			} else {
				isPropertyVerified = actualPropertyValue.equals(PropertyValue);
			}
			if (!isPropertyVerified) {
				Common.Property.Remarks = "Property - '" + Property
						+ "' , actual value - '" + actualPropertyValue
						+ "' doesn't match with expected value - '"
						+ PropertyValue + "'.";
			}
			return isPropertyVerified;

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			ExecuteScript
Description:			Execute script on (remote) browser that is currently used
Input variables:		tObject
						Script
Content of variable:	testObject 
						Script code
Output:					scrResult.toString
*********************************************************************************************/	
	
	public String ExecuteScript(WebElement tObject, String Script)
			throws Exception {
		try {
			Object scrResult = null;
			if (Property.IsRemoteExecution.equalsIgnoreCase("false")) {
				if (browserName.equalsIgnoreCase("firefox")) {
					FirefoxDriver ffdriver = (FirefoxDriver) driver;
					scrResult = ffdriver.executeScript(Script, tObject);
					if (scrResult != null) {
						return scrResult.toString();
					}
				} else if (browserName.equalsIgnoreCase("internetexplorer")) {
					InternetExplorerDriver iedriver = (InternetExplorerDriver) driver;
					scrResult = iedriver.executeScript(Script, tObject);
					if (scrResult != null) {
						return scrResult.toString();
					}
				} else if (browserName.equalsIgnoreCase("chrome")) {
					ChromeDriver chromeDriver = (ChromeDriver) driver;
					scrResult = chromeDriver.executeScript(Script, tObject);
					if (scrResult != null) {
						return scrResult.toString();
					}
				} else {
					return "";
				}

			} else {
				RemoteWebDriver remoteDriver = (RemoteWebDriver) driver;
				scrResult = remoteDriver.executeScript(Script, tObject);
				if (scrResult != null) {
					return scrResult.toString();
				}
			}

			return "";

		} catch (WebDriverException e) {
			if (e.getMessage().contains("No response from server for url")) {
				throw new Exception("No response from the Driver");
			}
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

/*********************************************************************************************
Function name: 			verifyPageProperty 
Description:			Verify if Page Property is present on browser screen
Input variables:		Property
						PropertyValue 
						optionsList
Content of variable:	Name of Page Property (for example: URL, title)
						Value of Page Property (for example: name of URL, name of URL)
						{partialmatch}{ignorecase}{ignorestep}
Output:					IsPagePropertyVerify
*********************************************************************************************/		
	
	public boolean verifyPageProperty(String Property, String PropertyValue,
			ArrayList<String> options) throws Exception {
		try {
			String ActualValue = this.GetPageProperty(Property);
			boolean IsPagePropertyVerify = false;
			if (!options.isEmpty()) {
				IsPagePropertyVerify = Utility.doMatchBasedOnOptions(
						PropertyValue, ActualValue);
			}

			else {
				if (ActualValue.contains(PropertyValue)) {
					IsPagePropertyVerify = true;
				}
			}

			if (IsPagePropertyVerify) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			navigateurl			
Description:			Navigate to URL in currently opened browser											
Input variables:		String Url
Content of variable:	URL
Output:					driver.navigate().to(Url);
*********************************************************************************************/		
	
	public void NavigateUrl(String Url) throws Exception {
		try {
			driver.navigate().to(Url);
		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			verifyPageDisplayed			
Description:			??								
Input variables:		None
Content of variable:	NA
Output:					driver.getCurrentUrl().matches(attribute)
*********************************************************************************************/		
	
	public boolean verifyPageDisplayed() throws Exception {
		try {

			return driver.getCurrentUrl().matches(attribute);

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			shutDown			
Description:			Close Browser window							
Input variables:		None
Content of variable:	NA
Output:					driver.close() or driver.quit()
*********************************************************************************************/		
	public void shutDown() throws Exception {
		try {

			if (driver.getTitle() != null || driver.getTitle() != "") {
				driver.close();
				try {
					driver.quit();
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Step action name: 		GetPageProperty
Description:			Get a Page property 
Input variables:		Property
Content of variable:	title 
						url
Output:					driver.getTitle()
						driver.getCurrentUrl()
*********************************************************************************************/		
	
	public String GetPageProperty(String Property) throws Exception {
		try {
			if (Property.equalsIgnoreCase("title")) {
				return driver.getTitle();
			} else if (Property.equalsIgnoreCase("url")) {
				return driver.getCurrentUrl();
			} else {
				return null;
			}
		} catch (WebDriverException e) {
			if (e.getMessage().contains("No response from server for url")) {
				throw new WebDriverException("No response from driver");
			}
			throw new Exception("No response from driver. Session not exist.");
		} catch (Exception e) {
			throw e;
		}

	}

/*********************************************************************************************
Step action name: 		SelectElementByIndex
Description:			Select specific item by index number
Input variables:		TestObject
						AND
						Index
Content of variable:	Testobject name of GUI item
						Item number of data that need the be selected
Output:					testObject.getAttribute("value")
						options.get(index).getText();
*********************************************************************************************/		
	
	public String SelectElementByIndex(String Index) throws Exception {
		try {

			int index = Integer.parseInt(Index) - 1;
			testObject = waitAndGetElement();
			if (testObject.getAttribute("type").equalsIgnoreCase("radio")) {
				testObject = testObjects.get(index);
				this.ExecuteScript(testObject, "arguments[0].click();");
				return testObject.getAttribute("value");
			}

			else {
				Select select = new Select(testObject);
				select.selectByIndex(index);
				List<WebElement> options = select.getOptions();
				return options.get(index).getText();
			}
		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			selectitem
Description:			Select specific item from dropdown menu							
Input variables:		TestObject
						AND
						ItemtoSelect
Content of variable:	Testobject name of dropdown menu
						Data that need the be selected from menu
Output:					selectedOption
*********************************************************************************************/		
	
	public void selectItem(String ItemtoSelect) throws Exception {
		try {
			testObject = waitAndGetElement();
			Select objSelect = new Select(testObject);
			objSelect.selectByVisibleText(ItemtoSelect);
			WebElement selectedOption = objSelect.getFirstSelectedOption();

		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			GetSeleniumOne
Description:			Select specific item from dropdown menu							
Input variables:		TestObject
						AND
						ItemtoSelect
Content of variable:	Testobject name of dropdown menu
						Data that need the be selected from menu
Output:					selectedOption
*********************************************************************************************/		
	
	private Selenium GetSeleniumOne() throws Exception {
		try {

			if (selenium == null)
				selenium = new WebDriverBackedSelenium(driver,
						driver.getCurrentUrl());

			try {
				selenium.start();
			} catch (Exception e) {

			}
			return selenium;
		} catch (Exception e) {
			throw e;
		}
	}

/*********************************************************************************************
Function name: 			GetObjectProperty
Description:			Get an object property 
Input variables:		TestObject
						AND
						property
Content of variable:	Testobject name of GUI item
						Item number of data that need the be selected
Output:					ActualPropertyValue
*********************************************************************************************/		
	
	public String GetObjectProperty(String property) throws Exception {
		try {
			waitAndGetElement();
			String ActualPropertyValue = "";
			Boolean enable;
			String JavaScript = "";
			if (property.equalsIgnoreCase("text")) {
				ActualPropertyValue = testObject.getText();
			} else if (property.equalsIgnoreCase("style.background")
					|| property.equalsIgnoreCase("style.backgroundimage")
					|| property.equalsIgnoreCase("style.background-image")) {
				if (Property.BrowserName.equalsIgnoreCase("firefox")) {
					JavaScript = "return document.defaultView.getComputedStyle(arguments[0], '').getPropertyValue(\"background-image\");";
				} else if (Property.BrowserName
						.equalsIgnoreCase("internetexplorer")) {
					JavaScript = "return arguments[0].currentStyle.backgroundImage;";
				} else {
					JavaScript = "return arguments[0].currentStyle.backgroundImage;";
				}
				// ActualPropertyValue = this.ExecuteScript(JavaScript);

			} else if (property.equalsIgnoreCase("tagname")) {
				ActualPropertyValue = testObject.getTagName();
			} else if (property.equalsIgnoreCase("isEnable")) {
				enable = testObject.isEnabled();
				ActualPropertyValue = enable.toString();
			} else if (property.equalsIgnoreCase("background-color")) {
				ActualPropertyValue = testObject.getCssValue(property);
				} else {
				ActualPropertyValue = testObject.getAttribute(property);
			}
			return ActualPropertyValue;

		} catch (Exception e) {
			throw e;
		}

	}

/*********************************************************************************************
Function name: 			isEnable
Description:			check if TestObject is enabled
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					true/false
*********************************************************************************************/			
	
	public boolean isEnable() throws Exception {
		try {
			testObject = waitAndGetElement();
			if (testObject.isEnabled())
				return true;

		} catch (Exception e) {
			throw e;
		}
		return false;
	}
	

	
/*********************************************************************************************
Step action name: 		RandomNameGenerator 
Description:			Generates a Random name
Input variables:		NA
Content of variable:	NA 
Output:					Random Name
*********************************************************************************************/		

	public String RandomNameGenerator() throws Exception {

		Random rand = new Random();

		String randomName ="";
		int  n = rand.nextInt(10000) + 1;
		
		randomName = "SeleniumJob" + n +"";
		return randomName;
				
	    }
		
			
/*********************************************************************************************
Step action name: 		InputFileName 
Description:			Enter filename of file to upload
Input variables:		text
Content of variable:	Filename 
Output:					objSeleniumDriver.FileUpload
	*********************************************************************************************/			
	
	public void FileUpload(String UploadFileName) throws AWTException, IOException,	InterruptedException {
		for (int i = 0; i < UploadFileName.length(); i++){
		    	char KeyBoardInput = UploadFileName.charAt(i);        
		    	Robot r;
				try {
					r = new Robot();
				        switch (KeyBoardInput) {
				            case 'A': r.keyPress(KeyEvent.VK_A); 
									 r.keyRelease(KeyEvent.VK_A);
				                     break;
				            case 'B':  r.keyPress(KeyEvent.VK_B); 
							          r.keyRelease(KeyEvent.VK_B);
				                     break;
				            case 'C':  r.keyPress(KeyEvent.VK_C); 
					                 r.keyRelease(KeyEvent.VK_C);
				                     break;
				            case 'D':  r.keyPress(KeyEvent.VK_D); 
					                 r.keyRelease(KeyEvent.VK_D);
				                     break;
				            case 'E':  r.keyPress(KeyEvent.VK_E); 
					                 r.keyRelease(KeyEvent.VK_E);
				                     break;
				            case 'F':  r.keyPress(KeyEvent.VK_F); 
					                 r.keyRelease(KeyEvent.VK_F);
				                     break;
				            case 'G':  r.keyPress(KeyEvent.VK_G); 
					                 r.keyRelease(KeyEvent.VK_G);
				                     break;
				            case 'H':  r.keyPress(KeyEvent.VK_H); 
					                 r.keyRelease(KeyEvent.VK_H);
				                     break;
				            case 'I':  r.keyPress(KeyEvent.VK_I); 
					                 r.keyRelease(KeyEvent.VK_I);
				                     break;
				            case 'J':  r.keyPress(KeyEvent.VK_J); 
					                 r.keyRelease(KeyEvent.VK_J);
				                     break;
				            case 'K':  r.keyPress(KeyEvent.VK_K); 
					                 r.keyRelease(KeyEvent.VK_K);
				                     break;
				            case 'L':  r.keyPress(KeyEvent.VK_L); 
					                 r.keyRelease(KeyEvent.VK_L);
				                     break;
				            case 'M':  r.keyPress(KeyEvent.VK_M); 
					                 r.keyRelease(KeyEvent.VK_M);
				            		 break;
				            case 'N':  r.keyPress(KeyEvent.VK_N); 
					                 r.keyRelease(KeyEvent.VK_N);
				                     break;
				            case 'O':  r.keyPress(KeyEvent.VK_O); 
					                 r.keyRelease(KeyEvent.VK_O);
				                     break;
				            case 'P':  r.keyPress(KeyEvent.VK_P); 
					                 r.keyRelease(KeyEvent.VK_P);
				                     break;
				            case 'Q':  r.keyPress(KeyEvent.VK_Q); 
					                 r.keyRelease(KeyEvent.VK_Q);
				                     break;
				            case 'R':  r.keyPress(KeyEvent.VK_R); 
					                 r.keyRelease(KeyEvent.VK_R);
				                     break;
				            case 'S':  r.keyPress(KeyEvent.VK_S); 
					                 r.keyRelease(KeyEvent.VK_S);
				                     break;
				            case 'T':  r.keyPress(KeyEvent.VK_T); 
					                 r.keyRelease(KeyEvent.VK_T);
				                     break;
				            case 'U':  r.keyPress(KeyEvent.VK_U); 
					                 r.keyRelease(KeyEvent.VK_U);
				                     break;
				            case 'V':  r.keyPress(KeyEvent.VK_V); 
					                 r.keyRelease(KeyEvent.VK_V);
				                     break;
				            case 'W':  r.keyPress(KeyEvent.VK_W); 
					                 r.keyRelease(KeyEvent.VK_W);
				                     break;
				            case 'X':  r.keyPress(KeyEvent.VK_X); 
					                 r.keyRelease(KeyEvent.VK_X);
				                     break;	
				            case 'Y':  r.keyPress(KeyEvent.VK_Y); 
					                 r.keyRelease(KeyEvent.VK_Y);
		                     		 break;
				            case 'Z':  r.keyPress(KeyEvent.VK_Z); 
					                 r.keyRelease(KeyEvent.VK_Z);
				                     break;
				            case '1':  r.keyPress(KeyEvent.VK_1); 
					                 r.keyRelease(KeyEvent.VK_1);
				                     break;
				            case '2':  r.keyPress(KeyEvent.VK_2); 
					                 r.keyRelease(KeyEvent.VK_2);
				                     break;
				            case '3':  r.keyPress(KeyEvent.VK_3); 
					                 r.keyRelease(KeyEvent.VK_3);
				                     break;
				            case '4':  r.keyPress(KeyEvent.VK_4); 
					                 r.keyRelease(KeyEvent.VK_4);
				                     break;
				            case '5':  r.keyPress(KeyEvent.VK_5); 
					                 r.keyRelease(KeyEvent.VK_5);
				                     break;
				            case '6':  r.keyPress(KeyEvent.VK_6); 
					                 r.keyRelease(KeyEvent.VK_6);
				                     break;
				            case '7':  r.keyPress(KeyEvent.VK_7); 
					                 r.keyRelease(KeyEvent.VK_7);
				                     break;
				            case '8':  r.keyPress(KeyEvent.VK_8); 
					                 r.keyRelease(KeyEvent.VK_8);
				                     break;
				            case '9':  r.keyPress(KeyEvent.VK_9); 
					                 r.keyRelease(KeyEvent.VK_9);
				                     break;
				            case '0':  r.keyPress(KeyEvent.VK_0); 
					                 r.keyRelease(KeyEvent.VK_0);
				                     break;
				            case ':':r.keyPress(KeyEvent.VK_SHIFT);  
					                 r.keyPress(KeyEvent.VK_SEMICOLON);  
					                 r.keyRelease(KeyEvent.VK_SEMICOLON);  
					                 r.keyRelease(KeyEvent.VK_SHIFT);
					                 break;
				            case '\\':r.keyPress(KeyEvent.VK_BACK_SLASH); 
					                 r.keyRelease(KeyEvent.VK_BACK_SLASH);         
				                     break;				                     
				            case '.':r.keyPress(KeyEvent.VK_PERIOD); 
					         		 r.keyRelease(KeyEvent.VK_PERIOD);
				            		 break;
				            case '!':r.keyPress(KeyEvent.VK_ENTER); 
			                 		 r.keyRelease(KeyEvent.VK_ENTER);         
		                     break;						            		 
	   				            default:r.keyPress(KeyEvent.VK_ENTER); 
       		              			  r.keyRelease(KeyEvent.VK_ENTER);
				                     break;
				        }
 
		    
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		}
	}

/*********************************************************************************************
Function name: 			isNotEnable
Description:			check if a TestObject is not enabled
Input variables:		TestObject
Content of variable:	Testobject name of GUI item
Output:					true/false
*********************************************************************************************/			
	
	public boolean isNotEnable() throws Exception {
		try {
			testObject = waitAndGetElement();
			if (!testObject.isEnabled())
				return true;

		} catch (Exception e) {
			throw e;
		}
		return false;
	}



/*********************************************************************************************
Function name: 			setObjectDataRow
Description:			Sets objDatarow with the content of the 'how'[id/css/xpath/name] and 'what'[locator attribute] cells of every step
Input variables:		HashMap<String, String> objDatarow. This represents the data from a particular row from a TC.
Content of variable:	Location of content
Output:					objDatarow of this class
*********************************************************************************************/		
	
	public void setObjectDataRow(HashMap<String, String> objDatarow) {
		if (objDatarow != null) {
			attributeType = Utility.replaceVariableInString(objDatarow
					.get("how"));
			attribute = Utility.replaceVariableInString(objDatarow.get("what"));
			// Will add more if needed.
		}
		this.objDataRow = objDatarow;
	}
	
// FireEvent.
// KeyPress.
// GetElementFromFrames.

}