package org.scraping.login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.scraping.Scraper;

public class LoginScraper implements Scraper {
    @Override
    public void scrape() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");  // Set the path to your chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Optional: Run headlessly for faster execution without a browser window
        WebDriver driver = new ChromeDriver(options);

        try {
            String url = "https://www.scrapingcourse.com/login";
            driver.get(url);

            // Enter login credentials
            WebElement emailInput = driver.findElement(By.id("email"));
            emailInput.sendKeys("admin@example.com");

            WebElement passwordInput = driver.findElement(By.id("password"));
            passwordInput.sendKeys("password");

            // Submit the form
            WebElement submitButton = driver.findElement(By.id("submit-button"));
            submitButton.click();

            // Wait a moment for the page to load after login
            Thread.sleep(2000);

            // Parse the page after login
            Document doc = Jsoup.parse(driver.getPageSource());

            // Example of scraping product information after login
            System.out.println("Scraped content after login:");
            doc.select(".product-item").forEach(product -> {
                String name = product.select(".product-name").text();
                String price = product.select(".product-price").text();
                String link = product.select("a.product-link").attr("href");
                String imageUrl = product.select("img.product-image").attr("src");

                System.out.println("Product Name: " + name);
                System.out.println("Price: " + price);
                System.out.println("Link: " + link);
                System.out.println("Image URL: " + imageUrl);
                System.out.println("-------------------------------");
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
