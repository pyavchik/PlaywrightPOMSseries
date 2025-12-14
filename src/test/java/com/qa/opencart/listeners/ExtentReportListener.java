package com.qa.opencart.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.microsoft.playwright.Page;
import com.qa.opencart.factory.PlaywrightFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ExtentReportListener implements ITestListener {
    
    private static volatile ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final Object lock = new Object();
    private static Properties prop;
    private static boolean screenshotOnFailure;
    private static String screenshotPath;
    
    // Load properties on class initialization
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties");
            prop.load(ip);
            ip.close();
            
            // Read screenshot configuration
            screenshotOnFailure = Boolean.parseBoolean(prop.getProperty("screenshot.on.failure", "true"));
            screenshotPath = prop.getProperty("screenshot.path", "screenshots/");
            
            // Ensure screenshot path ends with /
            if (!screenshotPath.endsWith("/")) {
                screenshotPath += "/";
            }
            
            System.out.println("Screenshot Configuration - On Failure: " + screenshotOnFailure);
            System.out.println("Screenshot Configuration - Path: " + screenshotPath);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            // Use default values if properties file can't be loaded
            screenshotOnFailure = true;
            screenshotPath = "screenshots/";
        }
    }
    
    private static ExtentReports getExtent() {
        if (extent == null) {
            synchronized (lock) {
                if (extent == null) {
                    try {
                        extent = ExtentManager.getInstance();
                    } catch (ExceptionInInitializerError e) {
                        System.err.println("ExtentReports initialization failed: " + e.getMessage());
                        System.err.println("This might be due to Java version compatibility. Please ensure Java 11 is used.");
                        throw e;
                    } catch (Exception e) {
                        System.err.println("Error initializing ExtentReports: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to initialize ExtentReports", e);
                    }
                }
            }
        }
        return extent;
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite finished: " + context.getName());
        if (extent != null) {
            extent.flush();
            System.out.println("Extent Report generated at: " + ExtentManager.getReportPath());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            ExtentReports extentInstance = getExtent();
            if (extentInstance != null) {
                ExtentTest extentTest = extentInstance.createTest(result.getMethod().getMethodName())
                        .assignCategory(result.getTestClass().getName())
                        .assignAuthor("Automation Team");
                test.set(extentTest);
            }
            System.out.println("Test started: " + result.getMethod().getMethodName());
        } catch (ExceptionInInitializerError e) {
            System.err.println("ExtentReports initialization failed due to Java version compatibility issue.");
            System.err.println("Please ensure you're using Java 11 or configure IntelliJ to use Java 11.");
            System.err.println("Error: " + e.getMessage());
            // Continue without ExtentReports
            extent = null;
        } catch (Exception e) {
            System.err.println("Error initializing ExtentReports: " + e.getMessage());
            e.printStackTrace();
            // Continue without ExtentReports
            extent = null;
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            if (test.get() != null) {
                test.get().log(Status.PASS, MarkupHelper.createLabel(
                        result.getMethod().getMethodName() + " - Test Case PASSED", ExtentColor.GREEN));
            }
            System.out.println("Test passed: " + result.getMethod().getMethodName());
        } catch (Exception e) {
            System.err.println("Error logging test success: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            if (test.get() != null) {
                test.get().log(Status.FAIL, MarkupHelper.createLabel(
                        result.getMethod().getMethodName() + " - Test Case FAILED", ExtentColor.RED));
                if (result.getThrowable() != null) {
                    test.get().log(Status.FAIL, "Failure Reason: " + result.getThrowable().getMessage());
                    test.get().log(Status.FAIL, result.getThrowable());
                }
                
                // Add screenshot for failed tests - only if enabled in properties
                if (screenshotOnFailure) {
                    try {
                        Page page = PlaywrightFactory.getPage();
                        if (page != null) {
                            String screenshotFilePath = captureScreenshot(result.getMethod().getMethodName(), "FAIL");
                            if (screenshotFilePath != null) {
                                test.get().fail("Screenshot captured on failure").addScreenCaptureFromPath(screenshotFilePath);
                            }
                        }
                    } catch (Exception e) {
                        test.get().log(Status.INFO, "Screenshot not available: " + e.getMessage());
                    }
                } else {
                    System.out.println("Screenshot capture is disabled in config.properties");
                }
            }
            System.out.println("Test failed: " + result.getMethod().getMethodName());
        } catch (Exception e) {
            System.err.println("Error logging test failure: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            if (test.get() != null) {
                test.get().log(Status.SKIP, MarkupHelper.createLabel(
                        result.getMethod().getMethodName() + " - Test Case SKIPPED", ExtentColor.ORANGE));
                if (result.getThrowable() != null) {
                    test.get().log(Status.SKIP, "Skip Reason: " + result.getThrowable().getMessage());
                }
            }
            System.out.println("Test skipped: " + result.getMethod().getMethodName());
        } catch (Exception e) {
            System.err.println("Error logging test skip: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not implemented
    }

    private String captureScreenshot(String testName, String status) {
        try {
            // Use screenshot path from properties file
            String baseDir = System.getProperty("user.dir") + "/test-output/";
            String screenshotsDir = baseDir + screenshotPath;
            
            // Create screenshots directory if it doesn't exist
            Files.createDirectories(Paths.get(screenshotsDir));
            
            // Create filename with test name, status, and timestamp
            String timestamp = String.valueOf(System.currentTimeMillis());
            String screenshotFileName = testName + "_" + status + "_" + timestamp + ".png";
            String fullScreenshotPath = screenshotsDir + screenshotFileName;
            
            Page page = PlaywrightFactory.getPage();
            if (page != null) {
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get(fullScreenshotPath))
                        .setFullPage(true));
                
                // Return relative path for ExtentReports (relative to report location)
                // Use the screenshot path from properties (without leading /)
                return screenshotPath + screenshotFileName;
            }
        } catch (Exception e) {
            System.out.println("Error capturing screenshot: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

