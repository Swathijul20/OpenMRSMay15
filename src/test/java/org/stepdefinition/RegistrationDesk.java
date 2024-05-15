package org.stepdefinition;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

import org.junit.Assert;
import org.openmrs.pojo.AddPastVisitAndDeleteRecordsPOJO;
import org.openmrs.pojo.BmiCalculationPOJO;
import org.openmrs.pojo.DashboardPOJO;
import org.openmrs.pojo.LoginPOJO;
import org.openmrs.pojo.PatientDetailsPOJO;
import org.openmrs.pojo.PatientVisitingPOJO;
import org.openmrs.utility.HelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class RegistrationDesk extends HelperClass {

	LoginPOJO l;
	DashboardPOJO d;
	PatientDetailsPOJO p;
	PatientVisitingPOJO v;
	BmiCalculationPOJO b;
	AddPastVisitAndDeleteRecordsPOJO ad;

	@Given("To launch any browser and url of application")
	public void to_launch_any_browser_and_url_of_application() {
				
		launchBrowser("Chrome");
		launchUrl("https://qa-refapp.openmrs.org/openmrs/login.htm");
	}

	@When("To pass valid credentials in email and password field")
	public void to_pass_valid_credentials_in_email_and_password_field() {

		l = new LoginPOJO();
		passText("Admin", l.getUserName());
		passText("Admin123", l.getPassword());

	}

	@When("To pick any location and click on Login and verify user redirected to dashboard")
	public void to_pick_any_location_and_click_on_Login_and_verify_user_redirected_to_dashboard()
			throws IOException {

		clickElement(l.getRegDesk());
		clickElement(l.getLogin());
		d = new DashboardPOJO();
		// clickElement(d.getRegister());
		System.out.println(d.getLoggedInMsg().getText());
		takeScreenshot("01.LoggedIn");
		Assert.assertTrue("User not redirected to dashboard", d.getLoggedInMsg().getText().contains("Logged in"));
	}

	@When("To click patient register menu and fill all details in demographics and contact info")
	public void to_click_patient_register_menu_and_fill_all_details_in_demographics_and_contact_info()
			throws IOException, InterruptedException {

		clickElement(d.getRegister());
		p = new PatientDetailsPOJO();
		passText("Devi", p.getGivenName());
		passText("S", p.getFamilyName());
		clickElement(p.getNextButton());
		selectValueInDd(p.getGenderDropDown(), "F");
		clickElement(p.getNextButton());
		passText("25", p.getBirthDate());
		selectValueInDd(p.getBirthMonth(), "12");
		passText("2004", p.getBirthYear());
		clickElement(p.getNextButton());
		passText("Nehru Nagar", p.getAddressOne());
		passText("Adayar", p.getAddressTwo());
		passText("Chennai", p.getCity());
		passText("Tamil Nadu", p.getState());
		passText("India", p.getCountry());
		passText("600020", p.getPostalCode());
		clickElement(p.getNextButton());
		passText("9988776655", p.getPhoneNumber());
		clickElement(p.getNextButton());
		selectUsingIndex(p.getRelationship(), 7);
		passText("Keerthana", p.getPersonName());
		clickElement(p.getNextButton());
		

		takeScreenshot("02.Patient Details");

		Assert.assertEquals("Wrong Patient Name", true, p.getDetails().get(0).getText().contains("Devi, S"));
		Assert.assertEquals("Wrong Gender", true, p.getDetails().get(1).getText().contains("Female"));
		Assert.assertEquals("Wrong Birthdate", true, p.getDetails().get(2).getText().contains("25, December, 2004"));
		Assert.assertEquals("Wrong Address", true,
		p.getDetails().get(3).getText().contains("Nehru Nagar, Adayar, Chennai, Tamil Nadu, India, 600020"));
		Assert.assertEquals("Wrong PhNo", true, p.getDetails().get(4).getText().contains("9988776655"));
		Assert.assertEquals("Wrong Relatives", true, p.getDetails().get(5).getText().contains("Keerthana - Child"));

		clickElement(p.getSubmit());
		
		

		String date1String = "2004-12-25";
		String date2String = "2024-05-15";
		LocalDate date1 = LocalDate.parse(date1String);
		LocalDate date2 = LocalDate.parse(date2String);
		int ageDifference = Period.between(date1, date2).getYears();
		System.out.println("My Age is: " + ageDifference);
		if (ageDifference == Integer.parseInt(p.getAge().getText().substring(0, 2))) {
			System.out.println("Correct Age");
		} else {
			System.out.println("Incorrect Age");
		}

		takeScreenshot("03.Age Verified");
	}

	@When("To confirm start visit then attaching problems then verifying successfully attached message and ending visit")
	public void to_confirm_start_visit_then_attaching_problems_then_verifying_successfully_attached_message_and_ending_visit()
			throws Exception {

		v = new PatientVisitingPOJO();
		clickElement(v.getStartVisit());
		clickElement(v.getConfirm());
		clickElement(v.getAttachment());
		clickElement(v.getAttachFile());
		Thread.sleep(15000);
		passText("I have Neuro problem", v.getCaption());
		clickElement(v.getUploadFile());
		
		clickElement(v.getProfileRedirection());
		Assert.assertTrue("File not attached", v.getFileDisplaying().isDisplayed());
		Assert.assertEquals("More than one entry added", 1, v.getRecentEntry().size());
		takeScreenshot("04.File Attached");
		clickElement(v.getEndVisit());
		clickElement(v.getYesButton());
	}

	@When("To visit again for body bmi calculation")
	public void to_visit_again_for_body_bmi_calculation() throws IOException, InterruptedException {

		driver.navigate().refresh();
		b = new BmiCalculationPOJO();
		clickElement(b.getStartVis());
		clickElement(b.getConfirm());
		clickElement(b.getCaptureVitals());
		passText("158.5", b.getHeight());
		String height = b.getHeight().getAttribute("value");
		System.out.println(height);
		clickElement(b.getNextButton());
		passText("100", b.getWeight());
		String weight = b.getWeight().getAttribute("value");
		System.out.println(weight);
		clickElement(b.getNextButton());
		System.out.println(b.getBmiCalculated().getText());
		clickElement(b.getSaveForm());
		clickElement(b.getSave());
		clickElement(b.getEndVisit());
		clickElement(b.getYes());
		driver.navigate().refresh();
		clickElement(b.getRedirect());

		Assert.assertEquals("Wrong Height Displaying: ", height, b.getVerifyHeight().getText());
		Assert.assertEquals("Wrong Weight Displaying: ", weight, b.getVerifyWeight().getText());
		Assert.assertTrue("Vital not displayed: ", b.getVerifyVitals().isDisplayed());
		takeScreenshot("05.BMI calculated");
		takeScreenshot("06.Vital Attached");


		clickElement(b.getMergeVisit());

		for (int i = 0; i < b.getEnableMergeCheckbox().size(); i++) {

			WebElement checkBox = b.getEnableMergeCheckbox().get(i);
			checkBox.click();
		}

		clickElement(b.getMerge());
		clickElement(b.getReturnToDetailsPage());
		Assert.assertEquals("Vital Not Attached: ", 1, b.getVitalAttachmentUploaded().size());

		takeScreenshot("07.Vital And Attachment Merged");

	}

	@When("To add past visit and delete the records")
	public void to_add_past_visit_and_delete_the_records() throws AWTException, IOException, InterruptedException {

		ad = new AddPastVisitAndDeleteRecordsPOJO();
		clickElement(ad.getAddPastVisit());
		Assert.assertTrue("Future date is clickable: ", ad.getFutureDate().isEnabled());
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ESCAPE);
		r.keyRelease(KeyEvent.VK_ESCAPE);
		
		
		String patientId = driver.findElement(By.xpath("//em[text()='Patient ID']//following-sibling::span")).getText();
		clickElement(ad.getDeletePatient());
		passText("Not available", ad.getReason());
		clickElement(ad.getContinueBtn());
		Assert.assertTrue("Patient record page is not displayed", ad.getPatientRecordPage().isDisplayed());
		
		driver.findElement(By.xpath("//input[@id='patient-search']")).sendKeys(patientId);
		Thread.sleep(2000);
		takeScreenshot("08.Patient Record Deleted'");
		
	}
}
