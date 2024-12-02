package org.scraping.infinite_scroll;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.scraping.Scraper;

import java.util.HashSet;
import java.util.Set;

public class InfiniteScrollScraper implements Scraper {
    @Override
    public void scrape() {
        // Set up WebDriver
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");  // Specify the path to your chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);

        // Define the URL to scrape
        String url = "https://www.scrapingcourse.com/infinite-scrolling";
        driver.get(url);

        try {
            // Store unique product data to avoid duplicates
            Set<String> productLinks = new HashSet<>();
            boolean hasMoreContent = true;

            while (hasMoreContent) {
                // Scroll to the bottom of the page to trigger loading of more content
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

                // Give some time for the new products to load
                Thread.sleep(2000);

                // Parse the current page content
                Document doc = Jsoup.parse(driver.getPageSource());
                Elements products = doc.select(".product-item");

                // Extract product information
                for (Element product : products) {
                    String productName = product.select(".product-name").text();
                    String productPrice = product.select(".product-price").text();
                    String productImage = product.select(".product-image").attr("src");
                    String productLink = product.select("a").attr("href");

                    // Only print new products
                    if (!productLinks.contains(productLink)) {
                        productLinks.add(productLink);
                        System.out.println("Product Name: " + productName);
                        System.out.println("Product Price: " + productPrice);
                        System.out.println("Product Image: " + productImage);
                        System.out.println("Product Link: " + productLink);
                        System.out.println("-------------------------------");
                    }
                }

                // Check if there's more content to load
                hasMoreContent = !doc.select("#end-of-page").isEmpty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}


