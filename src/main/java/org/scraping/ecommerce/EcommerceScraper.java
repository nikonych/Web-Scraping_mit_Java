package org.scraping.ecommerce;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scraping.Scraper;

public class EcommerceScraper implements Scraper {
    @Override
    public void scrape() {
        try {
            String baseUrl = "https://www.scrapingcourse.com/ecommerce/";
            String nextPageUrl = baseUrl;
            int currentPage = 1;
            int totalPages = getTotalPages(baseUrl);

            while (nextPageUrl != null) {
                Document doc = Jsoup.connect(nextPageUrl).get();
                System.out.println("Seite wird analysiert: " + currentPage + " von " + totalPages);

                // Finden der Produktliste mit dem CSS-Selektor für die Klasse 'products'
                Elements products = doc.select("ul.products li.product");

                // Parsen der Produktinformationen auf der aktuellen Seite
                for (Element product : products) {
                    String name = product.select("h2.product-name").text();

                    String price = product.select("span.price").text();

                    String link = product.select("a.woocommerce-LoopProduct-link").attr("href");

                    String imageUrl = product.select("img").attr("src");

                    System.out.println("Produktname: " + name);
                    System.out.println("Preis: " + price);
                    System.out.println("Link: " + link);
                    System.out.println("Bild-URL: " + imageUrl);

                    // Abrufen weiterer Informationen zum Produkt
                    getProductDetails(link);

                    System.out.println("-------------------------------");
                }

                // Berechnung und Ausgabe des Fortschritts in Prozent
                double progress = (double) currentPage / totalPages * 100;
                System.out.printf("Fortschritt: %.2f%%\n", progress);

                // Wechsel zur nächsten Seite
                Element nextPageElement = doc.selectFirst("a.next.page-numbers");
                if (nextPageElement != null) {
                    nextPageUrl = nextPageElement.attr("href");
                    currentPage++;
                } else {
                    nextPageUrl = null; // Wenn es keine nächste Seite gibt, wird die Schleife beendet
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methode zur Bestimmung der Gesamtanzahl der Seiten
    private static int getTotalPages(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements pageNumbers = doc.select("ul.page-numbers li a.page-numbers");
            if (pageNumbers.size() > 0) {
                String lastPageNumber = pageNumbers.get(pageNumbers.size() - 2).text(); // Vorletztes Element - letzte Seite
                return Integer.parseInt(lastPageNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1; // Standardmäßig 1 zurückgeben, wenn die Gesamtanzahl der Seiten nicht ermittelt werden kann
    }

    // Methode zum Abrufen der Produktinformationen
    private static void getProductDetails(String productUrl) {
        try {
            Document doc = Jsoup.connect(productUrl).get();

            // Abrufen der detaillierten Beschreibung des Produkts
            String description = doc.selectFirst("#tab-description p").text();
            System.out.println("Beschreibung: " + description);

            // Abrufen zusätzlicher Informationen, falls verfügbar
            Element additionalInfoElement = doc.selectFirst("#tab-additional_information");
            if (additionalInfoElement != null) {
                String additionalInfo = additionalInfoElement.text();
                System.out.println("Zusätzliche Informationen: " + additionalInfo);
            }

            String sku = doc.selectFirst(".sku").text();
            System.out.println("SKU: " + sku);

            String category = doc.selectFirst(".posted_in a").text();
            System.out.println("Kategorie: " + category);

            // Verzögerung hinzufügen, um den Server nicht zu überlasten
            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("Fehler beim Abrufen von Produktdetails für URL: " + productUrl);
            e.printStackTrace();
        }
    }
}
