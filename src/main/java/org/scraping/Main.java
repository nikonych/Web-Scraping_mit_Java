package org.scraping;

import org.scraping.ecommerce.EcommerceScraper;
import org.scraping.infinite_scroll.InfiniteScrollScraper;
import org.scraping.javascript_rendering.JavaScriptRenderingScraper;
import org.scraping.login.LoginScraper;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Array of scraper instances implementing the Scraper interface
        Scraper[] scrapers = {
                new EcommerceScraper(),
                new InfiniteScrollScraper(),
                new JavaScriptRenderingScraper(),
                new LoginScraper()
        };

        String[] options = {"E-Commerce Seite", "Infinite Scrolling Seite", "JS Rendering Seite", "Login Seite", "Andere Seite (nicht verfügbar)"};
        int choice = JOptionPane.showOptionDialog(null,
                "Wählen Sie die Website, die Sie parsen möchten:",
                "Website Auswahl",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                options,
                options[0]);

        if (choice >= 0 && choice < scrapers.length) {
            System.out.println("Sie haben " + options[choice] + " gewählt.");
            scrapers[choice].scrape();
        } else {
            System.out.println("Keine gültige Auswahl getroffen.");
        }
    }
}
