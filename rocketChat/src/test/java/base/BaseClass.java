package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import utilities.ExtentManager;

import utilities.ExcelReader;

import org.apache.log4j.Logger;

public class BaseClass {
 
	public static WebDriver driver;
	public static FileInputStream fis;
	public static Properties or;
	public static Properties config;
	public static String projectPath;
	public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\data\\testdata.xlsx");
	
	public static Logger log; //To log in Application log 
	public static WebDriverWait wait;
	public static ExtentTest test; //To log in Extent Report
	public ExtentReports rep = ExtentManager.getInstance();
	public static String browser;
	
	@BeforeSuite // Navigate Browser
	public void setup() throws IOException {
		System.setProperty("org.uncommons.reportng.escape-output", "false"); 

		log = Logger.getLogger("devpinoyLogger"); 
		
		projectPath = System.getProperty("user.dir"); // C:\\Java\\eclipse-workspace\\Craigslist
		
		fis = new FileInputStream(projectPath + "\\src\\test\\resources\\properties\\OR.properties");
		or = new Properties();// or = object repository
		or.load(fis);

		
		//log4j.setProperty("log4j.appender.dest1.File", projectPath + "\\src\\test\\resources\\logs\\application.log");
		
		System.out.println("total = " +  or.size() );

		fis = new FileInputStream(projectPath + "\\src\\test\\resources\\properties\\config.properties");
		config = new Properties();
		config.load(fis);

		if (config.getProperty("BROWSER").equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", projectPath + "\\src\\test\\resources\\executables\\chromedriver.exe");
			driver = new ChromeDriver();
			// driver.manage().window().maximize();
		}
		
		// else if(config.getProperty("BROWSER").equals("firefox"));
		// System.getProperty("webdriver.gekho.driver","C:\\Java\\eclipse-workspace\\Craigslist");
		// driver = new FirefoxDriver();
		
		
	}

	public static void elementClick(By locator) {
		driver.findElement(locator).click();
	}

	public static void elementSendKeys(By locator, String value) {
		driver.findElement(locator).sendKeys(value);
	}

	public static boolean elementDisplayed(By locator) {
		boolean result = driver.findElement(locator).isDisplayed();
		return result;
	}
	
	public static String getText(By locator) {
		return driver.findElement(locator).getText();
	}

	public static boolean elementEnabled(By locator) {
		return driver.findElement(locator).isEnabled();
	}

	public static boolean isClickable(By locator) {
		try {
			WebElement el = driver.findElement(locator);
			WebDriverWait wait = new WebDriverWait(driver, 9);
			wait.until(ExpectedConditions.elementToBeClickable(el));
			return true;
		} catch (Exception e) {
			return false;
		}
	}// isClickable

	@BeforeClass // login to the craislist
	public void login() throws InterruptedException {
		// 1) navigate craislist
		driver.get(config.getProperty("BASE_URL"));

		// 2) Navigate login page

		// static void elementClick(By locator)
		elementClick(By.xpath(or.getProperty("MY_ACCOUNT")));
		// driver.findElement(By.xpath("//a[text()='my account']")).click();

		// 3. Enter Email Address
		// static void elementSendKeys(By locator, String value) {
		elementSendKeys(By.xpath(or.getProperty("SEARCH_EMAIL_ADDRESS")), or.getProperty("EMAIL"));
		// driver.findElement(By.xpath("//input[@name='inputEmailHandle']")).sendKeys("islamsoniya5011@gmail.com");
		// 4. Enter the Password
		elementSendKeys(By.xpath(or.getProperty("SEARCH_PASSWORD")), or.getProperty("ENTER_PASS"));

		// driver.findElement(By.xpath("//input[@type='password']")).sendKeys("groupstudy2020$");

		// 5. click on login button
		elementClick(By.xpath(or.getProperty("LOGIN_BUTTON")));
		// driver.findElement(By.xpath("//button[@id='login']")).click();
		// 6. Go to craigslist landing page
		elementClick(By.linkText(or.getProperty("CRAIGSLIST")));
		// driver.findElement(By.xpath("//a[@href='http://www.craigslist.org']")).click();

	}

	@AfterClass // Logout form the CraigsList
	public void logout() {

		elementClick(By.xpath(or.getProperty("LOGOUT")));
		// driver.findElement(By.xpath("//a[@href='https://accounts.craigslist.org/logout?this_browser_only=1']")).click();
		System.out.println("succesfully logout");
	}
	
	@AfterSuite
	public void tearDowm() {
		driver.close();
		log.debug("TEST IS COMPLETED");
	}
	
	/*
	@DataProvider
	public Object[][] getData(){
		String sheetName = "Test_FilterSearch";
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		Object[][] data = new Object[rows-1][cols];
		for (int rowNum=2; rowNum<= rows-1; rowNum++ ) {  
			for (int colNum=0; colNum<= cols-1; colNum++ ) {
		data[rowNum-1][colNum] = excel.getCellData(sheetName, colNum, rowNum);
			}//inner fexce
		}//outer for 
		return data;
	}//public Object[][] getData()

	*/
	
}// class
