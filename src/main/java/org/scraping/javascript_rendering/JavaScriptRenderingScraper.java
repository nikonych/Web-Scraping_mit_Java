package org.scraping.javascript_rendering;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.scraping.Scraper;

public class JavaScriptRenderingScraper implements Scraper {
    @Override
    public void scrape() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");  // Set the path to your chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode for faster scraping
        WebDriver driver = new ChromeDriver(options);

        String url = "https://www.scrapingcourse.com/javascript-rendering";
        driver.get(url);

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            boolean hasMoreContent = true;

            // Keep scrolling until no more content is loaded
            while (hasMoreContent) {
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000); // Wait for new content to load

                Document doc = Jsoup.parse(driver.getPageSource());
                Elements products = doc.select(".product-item");

                for (Element product : products) {
                    String name = product.select(".product-name").text();
                    String price = product.select(".product-price").text();
                    String link = product.select("a.product-link").attr("href");
                    String imageUrl = product.select("img.product-image").attr("src");

                    System.out.println("Product Name: " + name);
                    System.out.println("Price: " + price);
                    System.out.println("Link: " + link);
                    System.out.println("Image URL: " + imageUrl);
                    System.out.println("-------------------------------");
                }

                // Check if we've reached the end of the content
                hasMoreContent = doc.select("#end-of-page").isEmpty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
