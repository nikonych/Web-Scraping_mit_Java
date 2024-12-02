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
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");  // Pfad zum Chromedriver angeben
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            String url = "https://www.scrapingcourse.com/login";
            driver.get(url);

            // Eingeben der Anmeldedaten
            WebElement emailInput = driver.findElement(By.id("email"));
            emailInput.sendKeys("admin@example.com");

            WebElement passwordInput = driver.findElement(By.id("password"));
            passwordInput.sendKeys("password");

            // Absenden des Formulars
            WebElement submitButton = driver.findElement(By.id("submit-button"));
            submitButton.click();

            // Einen Moment warten, bis die Seite nach dem Login geladen ist
            Thread.sleep(2000);

            Document doc = Jsoup.parse(driver.getPageSource());

            System.out.println("Inhalt nach dem Login gescraped:");
            doc.select(".product-item").forEach(product -> {
                String name = product.select(".product-name").text();
                String price = product.select(".product-price").text();
                String link = product.select("a.product-link").attr("href");
                String imageUrl = product.select("img.product-image").attr("src");

                System.out.println("Produktname: " + name);
                System.out.println("Preis: " + price);
                System.out.println("Link: " + link);
                System.out.println("Bild-URL: " + imageUrl);
                System.out.println("-------------------------------");
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
