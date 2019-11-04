package org.iomedia.galen.pages;

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.iomedia.common.BaseUtil;
import org.iomedia.framework.Driver.HashMapNew;
import org.iomedia.framework.Reporting;
import org.iomedia.framework.WebDriverFactory;
import org.iomedia.galen.common.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NamMe extends BaseUtil {
	public Reporting Reporter;
	private String objName = "";
	private String tagVersion = null;
	private int majorVersion = 0;
	private int minorVersion = 0;
	private int maintenanceVersion = 0;

	enum WidgetType {
		Welcome_Carousel, Feature_Set, Image_Gallery, Content_Box, Typeform, PDF_Document, Video_Player, Event_Listing,
		Table
	}

	private SuperAdminPanel adminPanel;

	public NamMe(Utils base, WebDriverFactory driverFactory, HashMapNew Dictionary, HashMapNew Environment,
			Reporting Reporter, org.iomedia.framework.Assert Assert, org.iomedia.framework.SoftAssert SoftAssert,
			ThreadLocal<HashMapNew>[] sTestDetails, SuperAdminPanel adminPanel) {
		super(driverFactory, Dictionary, Environment, Reporter, Assert, SoftAssert, sTestDetails);
		this.adminPanel = adminPanel;
		this.Reporter = Reporter == null
				? (driverFactory.getReporting() == null ? null : driverFactory.getReporting().get())
				: Reporter;
	}

	private By getWidgetBy(String widgetName) {
		return By.xpath("//img[@alt='" + widgetName + "']");
	}

	private By gridOverlay = By.id("gridOverlay");

	// @formatter:off
	private By editPopup(WidgetType widgetType, boolean isContentSettingDialog) {
		By editPopup = null;
		String styleXpath = "contains(@style,'left: 0px;')";
		if (!isContentSettingDialog)
			switch (widgetType) {
			case Welcome_Carousel:
			case Feature_Set:
			case Image_Gallery:
				editPopup = By.xpath(
						"//div[contains(@class,'_-src-widgets-common-UI-layoutDialog-_styles_chooseLayoutDialog') and "
								+ styleXpath + "]");
				break;
			case Content_Box:
				editPopup = By.xpath("//div[contains(@class,'_-src-widgets-content-box-_style_editCountdownModal') and "
						+ styleXpath + "]");
				break;
			case Typeform:
				editPopup = By.xpath(
						"//div[contains(@class,'_-src-widgets-common-_styles_editModal') and " + styleXpath + "]");
				break;
			case PDF_Document:
				editPopup = By.xpath("//div[contains(@class,'_-src-widgets-common-_styles_editCountdownModal') and "
						+ styleXpath + "]");
				break;
			case Video_Player:
				editPopup = By.xpath(
						"//div[contains(@class,'_-src-widgets-video-streamer-_style_editCountdownModal') and "
								+ styleXpath + "]");
				break;
			case Event_Listing:
				editPopup = By.xpath("//div[contains(@class,'src-widgets-common-UI-layoutDialog-_styles_eventListingDialog') and "  
							+ styleXpath + "]");
				break;
			case Table:
				editPopup = By.xpath("");
				break;
			}
		else {
			switch (widgetType) {
			case Welcome_Carousel:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-carousel-widget-content-settings-_component-content-setting_dialogWrapper')])[last()]");
				break;
			case Feature_Set:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-featureBox-_style_editCountdownModal')])[last()]");
				break;
			case Image_Gallery:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-image-gallery-_style_editCountdownModal')])[last()]");
				break;
			case Content_Box:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-content-box-_style_editCountdownModal')])[last()]");
				break;
			case PDF_Document:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-common-_styles_editCountdownModal')])[last()]");
				break;
			case Typeform:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-common-_styles_editModal')])[last()]");
				break;
			case Video_Player:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-video-streamer-_style_editCountdownModal')])[last()]");
				break;
			case Event_Listing:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-events-listing-_style_editCountdownModal')])[last()]");
				break;
			case Table:
				editPopup = By.xpath("(//div[contains(@class,'_-src-widgets-table-_style_editCountdownModal')])[last()]");
				break;
			}
		}

		return editPopup;
	}

	// Welcome Carousel Objects
	private By selectText = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, false)) + "//following::div[text()='Text']");
	private By applyButton = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, false)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.Feature_Set, false)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, false)) + "//following::button[.='Apply']"  );
	private By cancelButton = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::button[.='Cancel']");
	
	private By slide = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::ul[contains(@class,'_-src-widgets-carousel-widget-content-settings-_component-content-setting_contentSettingWrapper')]/li");
	private By addSlide = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::button[.='Add Slide']");
	private By maximumSlideMessage = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::p");
	
	private By colorBlock = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::div[contains(@class,'_-src-widgets-common-_styles_colorBlock')]");
	private By selectRandomColor = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::div[contains(@class,'saturation-white')] | " + getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::div[contains(@class,'saturation-white')] | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::div[contains(@class,'saturation-white')] | " + getLocatorValue(editPopup(WidgetType.Content_Box, true)) + "//following::div[contains(@class,'saturation-white')] | " + getLocatorValue(editPopup(WidgetType.PDF_Document, true)) + "//following::div[contains(@class,'saturation-white')] | " + getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::div[contains(@class,'saturation-white')]");
	private By contentAlignment = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::ul[contains(@class,'_-src-widgets-common-_styles_alignmentOptions')]/li");
	private By deleteIcon = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::div[contains(@class,'deleteIcon')] | " + getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::div[contains(@class,'deleteIcon')]" );
	private By backButton = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//div[contains(@class,'backButton')] | " + getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//div[contains(@class,'backButton')]");
	private By editApplyButton = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.Content_Box, true)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.Typeform, true)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.PDF_Document, true)) + "//following::button[.='Apply'] | " + getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::button[.='Apply']");
	private By editCancelButton = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::button[.='Cancel'] | " + getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::button[.='Cancel'] | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::button[.='Cancel'] | " + getLocatorValue(editPopup(WidgetType.Content_Box, true)) + "//following::button[.='Cancel'] | " + getLocatorValue(editPopup(WidgetType.Typeform, true)) + "//following::button[.='Cancel'] | " + getLocatorValue(editPopup(WidgetType.PDF_Document, true)) + "//following::button[.='Cancel'] | " + getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::button[.='Cancel']");
	private By buttonSettings = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::button[.='Button Settings']");
	private By getToggleButton(WidgetType widgetType, String buttonType) {
		return By.xpath("(" + getLocatorValue(editPopup(widgetType, true)) + "//*[text()='" + buttonType + "']//following::input[@type='checkbox'])[1]");
	} 
	private By getbuttonNameField(WidgetType widgetType) {
		return By.xpath(getLocatorValue(editPopup(widgetType, true)) + "//following::label[text()='Button Name']/following-sibling::input");	
	}  
	private By getButtonStyle(WidgetType widgetType) {
		return By.xpath("(" + getLocatorValue(editPopup(widgetType, true)) + "//following::label[text()='Button Style']/following-sibling::div//following::button)[1]");
	}  
	private By getButtontype(WidgetType widgetType) {
		return By.xpath(getLocatorValue(editPopup(widgetType, true)) + "//following::*[@role='menu']/div");
	}
	private By getButtonSize(WidgetType widgetType) {
		return By.xpath(getLocatorValue(editPopup(widgetType, true)) + "//following::label[text()='Button Size']//following::ul[contains(@class,'alignmentOptions')]/li");
	}
	private By secondaryButton = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::li[text()='Secondary Button']");
	private By getErrorMessage(WidgetType widgetType) {
		By errorMessage = null;
		switch (widgetType) {
		
		case Welcome_Carousel:
			errorMessage = By.xpath(getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//div[contains(@class,'src-widgets-common-UI-errorToast-_styles_errorToastContainer')]//span");
			break;
		
		case Feature_Set:
			errorMessage = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::div[contains(@class,'src-widgets-common-UI-errorToast-_styles_errorToastContainer')]//span");
			break;
			
		case Typeform:
			errorMessage = By.xpath(getLocatorValue(editPopup(WidgetType.Typeform, true)) + "//div[contains(@class,'src-widgets-common-UI-errorToast-_styles_errorToastContainer')]//span");
			break;
			
		case Video_Player:
			errorMessage = By.xpath(getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//div[contains(@class,'src-widgets-common-UI-errorToast-_styles_errorToastContainer')]//span");
			break;

		default:
			break;
		}
		return errorMessage;
	}
	
	//Feature Set Objects
	private By selectTile = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, false)) + "//following::div[text()='Tile']");
	private By maximumFeatureTileMessage = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::p[contains(text(),'Feature Tiles')]");
	private By featureTiles = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::ul[contains(@class,'_-src-widgets-featureBox-_style_sortlistContainer')]/li");
	private By addNewFeatureTile = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::p[.='Add New Feature Tile']");
	private By getImageSourceBy(WidgetType widgetType, String type) {
		By imageSource = null;
		switch (widgetType) {
		case Feature_Set:
			imageSource = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::input[@type='radio' and following-sibling::div[.='" + type + "']]");
			break;

		case Video_Player:
			imageSource = By.xpath(getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::input[@type='radio' and following-sibling::div[.='" + type + "']]");
			break;
			
		default:
			break;
		}
		return imageSource;
	}
	private By chooseIconDropdown = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::p[contains(@class,'_-src-widgets-common-_styles_fontSelectorInput')]");
	private By selectIconFromList = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::div[@class='_-src-widgets-common-_styles_fontIconPanel_wKhq3P']//div[@title]");
	private By featureText = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='Feature Text']/following-sibling::input");
	private By buttonText = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='Button Text']/following-sibling::input");
	private By settingButton = By.xpath("//li[.='Button Settings']");
	private By buttonURL = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='Button URL']/following-sibling::input");
	private By newTab = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='New Tab']//following::input[@type='checkbox'] | "+ getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='New tab']//following-sibling::div");
	private By style = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::button[.='Style'] | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::button[.='Style'] | " + getLocatorValue(editPopup(WidgetType.PDF_Document, true)) + "//following::button[.='Style'] | " + getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::button[.='Style']" );
	private By getColorXpath(String label) {
		return By.xpath("("+getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='" + label + "']//following::div[contains(@class,'_-src-widgets-common-_styles_colorBlock')] | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::label[text()='" + label + "']//following::div[contains(@class,'_-src-widgets-common-_styles_colorBlock')] | " + getLocatorValue(editPopup(WidgetType.Welcome_Carousel, true)) + "//following::label[text()='" + label + "']//following::div[contains(@class,'_-src-widgets-common-_styles_colorBlock')] | " + getLocatorValue(editPopup(WidgetType.Content_Box, true)) + "//following::label[text()='" + label + "']//following::div[contains(@class,'_-src-widgets-common-_styles_colorBlock')] | " + getLocatorValue(editPopup(WidgetType.PDF_Document, true)) + "//following::label[text()='" + label + "']//following::div[contains(@class,'_-src-widgets-common-_styles_colorBlock')] | " + getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::label[text()='" + label + "']//following::div[contains(@class,'_-src-widgets-common-_styles_colorBlock')] )[1]");
	}
	private By headerSize = By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='Heading size']//following::input/preceding-sibling::div | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::label[text()='Heading size']//following::input/preceding-sibling::div | " + getLocatorValue(editPopup(WidgetType.PDF_Document, true)) + "//following::label[text()='Heading size']//following::input/preceding-sibling::div | " + getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::label[text()='Title Text Size']//following::input/preceding-sibling::div");
	private By getBackground(String type) {
		return By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true)) + "//following::label[text()='Background: ']//following::input[@value='" + type + "'] | " + getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::label[text()='Background: ']//following::input[@value='" + type + "'] | " + getLocatorValue(editPopup(WidgetType.Content_Box, true)) + "//following::label[text()='Background: ']//following::input[@value='" + type + "'] | " + getLocatorValue(editPopup(WidgetType.PDF_Document, true)) + "//following::label[text()='Background']//following::input[@value='" + type + "'] | " + getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::label[text()='Background:']//following::input[@value='" + type + "']");
	}
	
	// Image Gallery Objects
	private By select3X2SmallImages = By.xpath(getLocatorValue(editPopup(WidgetType.Image_Gallery, false)) + "//following::div[text()='3X2 Small Images']");
	private By imageGalleryTitle = By.xpath(getLocatorValue(editPopup(WidgetType.Image_Gallery, true)) + "//following::label[text()='Image Gallery Title']/following-sibling::input");
	
	// Content Box Objects
	private By editContentBox = By.xpath("//div[contains(@class,'_-src-widgets-content-box-_style_froalaWrapper')]//following::div[@class='fr-element fr-view'][last()]");
	
	// Typeform Objects
	private By selectTypeform = By.xpath(getLocatorValue(editPopup(WidgetType.Typeform, true)) + "//following::button[.='Select Typeform']");
	private By selectTypeformPopup = By.xpath(getLocatorValue(editPopup(WidgetType.Typeform, true)) + "//following::div[contains(@class,'_-src-widgets-typeform-_index_TypeformPopup') and contains(@style,'left: 0px;')]");
	private By allowMultipleSubmission = By.xpath(getLocatorValue(editPopup(WidgetType.Typeform, true)) + "//following::label[text()='Allow multiple submissions for this survey']/preceding::input[@type='checkbox' and @value='']");
	
	// Video Player Objects
	private By videoTitle = By.xpath(getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::label[text()='Video Title']/following-sibling::input");
	private By videoURL = By.xpath(getLocatorValue(editPopup(WidgetType.Video_Player, true)) + "//following::label[text()='Video URL']/following-sibling::input");
	
	
	private By publish = By.xpath("//button[contains(@class,'styles-savePublish')] | (//button[contains(@class,'styles-save')])[1]");
	private By popup = By.xpath("//div[contains(@class,'styles-modalTitle')]");
	private By Continue = By.xpath("//button[contains(@class,'styles-save') and text()='Continue']");
	private By viewPageTableRows = By.xpath("//table[@class='table']//tr[td[contains(text(),'TestAutomation')]]");
	
	private By deletePDF = By.xpath("(//span[text()='PDF Document']//following::*[contains(@class,'_-src-widgets-common-_styles_deleteWidgetIcon')])[1]");
	private By deleteTypeform = By.xpath("(//span[text()='Edit Typeform']//following::*[contains(@class,'_-src-widgets-common-_styles_deleteWidgetIcon')])[1]");
	private By uploadFile = By.xpath("((//div[contains(@class,'_-src-widgets-common-_styles_editCountdownModal')])[last()]//following::input[@type='file'])[1]");
	private By loader = By.xpath("//div[contains(@class,'styles-loaderCircle')]");
	
	// Table Widget Objects
	private By editTableContainer = By.xpath("(//div[contains(@class,'src-widgets-table-_style_carouselContainer')])[last()]");
	private By table = By.xpath(getLocatorValue(editTableContainer) + "//table");
	private By getTableField(int row, int column) {
		return By.xpath("((" + getLocatorValue(table) + "//tr)[" + row + "]//*[self::th or self::td])[" + column + "]");
	}
	private By editTableField(int row, int column) {
		return By.xpath(getLocatorValue(getTableField(row, column)) + "//div[contains(@class,'src-widgets-table-_style_tripleDot')]");
	}
	private By editStyleInCell_Top(int row, int column) {
		return By.xpath(getLocatorValue(getTableField(row, column)) + "//div[contains(@class,'src-widgets-table-_style_topRow')]");
	}
	private By editStyleInCell_Bottom(int row, int column) {
		return By.xpath(getLocatorValue(getTableField(row, column)) + "//div[contains(@class,'src-widgets-table-_style_bottomRow')]");
	}
	
	
	
	// UI Elements
	// Welcome Carousel
	private By getPrimaryButton_UI(WidgetType widgetType, int slideNumber) {
		slideNumber = slideNumber+1;
		String primaryButton_UI_Xpath = "//div[contains(@class,'src-widgets-carousel-_style_primaryButton') and ancestor::*[contains(@class,'styles_widgetContainer')]  and ancestor::*[contains(@class,'slick-active slick-current')]]/a "; 
		switch (widgetType) {
		case Welcome_Carousel: 
			primaryButton_UI_Xpath = "//div[contains(@class,'src-widgets-carousel-_style_primaryButton') and ancestor::*[contains(@class,'styles_widgetContainer')]  and ancestor::*[contains(@class,'slick-active slick-current')]]/a ";
			break;

		case Feature_Set:
			primaryButton_UI_Xpath = "(//*[contains(@class,'src-widgets-featureBox-_style_widgetBlockContainer')]//a[contains(@class,'src-widgets-featureBox-_style_featureBoxButton') and ancestor::*[contains(@class,'style_featureBoxContainer')]])[" + slideNumber + "]";
			break;
			
		default:
			break;
		}
		return By.xpath(primaryButton_UI_Xpath);
	} 
	private By secondaryButton_UI = By.xpath("//div[contains(@class,'src-widgets-carousel-_style_secondaryButton') and ancestor::*[contains(@class,'styles_widgetContainer')] and ancestor::*[contains(@class,'slick-active slick-current')]]/a");
	
	// Common Elements for UI Verification on Pulished Site
	private By getWidgetParent(WidgetType widgetType) {
		By widgetParent = null;
		switch (widgetType) {
		case Welcome_Carousel:
			widgetParent = By.xpath("(//div[contains(@class,'src-widgets-common-_styles_carouselContainer')])[1]");
			break;

		case Feature_Set:
			widgetParent = By.xpath("(//div[contains(@class,'src-widgets-featureBox-_style_featureBoxContainer')])[1]");
			break;
		case Image_Gallery:
			widgetParent = By.xpath("(//div[contains(@class,'src-widgets-image-gallery-_style_widgetContainer')])[1]");
			break;
		case Content_Box:
			widgetParent = By.xpath("");
			break;
		case PDF_Document:
			widgetParent = By.xpath("");
			break;
		case Typeform:
			widgetParent = By.xpath("");
			break;
		case Video_Player:
			widgetParent = By.xpath("");
			break;
		}
		
		return widgetParent;
	}
	//@formatter:on

	public void dragAndDropWidget(String widgetName) throws IOException {
		getTagVersion();
		String editMessage = "******************* Editing " + widgetName.toUpperCase() + " *******************";
		Reporter.log("****" + editMessage + "****", editMessage, editMessage, "Pass");
		sync(2000l);

		adminPanel.dragAndDrop(getElementWhenPresent(getWidgetBy(widgetName)), getElementWhenPresent(gridOverlay));

		sync(2000l);

		Assert.assertTrue(checkElementPresent(editPopup(getWidgetType(widgetName), false), 20),
				widgetName + " is successfully added[Drag And Drop] to GridOverlay");
	}

	public void editWidget(String widgetName) throws Exception {
		if (getWidgetType(widgetName).equals(WidgetType.Welcome_Carousel)) {
			editWelcomeCarousel();
		} else if (getWidgetType(widgetName).equals(WidgetType.Feature_Set)) {
			editFeatureSet();
		} else if (getWidgetType(widgetName).equals(WidgetType.Image_Gallery)) {
			editImageGallery();
		} else if (getWidgetType(widgetName).equals(WidgetType.Content_Box)) {
			editContentBox();
		} else if (getWidgetType(widgetName).equals(WidgetType.Typeform)) {
			editTypeform();
		} else if (getWidgetType(widgetName).equals(WidgetType.PDF_Document)) {
			editPdfDocument();
		} else if (getWidgetType(widgetName).equals(WidgetType.Video_Player)) {
			editVideoPlayer();
		} else if (getWidgetType(widgetName).equals(WidgetType.Event_Listing)) {
			editEventListing();
		} else if (getWidgetType(widgetName).equals(WidgetType.Table)) {
			editTableWidget();
		}
	}

	private void editTableWidget() throws Exception {
		List<WebElement> rows = getElementWhenPresent(table).findElements(By.xpath(".//tr"));
		List<WebElement> columns = getElementWhenPresent(table).findElements(By.xpath(".//tr")).get(0)
				.findElements(By.xpath("./th"));

		typeTextInTableField(1, 2, "");

	}

	private void typeTextInTableField(int row, int column, String text) throws Exception {
		click(By.xpath(getLocatorValue(getTableField(row, column))
				+ "//div[contains(@class,'src-widgets-table-_style_hideOverflow')]"),
				"Edit field [" + row + "" + column + "]");

		type(By.xpath(
				getLocatorValue(getTableField(row, column))
						+ "//div[contains(@class,'src-widgets-table-_style_hideOverflow')]"),
				"Edit field [" + row + "" + column + "]", text);

	}

	private void editEventListing() {

	}

	private void editWelcomeCarousel() throws Exception {
		click(selectText, "Select Text Change Layout");
		click(applyButton, "Apply Button");

		Assert.assertTrue(checkElementPresent(editPopup(WidgetType.Welcome_Carousel, true)),
				"Edit " + WidgetType.Welcome_Carousel + " is present");

		Assert.assertEquals(getText(maximumSlideMessage), "You can add a maximum of 4 slides",
				"Verify Message Present:- You can add a maximum of 4 slides");

		deleteAllSlides();
		click(editApplyButton, "Apply Button");
		sync(500l);
		Assert.assertEquals(getText(getErrorMessage(WidgetType.Welcome_Carousel)), "At least 1 slide is required",
				"Verified Error Message if No Slide Is Added");

		clickByMouseEvent(getElementWhenPresent(getErrorMessage(WidgetType.Welcome_Carousel))
				.findElement(By.xpath("./preceding-sibling::*[contains(@class,'closeIcon')]")));

		int slideCount = getDriver().findElements(slide).size();
		while (slideCount < 4) {
			click(addSlide, "Add Slide");
			sync(200l);
			slideCount = getDriver().findElements(slide).size();
		}
		slideCount = getDriver().findElements(slide).size();
		Assert.assertTrue(slideCount == 4, "4 Slides are added");
		Assert.assertEquals(checkElementPresent(addSlide, 5), false, "Add Slide Button has disappeared");

		// click Slide 1
		List<WebElement> slides = getDriver().findElements(slide);

		for (int i = 0; i < slides.size(); i++) {
			getDriver().findElements(slide).get(i).click();

			click(colorBlock, "Select Color Block");
			selectRandomColor();
			click(colorBlock, "Close Color Block");

			Random random = new Random();
			int x = random.nextInt(3);
			List<WebElement> contentAlignments = getDriver().findElements(contentAlignment);
			click(contentAlignments.get(x), contentAlignments.get(x).getText());

			Assert.assertTrue(checkElementPresent(buttonSettings), "Button Setting Is Present On Edit Content Page");
			Assert.assertTrue(checkElementPresent(editApplyButton), "Apply Button Is Present On Edit Content Page");
			Assert.assertTrue(checkElementPresent(cancelButton), "Cancel Button Is Present On Edit Content Page");
			Assert.assertTrue(checkElementPresent(deleteIcon), "Delete Icon Is Present On Edit Content Page");

			click(buttonSettings, "Button Settings");
			toggleButton(WidgetType.Welcome_Carousel, "Primary Button", 0);

			click(secondaryButton, "Secondary Button");
			toggleButton(WidgetType.Welcome_Carousel, "Secondary Button", 0);

			click(backButton, "Back Button");
			click(backButton, "Back Button");

		}
		click(editApplyButton, "Apply Button");
	}

	private void editFeatureSet() throws Exception {
		click(selectTile, "Select Tile Change Layout");
		click(applyButton, "Apply Button");

		Assert.assertTrue(checkElementPresent(editPopup(WidgetType.Feature_Set, true)),
				"Edit " + WidgetType.Feature_Set + " is present");

		Assert.assertEquals(getText(maximumFeatureTileMessage),
				"*You can add a maximum of 4 Feature Tiles with this Layout",
				"Verify Message Present:- You can add a maximum of 4 Feature Tiles with this Layout");

		int featureTilesCount = getDriver().findElements(featureTiles).size();
		while (featureTilesCount < 4) {
			click(addNewFeatureTile, "Add New Feature Tile");
			sync(200l);
			featureTilesCount = getDriver().findElements(featureTiles).size();
		}
		featureTilesCount = getDriver().findElements(featureTiles).size();
		Assert.assertTrue(featureTilesCount == 4, "4 Slides are added");
		Assert.assertEquals(checkElementPresent(addNewFeatureTile, 5), false,
				"Add Feature Tile Button has disappeared");

		List<WebElement> featureTile = getDriver().findElements(featureTiles);

		for (int i = 0; i < featureTile.size(); i++) {
			featureTile = getDriver().findElements(featureTiles);
			featureTile.get(i).click();

			System.out.println(getLocatorValue(getImageSourceBy(WidgetType.Feature_Set, "Icon")));
			getJavascriptExecutor().executeScript("arguments[0].click();",
					getElementWhenPresent(getImageSourceBy(WidgetType.Feature_Set, "Icon")));

			click(chooseIconDropdown, "Icon DropDown");
			List<WebElement> icons = getDriver().findElements(selectIconFromList);
			click(icons.get(new Random().nextInt(icons.size())), getText(chooseIconDropdown));

			clear(getElementWhenPresent(featureText));
			click(editApplyButton, "Apply Button");
			Assert.assertEquals(getText(getErrorMessage(WidgetType.Feature_Set)),
					"Feature Tile Text is required for Feature Tile " + (i + 1),
					"Verified Error Message if Feature Text Is Blank");

			type(featureText, "Feature Text", "Feature Text" + (i + 1));

			if (majorVersion > 4) {
				click(settingButton, "Button Settings");
				toggleButton(WidgetType.Feature_Set, "Button Settings", i);
				Assert.assertTrue(checkElementPresent(buttonURL), "Button URL Field is Present.");
				Assert.assertTrue(checkElementPresent(newTab), "New Tab Checkbox Is Present.");
				Assert.assertTrue(checkElementPresent(deleteIcon), "Delete Icon Is Present On Edit Feature Tile");
				click(getDriver().findElement(By.xpath(getLocatorValue(editPopup(WidgetType.Feature_Set, true))
						+ "//div[contains(@class,'backButton') and following-sibling::h3]")), "Back Button");

			} else {
				type(buttonText, "Button Text", "Button Text" + (i + 1));
				Assert.assertTrue(checkElementPresent(buttonURL), "Button URL Field is Present.");
				Assert.assertTrue(checkElementPresent(newTab), "New Tab Checkbox Is Present.");
				Assert.assertTrue(checkElementPresent(deleteIcon), "Delete Icon Is Present On Edit Feature Tile");
			}

			click(backButton, "Back Button");
		}
		click(style, "Style Tab");
		click(getColorXpath("Title Text Color"), "Select Title Text Color");
		selectRandomColor();
		click(getColorXpath("Title Text Color"), "DeSelect Title Text Color");

		click(getColorXpath("Striker color"), "Select Striker Color");
		selectRandomColor();
		click(getColorXpath("Striker color"), "DeSelect Striker Color");

		selectHeaderSize();

		getJavascriptExecutor().executeScript("arguments[0].click();", getElementWhenPresent(getBackground("color")));
		click(getColorXpath("Background color"), "Select Background Color");
		selectRandomColor();
		click(getColorXpath("Background color"), "DeSelect Background Color");

		click(editApplyButton, "Apply Button");
	}

	private void editImageGallery() throws Exception {
		click(select3X2SmallImages, "Select 3X2 Small Images Change Layout");
		click(applyButton, "Apply Button");

		type(imageGalleryTitle, "Image Gallery Title", "Image Gallery Title " + System.currentTimeMillis());
		click(style, "Style Tab");

		click(getColorXpath("Title Text Color"), "Select Title Text Color");
		selectRandomColor();
		click(getColorXpath("Title Text Color"), "DeSelect Title Text Color");

		click(getColorXpath("Striker color"), "Select Striker Color");
		selectRandomColor();
		click(getColorXpath("Striker color"), "DeSelect Striker Color");

		selectHeaderSize();

		getJavascriptExecutor().executeScript("arguments[0].click();", getElementWhenPresent(getBackground("color")));
		click(getColorXpath("Background color"), "Select Background Color");
		selectRandomColor();
		click(getColorXpath("Background color"), "DeSelect Background Color");

		click(editApplyButton, "Apply Button");

	}

	private void editContentBox() throws IOException {
		getJavascriptExecutor().executeScript("arguments[0].click();", getElementWhenPresent(getBackground("color")));
		click(getColorXpath("Background: "), "Select Background Color");
		selectRandomColor();
		click(getColorXpath("Background: "), "DeSelect Background Color");

		sync(1000l);
		String editScript = "<h6><span style='font-size: 24px;'>This Is Heading</span></h6><p><span style='font-size: 16px;'><strong>This Is Bold Statement</strong></span></p><p><span style='font-size: 16px;'><u>This Is Underline Statement</u></span></p><p><span style='font-size: 16px;'><s>This is Strikethrough Statement</s></span></p><p style='text-align: center;'><span style='font-size: 16px;'>This is Center Align Statement</span></p><p style='text-align: left;'><span style='font-size: 16px;'>This is Left Align Statement</span></p><p style='text-align: right;'><span style='font-size: 16px;'>This is Right Align Statement<br></span></p><p><br></p>";

		getJavascriptExecutor().executeScript("arguments[0].innerHTML=arguments[1];",
				getElementWhenPresent(editContentBox), editScript);

		click(editApplyButton, "Apply Button");
		sync(10000l);
	}

	private void editTypeform() throws IOException {
		click(editApplyButton, "Apply Button");
		Assert.assertEquals(getText(getErrorMessage(WidgetType.Typeform)),
				"You cannot save changes without selecting a Typeform",
				"Verified Error Message if No Typeform Is Selected");
		clickByMouseEvent(getElementWhenPresent(getErrorMessage(WidgetType.Typeform))
				.findElement(By.xpath("./preceding-sibling::*[contains(@class,'closeIcon')]")));

		click(selectTypeform, "Select Typeform");

		Assert.assertTrue(checkElementPresent(selectTypeformPopup), "Verify Select Typeform Popup is displayed");
		boolean typeFormListStatus = checkElementPresent(
				By.xpath(getLocatorValue(selectTypeformPopup) + "//following::input[@type='radio']"), 30);
		if (!typeFormListStatus) {
			SoftAssert.assertTrue(false, "No Typeform Found. Please check typeform is enabled from Super Admin Setup");
			click(By.xpath(getLocatorValue(selectTypeformPopup) + "//following::button[.='Cancel']"), "Cancel Button");
			click(editCancelButton, "Cancel Button");

			clickByMouseEvent(getElementWhenPresent(deleteTypeform));
			getJavascriptExecutor().executeScript("arguments[0].click();",
					getElementWhenPresent(By.xpath("//button[contains(@class,'style-delete')]")));
			SoftAssert.assertTrue(false, "Typeform widget was added successfully");
		} else {
			getJavascriptExecutor().executeScript("arguments[0].click();", getElementWhenPresent(
					By.xpath("(" + getLocatorValue(selectTypeformPopup) + "//following::input[@type='radio'])[1]")));
			objName = "Select Typeform";
			Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
					"User should able to click on " + objName.toLowerCase(),
					"User clicked on " + objName.toLowerCase() + " successfully", "Pass");

//		click(By.xpath("(" + getLocatorValue(selectTypeformPopup) + "//following::input[@type='radio'])[1]"),
//				"Select Typeform");

			click(By.xpath(getLocatorValue(selectTypeformPopup) + "//following::button[.='Save']"), "Save Button");
			getJavascriptExecutor().executeScript("arguments[0].click();",
					getElementWhenPresent(allowMultipleSubmission));
			objName = "Select Checkbox Allow multiple submissions for this survey";
			Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
					"User should able to click on " + objName.toLowerCase(),
					"User clicked on " + objName.toLowerCase() + " successfully", "Pass");
//		click(allowMultipleSubmission, "Select Checkbox Allow multiple submissions for this survey");

			click(editApplyButton, "Apply Button");
		}

	}

	private void editPdfDocument() throws Exception {
		getDriver().findElement(uploadFile).sendKeys(System.getProperty("user.dir") + "/uploadFile/ContentBox.pdf");

		waitForInvisibilityOfElement(loader, 20);

		click(style, "Style Tab");

		click(getColorXpath("Title Text Color"), "Select Title Text Color");
		selectRandomColor();
		click(getColorXpath("Title Text Color"), "DeSelect Title Text Color");

		selectHeaderSize();

		click(getColorXpath("Striker color"), "Select Striker Color");
		selectRandomColor();
		click(getColorXpath("Striker color"), "DeSelect Striker Color");

		getJavascriptExecutor().executeScript("arguments[0].click();", getElementWhenPresent(getBackground("color")));
		objName = "Radio Button Color";
		Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
				"User should able to click on " + objName.toLowerCase(),
				"User clicked on " + objName.toLowerCase() + " successfully", "Pass");

		click(getColorXpath("Background Color"), "Select Background Color");
		selectRandomColor();
		click(getColorXpath("Background Color"), "DeSelect Background Color");

		click(editApplyButton, "Apply Button");

		/**
		 * Remove this section once upload functionality is added
		 */
//		click(editCancelButton, "Cancel Button");
//
//		clickByMouseEvent(getElementWhenPresent(deletePDF));
//		getJavascriptExecutor().executeScript("arguments[0].click();",
//				getElementWhenPresent(By.xpath("//button[contains(@class,'style-delete')]")));
//		SoftAssert.assertTrue(false, "PDF widget added successfully");
	}

	private void editVideoPlayer() throws Exception {
		click(editApplyButton, "Apply Button");
		click(editApplyButton, "Apply Button");
		Assert.assertEquals(getText(getErrorMessage(WidgetType.Video_Player)), "Please enter a video URL.",
				"Verified Error Message if No URL Is Entered");
		clickByMouseEvent(getElementWhenPresent(getErrorMessage(WidgetType.Video_Player))
				.findElement(By.xpath("./preceding-sibling::*[contains(@class,'closeIcon')]")));

		getJavascriptExecutor().executeScript("arguments[0].click();",
				getElementWhenPresent(getImageSourceBy(WidgetType.Video_Player, "Upload Video")));
		click(editApplyButton, "Apply Button");
		Assert.assertEquals(getText(getErrorMessage(WidgetType.Video_Player)), "Please upload a video.",
				"Verified Error Message if No URL Is Entered");
		clickByMouseEvent(getElementWhenPresent(getErrorMessage(WidgetType.Video_Player))
				.findElement(By.xpath("./preceding-sibling::*[contains(@class,'closeIcon')]")));

		getJavascriptExecutor().executeScript("arguments[0].click();",
				getElementWhenPresent(getImageSourceBy(WidgetType.Video_Player, "YouTube/Vimeo")));

		type(videoTitle, "Video Title", "Ticketmaster Begins Interactive Seat Maps Beta Launch");

		try {
			getElementWhenPresent(videoURL).sendKeys("https://www.youtube.com/watch?v=Rq1FSglST8E");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		type(videoURL, "Video URL", "https://www.youtube.com/watch?v=Rq1FSglST8E");

		click(style, "Style Tab");

		click(getColorXpath("Title Text Color"), "Select Title Text Color");
		selectRandomColor();
		click(getColorXpath("Title Text Color"), "DeSelect Title Text Color");

		selectHeaderSize();

		click(getColorXpath("Striker color"), "Select Striker Color");
		selectRandomColor();
		click(getColorXpath("Striker color"), "DeSelect Striker Color");

		getJavascriptExecutor().executeScript("arguments[0].click();", getElementWhenPresent(getBackground("color")));
		objName = "Radio Button Color";
		Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
				"User should able to click on " + objName.toLowerCase(),
				"User clicked on " + objName.toLowerCase() + " successfully", "Pass");

		click(getColorXpath("Background Color"), "Select Background Color");
		selectRandomColor();
		click(getColorXpath("Background Color"), "DeSelect Background Color");

		click(editApplyButton, "Apply Button");

	}

	private WidgetType getWidgetType(String widgetName) {
		widgetName = widgetName.toUpperCase();
		WidgetType widgetType = null;
		switch (widgetName) {
		case "WELCOME CAROUSEL":
			widgetType = WidgetType.Welcome_Carousel;
			break;

		case "FEATURE SET":
			widgetType = WidgetType.Feature_Set;
			break;

		case "IMAGE GALLERY":
			widgetType = WidgetType.Image_Gallery;
			break;

		case "CONTENT BOX":
			widgetType = WidgetType.Content_Box;
			break;

		case "TYPEFORM":
			widgetType = WidgetType.Typeform;
			break;

		case "PDF DOCUMENT":
			widgetType = WidgetType.PDF_Document;
			break;

		case "VIDEO PLAYER":
			widgetType = WidgetType.Video_Player;
			break;

		}

		return widgetType;
	}

	private String getClickByMouseEventResource() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("js/clickByMouseEvent.js"));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		String clickByMouseEventScript = stringBuilder.toString();
		return clickByMouseEventScript;
	}

	private void selectRandomColor() throws IOException {
		sync(100l);
		Random random = new Random();
		int X = random.nextInt(500) + 100;
		int Y = random.nextInt(500) + 100;
		String clickScript = "performClick(arguments[0], arguments[1], arguments[2])";
		getJavascriptExecutor().executeScript(getClickByMouseEventResource() + clickScript,
				getElementWhenPresent(selectRandomColor), X, Y);
		sync(100l);
	}

	private void selectHeaderSize() throws IOException {
		sync(100l);
		Random random = new Random();
		int X = random.nextInt(400) + 100;
		int Y = 0;
		String clickScript = "performClick(arguments[0], arguments[1], arguments[2])";
		getJavascriptExecutor().executeScript(getClickByMouseEventResource() + clickScript,
				getElementWhenPresent(headerSize), X, Y);
		sync(100l);
	}

	private void clickByMouseEvent(WebElement element) throws IOException {
		System.out.println(element);
		sync(100l);
		int X = 0;
		int Y = 0;
		String clickScript = "performClick(arguments[0], arguments[1], arguments[2])";
		getJavascriptExecutor().executeScript(getClickByMouseEventResource() + clickScript, element, X, Y);
		sync(100l);
	}

	private JavascriptExecutor getJavascriptExecutor() {
		JavascriptExecutor jse = (JavascriptExecutor) getDriver();
		return jse;
	}

	public void clickPublishButton() {
		sync(5000l);
		click(publish, "Publish Button");
	}

	public void verifyPopupPresent() {
		Assert.assertTrue(checkElementPresent(popup), "Add content for other languages :: Popup is present");
	}

	public void clickContinueButton() {
		click(Continue, "Continue Button");
	}

	public void verifyPlubishedPage() {
		boolean flag = false;
		List<WebElement> rows = getDriver().findElements(By.xpath(getLocatorValue(viewPageTableRows)));
		for (WebElement row : rows) {
			WebElement toggleButton = row.findElement(By.xpath("./td[6]//input[@type='checkbox']"));
			if (getToggleButtonStatus(toggleButton)) {
				flag = true;
				Assert.assertTrue(true, "TestAutomation is in published state");
				break;
			}
		}
		if (!flag) {
			Assert.assertTrue(false, "TestAutomation is not in published state");
		}
	}

	/**
	 * 
	 * @return true if toggle button is On
	 */
	private boolean getToggleButtonStatus(WebElement toggleButton) {
		JavascriptExecutor jse = (JavascriptExecutor) getDriver();
		Boolean status = (Boolean) jse.executeScript("return arguments[0].checked;", toggleButton);
		return status;
	}

	private boolean waitForInvisibilityOfElement(By by, int timeInSeconds) {
		sync(2000l);
		getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.MILLISECONDS);

		WebDriverWait wait = new WebDriverWait(getDriver(), timeInSeconds);
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			getDriver().manage().timeouts().implicitlyWait(Long.valueOf(Environment.get("implicitWait")),
					TimeUnit.MILLISECONDS);
		}

	}

	private String getTagVersion() {
		if (tagVersion == null) {
			tagVersion = Dictionary.get("tagVersion");
			String[] versions = tagVersion.split("\\.");
			try {
				majorVersion = Integer.parseInt(versions[0]);
				minorVersion = Integer.parseInt(versions[1]);
				maintenanceVersion = Integer.parseInt(versions[2]);
			} catch (Exception e) {
				majorVersion = 5;
				minorVersion = 5;
				maintenanceVersion = 5;
			}
		}
		return tagVersion;
	}

	private void deleteAllSlides() throws IOException {
		int slideCount = getDriver().findElements(slide).size();
		while (slideCount >= 1) {
			System.out.println(slideCount);
			getDriver().findElements(slide).get(0).click();
			clickByMouseEvent(getElementWhenPresent(deleteIcon).findElement(By.xpath("./*")));
//			getJavascriptExecutor().executeScript("arguments[0].click();", getElementWhenPresent(deleteIcon));
			objName = "Delete Icon";
			Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
					"User should able to click on " + objName.toLowerCase(),
					"User clicked on " + objName.toLowerCase() + " successfully", "Pass");
			sync(500l);
			getDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			slideCount = getDriver().findElements(slide).size();
			getDriver().manage().timeouts().implicitlyWait(Long.valueOf(Environment.get("implicitWait")),
					TimeUnit.MILLISECONDS);
		}
	}

	private void toggleButton(WidgetType widgetType, String buttonType, int slideNumber) throws Exception {
		getDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		boolean primaryButtonStatus = getElementWhenPresent(getToggleButton(widgetType, buttonType)).isSelected();
		if (primaryButtonStatus) {
			Assert.assertTrue(true, "Primary Button Is Enabled");
			// Disabling Primary Button
			getJavascriptExecutor().executeScript("arguments[0].click()",
					getElementWhenPresent(getToggleButton(widgetType, buttonType)));
			objName = "Toggle Primary Button :: Off";
			Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
					"User should able to click on " + objName.toLowerCase(),
					"User clicked on " + objName.toLowerCase() + " successfully", "Pass");
//			click(getToggleButton(buttonType), "Toggle Primary Button :: Off");
			Assert.assertEquals(getElementWhenPresent(getToggleButton(widgetType, buttonType)).isSelected(), false,
					"Primary Button Is Disabled");
			if (buttonType.equals("Primary Button")) {
				boolean primaryButton_UI_status = waitForInvisibilityOfElement(
						getPrimaryButton_UI(widgetType, slideNumber), 20);
				Assert.assertEquals(primaryButton_UI_status, true, "Primary Button Is Disabled On UI");
			} else if (buttonType.equals("Secondary Button")) {
				boolean primaryButton_UI_status = waitForInvisibilityOfElement(secondaryButton_UI, 20);
				Assert.assertEquals(primaryButton_UI_status, true, "Secondary Button Is Disabled On UI");
			}
		}
		// Enabling Primary Button
		getJavascriptExecutor().executeScript("arguments[0].click()",
				getElementWhenPresent(getToggleButton(widgetType, buttonType)));
		objName = "Toggle Primary Button :: On";
		Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
				"User should able to click on " + objName.toLowerCase(),
				"User clicked on " + objName.toLowerCase() + " successfully", "Pass");
//		click(getToggleButton(buttonType), "Toggle Primary Button :: On");
		WebElement buttonName_Field = getElementWhenPresent(getbuttonNameField(widgetType));
		String maxLength = buttonName_Field.getAttribute("maxlength");
		clear(getElementWhenPresent(getbuttonNameField(widgetType)));
		String getToolTipMsg = buttonName_Field.findElement(By.xpath("./following::p")).getText();
		Assert.assertEquals(getToolTipMsg, maxLength + " Characters remaining",
				maxLength + " Characters remaining Message Verified.");

		getDriver().findElement(getbuttonNameField(widgetType)).sendKeys("12345678901234567890");

//		type(buttonNameField, "Button Name", "12345678901234567890");

		getToolTipMsg = buttonName_Field.findElement(By.xpath("./following::p")).getText();
		Assert.assertEquals(getToolTipMsg, 0 + " Character remaining", "Message Verified :: Character Remaining");
		String textEntered = getElementWhenPresent(getbuttonNameField(widgetType)).getAttribute("value");
		System.out.println("TEXT ENTERED :: " + textEntered);
		Assert.assertEquals(textEntered.length(), 18, "Typed 20 Character But accepting only 18");
		if (buttonType.equals("Primary Button")) {
			String textEntered_UI = getDriver().findElement(getPrimaryButton_UI(widgetType, slideNumber)).getText();
			Assert.assertEquals(textEntered_UI, "123456789012345678", "Text is displayed on UI button");
		} else if (buttonType.equals("Secondary Button")) {
			String textEntered_UI = getDriver().findElements(secondaryButton_UI).get(0).getText();
			Assert.assertEquals(textEntered_UI, "123456789012345678", "Text is displayed on UI button");
		}

		clear(getElementWhenPresent(getbuttonNameField(widgetType)));
		getDriver().findElement(getbuttonNameField(widgetType)).sendKeys(buttonType.toUpperCase());

//		type(buttonNameField, "Button Name", "PRIMARY BUTTON");

		selectButtonStyle(widgetType, buttonType, slideNumber);
		selectButtonSize(widgetType, buttonType, slideNumber);

		getDriver().manage().timeouts().implicitlyWait(Long.valueOf(Environment.get("implicitWait")),
				TimeUnit.MILLISECONDS);
	}

	private void selectButtonSize(WidgetType widgetType, String buttonType, int slideNumber) {
		List<WebElement> buttonSizes = getDriver().findElements(getButtonSize(widgetType));
		Random random = new Random();
		int x = random.nextInt(3);
		WebElement button_size = buttonSizes.get(x);
		objName = button_size.getText();
		button_size.click();
		Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
				"User should able to click on " + objName.toLowerCase(),
				"User clicked on " + objName.toLowerCase() + " successfully", "Pass");
		WebElement ButtonUI = null;
		if (buttonType.equals("Primary Button") || buttonType.equals("Button Settings"))
			ButtonUI = getElementWhenPresent(getPrimaryButton_UI(widgetType, slideNumber));
		else
			ButtonUI = getElementWhenPresent(By.xpath("(" + getLocatorValue(secondaryButton_UI) + ")[" + 1 + "]"));
		String ButtonStyle = ButtonUI.getAttribute("style");
		if (objName.contains("Small")) {
			if (ButtonStyle.contains("ont-size: 12px;")) {
				Assert.assertTrue(true, "Small Size Button Is Reflected On UI");
			} else {
				Assert.assertTrue(false, "Small Size Button Is Not Reflected On UI");
			}
		} else if (objName.contains("Medium")) {
			if (ButtonStyle.contains("ont-size: 14px;")) {
				Assert.assertTrue(true, "Medium Size Button Is Reflected On UI");
			} else {
				Assert.assertTrue(false, "Medium Size Button Is Not Reflected On UI");
			}
		} else if (objName.contains("Large")) {
			if (ButtonStyle.contains("ont-size: 16px;")) {
				Assert.assertTrue(true, "Large Size Button Is Reflected On UI");
			} else {
				Assert.assertTrue(false, "Large Size Button Is Not Reflected On UI");
			}
		}

	}

	private void selectButtonStyle(WidgetType widgetType, String buttonType, int slideNumber) throws IOException {
		click(getButtonStyle(widgetType), "Button Style");
		List<WebElement> buttonTypes = getDriver().findElements(getButtontype(widgetType));
		Random random = new Random();
		int x = random.nextInt(3);
		WebElement button_type = buttonTypes.get(x);
		objName = button_type.getText();
		button_type.click();
		Reporter.log("Verify user is able to click on " + objName.toLowerCase(),
				"User should able to click on " + objName.toLowerCase(),
				"User clicked on " + objName.toLowerCase() + " successfully", "Pass");

		WebElement ButtonUI = null;
		if (buttonType.equals("Primary Button") || buttonType.equals("Button Settings"))
			ButtonUI = getElementWhenPresent(getPrimaryButton_UI(widgetType, slideNumber));
		else
			ButtonUI = getElementWhenPresent(secondaryButton_UI);
		String ButtonStyle = ButtonUI.getAttribute("style");
		System.out.println("CSS STYLE :: " + ButtonStyle);
		if (objName.contains("Filled Button")) {
			if ((ButtonStyle.contains("border-color: transparent;")
					|| ButtonStyle.contains("border: 1px solid transparent;"))
					&& ButtonStyle.contains("background-color: rgb")) {
				Assert.assertTrue(true, "Filled Type Button is reflected on UI");
			} else {
				Assert.assertTrue(false, "Filled Type Button is not reflected on UI");
			}
			click(getColorXpath("Background Color"), "Background Color");
			selectRandomColor();
			click(getColorXpath("Background Color"), "Background Color");

			click(getColorXpath("Text Color"), "Text Color");
			selectRandomColor();
			click(getColorXpath("Text Color"), "Text Color");

		} else if (objName.contains("Lined Button")) {
			if ((ButtonStyle.contains("border-color: rgb") || ButtonStyle.contains("border: 1px solid rgb"))
					&& ButtonStyle.contains("background-color: transparent;")) {
				Assert.assertTrue(true, "Lined Type Button is reflected on UI");
			} else {
				Assert.assertTrue(false, "Lined Type Button is not reflected on UI");
			}
			click(getColorXpath("Border & Text Color"), "Border & Text Color");
			selectRandomColor();
			click(getColorXpath("Border & Text Color"), "Border & Text Color");
		} else if (objName.contains("Link")) {
			if ((ButtonStyle.contains("border-color: transparent;")
					|| ButtonStyle.contains("border: 1px solid transparent;"))
					&& ButtonStyle.contains("background-color: transparent;")) {
				Assert.assertTrue(true, "Link Type Button is reflected on UI");
			} else {
				Assert.assertTrue(false, "Link Type Button is not reflected on UI");
			}
			click(getColorXpath("Text Color"), "Text Color");
			selectRandomColor();
			click(getColorXpath("Text Color"), "Text Color");
		}
	}

	public static void main(String[] args) {
		String tagVersion = "5.0.2";
		String[] versions = tagVersion.split("\\.");
		System.out.println(versions);
		for (String string : versions) {
			System.out.println(string);
		}
		System.out.println(Integer.parseInt(versions[0]));
//		minorVersion = Integer.parseInt(versions[1]);
//		maintenanceVersion = Integer.parseInt(versions[2]);
	}

}
