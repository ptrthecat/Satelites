package satelity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SatBeams {
    // Adres URL witryny SatBeams
    final String satBeamsUrl = "https://www.satbeams.com/satellites";
    // Lista zawierająca dane o satelitach
    List<String[]> satellitesList = new ArrayList<>();

    // Zmienne przechowujące dane ze strony głównej
    public String orbitalPosition;
    public String status;
    public String satelliteName;
    public String norad;
    public String cospar;
    public String model;
    public String operator;
    public String launchSite;
    public String launchMass;
    public String launchDate;

    // Konstruktor klasy Satelity
    public SatBeams() {
        webScraper();
        // displayData();
    }

    // Metoda wykonująca web scraping ze strony SatBeams
    void webScraper() {
        try {
            final Document document = Jsoup.connect(satBeamsUrl).get();
            System.out.println("Strona główna pomyślnie zescrapowana");
            scrapMainPage(document);
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas łączenia się z adresem URL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metoda scrapująca dane ze strony głównej
    private void scrapMainPage(Document document) {
        Elements rows = document.select(".class_tr");
        int satelliteCount = 0;

        for (Element row : rows) {
            Elements cells = row.select("td");

            if (cells.size() >= 11) {
                // Wydobywanie danych ze strony głównej
                orbitalPosition = cells.get(1).text();
                status = cells.get(2).text();
                satelliteName = cells.get(3).select("a").text();
                norad = cells.get(4).text();
                cospar = cells.get(5).text();
                model = cells.get(6).text();
                operator = cells.get(7).text();
                launchSite = cells.get(8).text();
                launchMass = cells.get(9).text();
                launchDate = cells.get(10).text();

                // Dodawanie danych do listy satellitesList
                satellitesList.add(new String[]{orbitalPosition, status, satelliteName, norad, cospar, model,
                        operator, launchSite, launchMass, launchDate});

                // Wydobywanie linku do podstrony
                String hrefValue = cells.get(1).select("a").attr("href");
                String subpageUrl = "https://www.satbeams.com" + hrefValue;
                //scrapSubpage(subpageUrl);
                satelliteCount++;
            }
        }
    }

    // Metoda zwracająca listę danych o satelitach
    List<String[]> getSatellitesData() {
        return satellitesList;
    }

}
