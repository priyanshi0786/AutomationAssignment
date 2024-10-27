package com.Test;

import java.text.ParseException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Pages.FlightSearchPage;

public class FlightSearchTest {

	 private ChromeDriver cd;
	 private FlightSearchPage flightSearchPage;
	 
	 @BeforeClass
	    public void initialisingDriver() {
	        
		 cd = new ChromeDriver();
		    cd.manage().window().maximize();
	        flightSearchPage = new FlightSearchPage(cd);
	    }
	 
	 @DataProvider(name = "travelDates")
	    public Object[][] travelDates() {
	        return new Object[][]{
	                {"today"},
	                {"tomorrow"}
	        };
	    }
	 
	 @Test(dataProvider = "travelDates")
	    public void testFlightSearchDELToBOM(String date) throws ParseException, InterruptedException {
		 	flightSearchPage.getTheURL();
		 	flightSearchPage.selectTrip("ONEWAY");
	        flightSearchPage.enterOrigin("DEL");
	        flightSearchPage.enterDestination("BOM","AIRPORTBOM");
	        flightSearchPage.selectTravelDate(date);
	        flightSearchPage.clickSearch("button_search_submit");

	        Assert.assertTrue(flightSearchPage.areResultsDisplayed(), 
	                          "Flight results not displayed for " + date);
	    }
	 
	 @Test
	    public void testSameOriginAndDestination() throws InterruptedException {
	        cd.get("https://www.booking.com/flights/index.en-gb.html");

	        flightSearchPage.enterOrigin("DEL");
	        flightSearchPage.enterDestination("DEL","AIRPORTDEL"); // Same destination

	        flightSearchPage.clickSearch("button_search_submit");

	        // Assert the error message for invalid input
	        String errorMessage = flightSearchPage.getErrorMessage();
	        Assert.assertTrue(errorMessage.contains("Origin and destination can't be the same"), 
	                          "Error message not shown for same origin and destination.");
	    }

	    @Test
	    public void testEmptyFieldsValidation() throws InterruptedException {
	        cd.get("https://www.booking.com/flights/index.en-gb.html");

	        flightSearchPage.clickSearch("button_search_submit"); // Leave fields empty

	        // Verify error message for empty fields
	        String errorMessage = flightSearchPage.getErrorMessage();
	        Assert.assertTrue(errorMessage.contains("Add an airport or city"), 
	                          "Error message not shown for empty fields.");
	    }

	    @AfterClass
	    public void tearDown() {
	        if (cd != null) {
	            cd.quit();
	        }
	    }
}
