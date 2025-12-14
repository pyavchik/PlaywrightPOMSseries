package com.qa.opencart.pages;

import com.microsoft.playwright.Page;

public class HomePage {

    private Page page;

    // 1. String Locators
    private String search = "input[name='search']";
    private String searchIcon = "div#search button";
    private String searchPageHeader = "div#content h1";
    private String loginLink = "a:has-text('Login')";
    private String accountLink = "a[title='My Account']";

    // 2. Page constructor
    public HomePage(Page page) {
        this.page = page;
    }

    // 3. Page actions/methods
    public String getHomePageTitle(){
        String pageTitle = page.title();
        System.out.println("Page title is: " + pageTitle);
        return pageTitle;
    }

    public String getHomePageUrl(){
        String pageUrl = page.url();
        System.out.println("Page url is: " + pageUrl);
        return pageUrl;
    }

    public String doSearch(String productName){
        page.fill(search, "");
        page.fill(search, productName);
        page.click(searchIcon);
        String searchPageHeaderText = page.textContent(searchPageHeader);
        System.out.println("Search page header text is: " + searchPageHeaderText);
        return searchPageHeaderText;
    }

    public LoginPage navigateToLoginPage(){
        page.click(accountLink);
        page.click(loginLink);
        System.out.println("Login link is clicked");
        return new LoginPage(page);
    }

}
