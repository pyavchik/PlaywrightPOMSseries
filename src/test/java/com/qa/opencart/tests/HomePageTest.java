package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.qa.opencart.constants.AppConstants.HOME_PAGE_TITLE;
import static org.testng.Assert.assertEquals;


public class HomePageTest extends BaseTest {

    @Test
    public void homePageTitleTest(){
        String actualTitle = homePage.getHomePageTitle();
        assertEquals(actualTitle, HOME_PAGE_TITLE);
    }

    @Test
    public void homePageUrlTest(){
        String actualUrl = homePage.getHomePageUrl();
        assertEquals(actualUrl, prop.getProperty("url"));
    }

    @DataProvider
    public Object[][] getProductData(){
        return new Object[][]{
            {"Macbook"},
            {"iMac"},
            {"Samsung"},
            {"iPhone"},
            {"iPad"},
            {"Dell"},
            {"HP"},
            {"Lenovo"},
            {"Asus"},
            {"Acer"},
            {"Sony"},
            {"Canon"},
            {"Nikon"},
            {"Panasonic"},
            {"LG"},
            {"Toshiba"},
            {"Microsoft"},
            {"Google"},
            {"Xiaomi"},
            {"Huawei"},
            {"OnePlus"},
            {"Motorola"},
            {"Nokia"},
            {"BlackBerry"},
            {"Apple Watch"},
            {"AirPods"},
            {"Bose"},
            {"JBL"},
            {"Sennheiser"},
            {"Beats"},
            {"Samsung Galaxy"},
            {"Samsung TV"},
            {"Samsung Phone"},
            {"MacBook Pro"},
            {"MacBook Air"},
            {"iMac Pro"},
            {"Mac Mini"},
            {"Mac Pro"},
            {"Surface Pro"},
            {"Surface Laptop"},
            {"Xbox"},
            {"PlayStation"},
            {"Nintendo Switch"},
            {"Gaming Laptop"},
            {"Gaming Mouse"},
            {"Gaming Keyboard"},
            {"Monitor"},
            {"Keyboard"},
            {"Mouse"},
            {"Webcam"},
            {"Printer"},
            {"Scanner"},
            {"Router"},
            {"Modem"},
            {"Headphones"},
            {"Speakers"},
            {"Microphone"},
            {"Camera"},
            {"Lens"},
            {"Tripod"},
            {"Drone"},
            {"Smartwatch"},
            {"Fitness Tracker"},
            {"Tablet"},
            {"E-reader"},
            {"Smart TV"},
            {"Soundbar"},
            {"Home Theater"},
            {"Refrigerator"},
            {"Washing Machine"},
            {"Dishwasher"},
            {"Microwave"},
            {"Coffee Maker"},
            {"Blender"},
            {"Mixer"},
            {"Vacuum Cleaner"},
            {"Air Purifier"},
            {"Fan"},
            {"Heater"},
            {"Lamp"},
            {"Desk"},
            {"Chair"},
            {"Backpack"},
            {"Laptop Bag"},
            {"Phone Case"},
            {"Screen Protector"},
            {"Charger"},
            {"Power Bank"},
            {"Cable"},
            {"Adapter"},
            {"USB Drive"},
            {"External Hard Drive"},
            {"SSD"},
            {"Memory Card"},
            {"SD Card"},
            {"Bluetooth Speaker"},
            {"Wireless Earbuds"},
            {"Smart Home Hub"},
            {"Smart Light"},
            {"Security Camera"}
        };
    }

    @Test(dataProvider = "getProductData")
    public void searchTest(String productName){
        String actualSearchPageHeaderText = homePage.doSearch(productName);
        assertEquals(actualSearchPageHeaderText, "Search - " + productName);
    }

}
