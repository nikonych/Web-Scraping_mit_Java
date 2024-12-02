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
        // Einrichten des WebDrivers
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");  // Pfad zum Chromedriver angeben
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Im headless-Modus ausführen
        WebDriver driver = new ChromeDriver(options);

        // Definieren der zu scrappenden URL
        String url = "https://www.scrapingcourse.com/infinite-scrolling";
        driver.get(url);

        try {
            // Speichern von einzigartigen Produktdaten, um Duplikate zu vermeiden
            Set<String> productLinks = new HashSet<>();
            boolean hasMoreContent = true;

            while (hasMoreContent) {
                // Scrollen zum Ende der Seite, um das Laden weiterer Inhalte auszulösen
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

                // Zeit geben, damit neue Produkte geladen werden
                Thread.sleep(2000);

                // Analysieren des aktuellen Seiteninhalts
                Document doc = Jsoup.parse(driver.getPageSource());
                Elements products = doc.select(".product-item");

                // Extrahieren von Produktinformationen
                for (Element product : products) {
                    String productName = product.select(".product-name").text();
                    String productPrice = product.select(".product-price").text();
                    String productImage = product.select(".product-image").attr("src");
                    String productLink = product.select("a").attr("href");

                    // Nur neue Produkte ausgeben
                    if (!productLinks.contains(productLink)) {
                        productLinks.add(productLink);
                        System.out.println("Produktname: " + productName);
                        System.out.println("Produktpreis: " + productPrice);
                        System.out.println("Produktbild: " + productImage);
                        System.out.println("Produktlink: " + productLink);
                        System.out.println("-------------------------------");
                    }
                }

                // Prüfen, ob weitere Inhalte geladen werden können
                hasMoreContent = !doc.select("#end-of-page").isEmpty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
