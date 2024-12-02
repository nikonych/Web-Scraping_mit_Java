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
                // Загрузите HTML-контент текущей страницы
                Document doc = Jsoup.connect(nextPageUrl).get();
                System.out.println("Seite wird analysiert: " + currentPage + " von " + totalPages);

                // Находим список продуктов по селектору CSS класса 'products'
                Elements products = doc.select("ul.products li.product");

                // Парсинг информации о продуктах на текущей странице
                for (Element product : products) {
                    // Извлекаем название продукта
                    String name = product.select("h2.product-name").text();

                    // Извлекаем цену продукта
                    String price = product.select("span.price").text();

                    // Извлекаем ссылку на продукт
                    String link = product.select("a.woocommerce-LoopProduct-link").attr("href");

                    // Извлекаем ссылку на изображение продукта
                    String imageUrl = product.select("img").attr("src");

                    // Печатаем полученную информацию о продукте
                    System.out.println("Produktname: " + name);
                    System.out.println("Preis: " + price);
                    System.out.println("Link: " + link);
                    System.out.println("Bild URL: " + imageUrl);

                    // Получаем дополнительную информацию о продукте
                    getProductDetails(link);

                    System.out.println("-------------------------------");
                }

                // Вычисление и вывод процента завершения
                double progress = (double) currentPage / totalPages * 100;
                System.out.printf("Fortschritt: %.2f%%\n", progress);

                // Переход к следующей странице
                Element nextPageElement = doc.selectFirst("a.next.page-numbers");
                if (nextPageElement != null) {
                    nextPageUrl = nextPageElement.attr("href");
                    currentPage++;
                } else {
                    nextPageUrl = null; // Если следующей страницы нет, заканчиваем цикл
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для получения общего количества страниц
    private static int getTotalPages(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements pageNumbers = doc.select("ul.page-numbers li a.page-numbers");
            if (pageNumbers.size() > 0) {
                String lastPageNumber = pageNumbers.get(pageNumbers.size() - 2).text(); // Предпоследний элемент - последняя страница
                return Integer.parseInt(lastPageNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1; // Возвращаем 1 по умолчанию, если не удалось определить количество страниц
    }

    // Метод для получения информации о товаре
    private static void getProductDetails(String productUrl) {
        try {
            Document doc = Jsoup.connect(productUrl).get();

            // Получаем подробное описание продукта
            String description = doc.selectFirst("#tab-description p").text();
            System.out.println("Beschreibung: " + description);

            // Получаем дополнительную информацию, если она доступна
            Element additionalInfoElement = doc.selectFirst("#tab-additional_information");
            if (additionalInfoElement != null) {
                String additionalInfo = additionalInfoElement.text();
                System.out.println("Zusätzliche Informationen: " + additionalInfo);
            }

            // Извлекаем SKU
            String sku = doc.selectFirst(".sku").text();
            System.out.println("SKU: " + sku);

            // Извлекаем категорию
            String category = doc.selectFirst(".posted_in a").text();
            System.out.println("Kategorie: " + category);

            // Добавьте задержку для предотвращения перегрузки сервера
            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("Fehler beim Abrufen von Produktdetails für URL: " + productUrl);
            e.printStackTrace();
        }
    }
}
