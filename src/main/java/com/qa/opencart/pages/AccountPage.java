package com.qa.opencart.pages;

import com.microsoft.playwright.Page;

public class AccountPage {
    Page page;

    // 1. String Locators - OR
    private String emaileInput = "//input[@id='input-email']";

    // 2. Page constructor
    public AccountPage(Page page) {
        this.page = page;
    }

    // 3. Page actions/methods
    public String getAccountPageTitle(){
        return page.title();
    }
}
