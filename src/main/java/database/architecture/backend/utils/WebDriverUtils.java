package database.architecture.backend.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverUtils {

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        return new ChromeDriver(new ChromeDriverService.Builder().build(), options);
    }
}
