package djia_citi;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.*;

public class DowTracker {
	
    public static void main(String[] args) throws InterruptedException {

    	Queue<DowJIA> queue = new ConcurrentLinkedQueue<>();
    	
        // Path to Firefox Driver
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        //Direct path below
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        
        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        //Open window
        driver.manage().window().maximize();
        driver.get("https://finance.yahoo.com/quote/%5EDJI/");

        while(true) {
	        try {
	            // Wait for the price element to appear
	            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                    By.cssSelector("fin-streamer[data-symbol='^DJI'][data-field='regularMarketPrice']")
	            ));
	
	            String price = priceElement.getText();
	            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	            
	            queue.add(new DowJIA(time, price));
	            System.out.println(queue);
	
	        } catch (Exception e) {
	            System.out.println("Error extracting price: " + e.getMessage());
	        }
	        
	        Thread.sleep(5000);        
	       }
        
    }
    
    public static class DowJIA{
    	private String timestamp;
    	private String price;
    	
    	public DowJIA(){}
    	public DowJIA(String time, String price){
    		this.timestamp = time;
    		this.price = price;
    	}
    	
    	@Override
    	public String toString() {
    		return "Dow Jones Industrial Average{" + timestamp + ", " + price + "}";
    	}
    }
}

