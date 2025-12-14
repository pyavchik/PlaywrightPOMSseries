package com.qa.opencart.pages;

import com.microsoft.playwright.Page;

public class LoginPage {

    private Page page;

    // 1. String Locators - OR
    private String emaileInput = "//input[@id='input-email']";
    private String passwordInput = "//input[@id='input-password']";
    private String loginBtn = "//input[@type='submit']";
    private String forgotPwdLink = "//div[@class='form-group']/a[text()='Forgotten Password']";
    private String logoutLink = "//a[@class='list-group-item'][normalize-space()='Logout']";

    // 2. Page constructor
    public LoginPage(Page page) {
        this.page = page;
    }

    // 3. Page actions/methods
    public String getLoginPageTitle(){
        return page.title();
    }

    public String getLoginPageUrl(){
        return page.url();
    }

    public boolean isForgotPwdLinkExists(){
        return page.isVisible(forgotPwdLink);
    }

    public AccountPage doLogin(String appUserName, String appPassword){
        System.out.println("App credentials: " + appUserName + " : " + appPassword);
        page.fill(emaileInput, appUserName);
        page.fill(passwordInput, appPassword);
        page.click(loginBtn);

        return new AccountPage(page);
    }


}
