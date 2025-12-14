package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import org.testng.annotations.Test;

import static com.qa.opencart.constants.AppConstants.ACCOUNT_PAGE_TITLE;
import static com.qa.opencart.constants.AppConstants.LOGIN_PAGE_TITLE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class LoginPageTest extends BaseTest {

    @Test(priority = 1)
    public void loginPageNavigationTest(){
        loginPage = homePage.navigateToLoginPage();
        String actualLoginPageTitle = loginPage.getLoginPageTitle();
        System.out.println(actualLoginPageTitle);
        assertEquals(actualLoginPageTitle, LOGIN_PAGE_TITLE);
    }

    @Test(priority = 2)
    public void forgotPwdLinkExistTest(){
        assertTrue(loginPage.isForgotPwdLinkExists());

    }

    @Test(priority = 3)
    public void appLoginTest(){
        accountPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
        String actualAccountPageTitle = accountPage.getAccountPageTitle();
        assertEquals(actualAccountPageTitle, ACCOUNT_PAGE_TITLE);
    }
}
