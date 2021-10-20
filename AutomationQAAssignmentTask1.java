package Assignment;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AutomationQAAssignmentTask1 {
	public static WebDriver driver;
	public static JavascriptExecutor js;
	public static WebDriverWait wait;
	public static String parentId;
	public static String childId;
	public static String Laptop;
	public static String Phone;

	public static void main(String[] args) throws InterruptedException {
		launchBrowser();
		loginToFlipkart();
		searchHPLaptop();
		handleChildWindow();
		addItemToCart();
		closeChildWindow();
		clearSearchBar();
		addItemMobile();
		handleChildWindow();
		addItemToCart();
		removeOldItem();
		checkForLastAddedProduct();
		switchToParentWindow();
		logoutFromFlipkart();

	}

	public static void launchBrowser() {

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\191441\\Desktop\\Automation\\Selenium\\chromedriver_win32\\chromedriver_win32"
						+ "\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, 10);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();

	}

	// Login to Flipkart
	public static void loginToFlipkart() {

		driver.get("https://www.flipkart.com/");
		driver.findElement(By.xpath("//span[@class='_36T8XR']/../input")).sendKeys("****");
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("****");
		wait.until(
				ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@class='_1D1L_j']/button"))))
				.click();

	}

	// In search bar, look for HP laptop
	public static void searchHPLaptop() throws InterruptedException {

		WebElement SearchBar = driver.findElement(By.cssSelector("input[class='_3704LK']"));
		wait.until(ExpectedConditions.elementToBeClickable(SearchBar));
		SearchBar.sendKeys("HP Laptop");
		SearchBar.sendKeys(Keys.ENTER);
		Laptop = driver.findElement(By.xpath("//div[@class='_4rR01T']")).getText();
		System.out.println(Laptop);

		// Once the search items are displayed, pick any one item from the list
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='_1fQZEK']"))).click();

	}

	// Handle the child window
	public static void handleChildWindow() {
		Set<String> windowID = driver.getWindowHandles();
		Iterator<String> it = windowID.iterator();
		parentId = it.next();
		childId = it.next();
		driver.switchTo().window(childId);
	}

	// Add that item to cart
	public static void addItemToCart() throws InterruptedException {
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='row']/li/button"))).click();
		Thread.sleep(1000);
	}

	// Close the child window / Get back to parent window
	public static void closeChildWindow() {

		driver.close();
		driver.switchTo().window(parentId);

	}

	// Clear search bar
	public static void clearSearchBar() {
		WebElement SearchBar1 = driver.findElement(By.xpath("//input[@class='_3704LK']"));
		wait.until(ExpectedConditions.visibilityOf(SearchBar1));
		SearchBar1.sendKeys(Keys.CONTROL + "a");
		SearchBar1.sendKeys(Keys.DELETE);
	}

	// Search another item - any mobile
	public static void addItemMobile() {
		WebElement SearchBar1 = driver.findElement(By.xpath("//input[@class='_3704LK']"));
		SearchBar1.sendKeys("IPhone 12");
		driver.findElement(By.xpath("//button[@class='L0Z3Pu']")).click();
		// Once the search items are displayed, pick any one random item from the list

		driver.navigate().refresh();
		Phone = driver.findElement(By.xpath("//div[@class='_4rR01T']")).getText();
		WebElement ele = driver.findElement(By.xpath("//div[@class='_2kHMtA']/a"));
		wait.until(ExpectedConditions.elementToBeClickable(ele)).click();
	}

	// Remove the old item(hp laptop) from the cart
	public static void removeOldItem() {

		wait.until(ExpectedConditions.titleContains("Shopping Cart"));
		List<WebElement> cartProducts = driver
				.findElements(By.xpath("//div[@class='_1Er18h']/div/div/div/div/div/div/div/div/div/a"));
		for (int i = 0; i < cartProducts.size(); i++) {
			if (cartProducts.get(i).getText().contains("HP")) {
				WebElement removeButton = driver.findElement(By.xpath(
						"//div[@class='_1Er18h']/div/div/div/div/div[" + (i + 2) + "]/div/" + "div[2]/div[2]/div[2]"));
				js.executeScript("arguments[0].click()", removeButton);
				WebElement removePopup = driver
						.findElement(By.xpath("//div[@id='container']/div/div/div/div/div/div[2]"));
				wait.until(ExpectedConditions.elementToBeClickable(removePopup)).click();
				System.out.println("HP Laptop removed from the cart successfully");
			}
		}
	}

	// Validate if the last added item is in the cart
	public static void checkForLastAddedProduct() {
		List<WebElement> cartProductsNew = driver
				.findElements(By.xpath("//div[@class='_1Er18h']/div/div/div/div/div/div/div/div/div/a"));
		for (int i = 0; i < cartProductsNew.size(); i++) {
			if (cartProductsNew.get(i).getText().equalsIgnoreCase(Phone)) {
				System.out.println("Last added item - IPhone is present in the cart");
			}
		}
	}

	// Get back to parent window
	public static void switchToParentWindow() {
		driver.switchTo().window(parentId);
	}

	// Logout
	public static void logoutFromFlipkart() {
		Actions a = new Actions(driver);
		a.moveToElement(driver.findElement(By.xpath("//div[text()='My Account']"))).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Logout']"))).click();
	}

}
