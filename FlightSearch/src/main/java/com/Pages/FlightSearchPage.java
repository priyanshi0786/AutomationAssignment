package com.Pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlightSearchPage {
	
	private WebDriver wd;
	private WebDriverWait wait;
	
	private String flightFunctionality = "//button[@data-ui-name='%b']";
	private By inputField = By.xpath("//input[@placeholder='Airport or city']");
	private By clearbutton = By.xpath("//span[contains(@class,'Tags-module__icon')]");
	private By flightResults = By.xpath("//div[contains(@id,'flight-card')]");
    private By errorMessage = By.xpath("//div[contains(@class,'ErrorMessage-module__root')]");
    private String url = "https://www.booking.com/flights/index.en-gb.html";
    private String tripSelection = "//input[@value='%t']";
    private By calendarBtn = By.xpath("//button[contains(@class,'Calendar-module__control--prev')]");
	
	public FlightSearchPage(WebDriver driver) {
        this.wd = driver;
//        this.wait = new WebDriverWait(driver,15);
    }
	
	public FlightSearchPage getTheURL(){
        wd.get(url);
        return this;

    }
	
	public FlightSearchPage selectTrip(String trip) {
		String tripSelectionFinal = tripSelection.replace("%t", "ONEWAY");
		By selectedTrip = By.xpath(tripSelectionFinal);
		wd.findElement(selectedTrip).click();
		return this;
	}
	
	public FlightSearchPage enterOrigin(String origin) throws InterruptedException {
		String originNameFinal = flightFunctionality.replace("%b", "input_location_from_segment_0");
		By originInput = By.xpath(originNameFinal);
		wd.findElement(originInput).click();
		Thread.sleep(1500);
		wd.findElement(clearbutton).click();
		Thread.sleep(2000);
//		wd.findElement(originInput).click();
//		Thread.sleep(3000);
		wd.findElement(inputField).sendKeys(origin);
		Thread.sleep(3000);
		wd.findElement(By.xpath("//input[@name='AIRPORTDEL']")).click();
		return this;
	}
	
	public FlightSearchPage enterDestination(String destination,String destinationName) throws InterruptedException {
		String destinationNameFinal = flightFunctionality.replace("%b", "input_location_to_segment_0");
		By destinationInput = By.xpath(destinationNameFinal);
		wd.findElement(destinationInput).click();
		Thread.sleep(2000);
		wd.findElement(inputField).sendKeys(destination);
		Thread.sleep(3000);
		wd.findElement(By.xpath("//input[@name='"+destinationName+"']")).click();
		return this;
	}
	
	public void selectTravelDate(String date) throws ParseException, InterruptedException {
		String dateFinal = flightFunctionality.replace("%b", "button_date_segment_0");
		By dateInput = By.xpath(dateFinal);
		String formattedDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        WebElement dateField = wd.findElement(dateInput);
        dateField.click();
        Thread.sleep(2000);
        wd.findElement(calendarBtn).click();
        Thread.sleep(1500);
        if (date.equalsIgnoreCase("today")) {
            formattedDate = LocalDate.now().format(formatter);
        } else if (date.equalsIgnoreCase("tomorrow")) {
            formattedDate = LocalDate.now().plusDays(1).format(formatter);
        }else {
            try {
                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
                LocalDate parsedDate = LocalDate.parse(date, inputFormat);
                formattedDate = parsedDate.format(formatter);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Invalid date format. Please use 'dd-MM-yyyy'");
            }
        }
        System.out.println("daetttt+ "+formattedDate);
        WebElement dateElement = wd.findElement(By.xpath("//td/span[@data-date='" + formattedDate + "']"));
        dateElement.click();
    }
	
	public void clickSearch(String button) throws InterruptedException {
		String buttonNameFinal = flightFunctionality.replace("%b", button);
		By searchBtn = By.xpath(buttonNameFinal);
		wd.findElement(searchBtn).click();
		Thread.sleep(4000);
    }
	
	public boolean areResultsDisplayed() {
		return wd.findElement(flightResults).isDisplayed();
    }
	
	public String getErrorMessage() {
		return wd.findElement(errorMessage).getText();
    }
	

}
