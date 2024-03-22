package satelity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KingOfSat {
    // Adres URL witryny KingOfSat
    final String kingOfSatUrl = "https://en.kingofsat.net/satellites.php";
    // Lista zawierająca dane o satelitach
    List<String[]> satellitesList = new ArrayList<>();
    List<String[]> transpondersList = new ArrayList<>();

    // Dane ze strony głównej
    public String satelliteName;
    public String orbitalPosition;
    public String channels;
    public String longitude;
    public String maxDeclination;
    // Konkretne dane z podstron
    public String frequence;
    public String polarization;
    public String txp;
    public String beam;
    public String standard;
    public String modulation;
    public String srFec;
    public String networkBitrate;
    public String norad;

    /**
     * Konstruktor klasy KingOfSat. Inicjuje proces web scrapingu.
     */
    KingOfSat() {

        webScrapper();
        //displayData();
    }

    /**
     * Metoda inicjująca proces web scrapingu z adresu https://en.kingofsat.net/satellites.php.
     */
    void webScrapper() {
        try {
            final Document document = Jsoup.connect(kingOfSatUrl).get();
            System.out.println("Strona główna pomyślnie zescrapowana");
            scrapMainPage(document);
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas łączenia się z adresem URL: " + e.getMessage());
            e.printStackTrace();
            //System.exit(1);
        }
    }

    /**
     * Metoda scrapująca stronę główną i zbierająca dane o satelitach.
     *
     * @param document Dokument HTML strony głównej.
     */
    private void scrapMainPage(Document document) {
        Elements rows = document.select("table.footable tr");

        for (Element row : rows) {
            Elements cells = row.select("td");

            if (cells.size() >= 10) {  // Upewnij się, że jest wystarczająco komórek
                // Wydobywanie danych ze strony głównej
                satelliteName = cells.get(1).select("a").text();
                orbitalPosition = cells.get(0).text();
                norad = cells.get(2).text();
                channels = cells.get(5).text();
                longitude = cells.get(7).text();
                maxDeclination = cells.get(9).text();

                if (!satelliteName.contains("Moving")) {
                    satellitesList.add(new String[]{satelliteName, orbitalPosition, channels, longitude, maxDeclination, norad});
                    // Wydobywanie linku do podstrony
                    String subpageUrl = "https://en.kingofsat.net/" + cells.get(1).select("a").attr("href");
                    // Wywołanie metody do scrapowania konkretnych danych z podstron
                    scrapSubpage(subpageUrl);
                }
            }
        }
    }

    /**
     * Metoda scrapująca dane z podstrony.
     *
     * @param subpageUrl URL podstrony.
     */
    private void scrapSubpage(String subpageUrl) {
        try {
            final Document subpageDocument = Jsoup.connect(subpageUrl).get();
            System.out.println("Podstrona pomyślnie zescrapowana: " + subpageUrl);
            Elements tables = subpageDocument.select("table.frq");

            // Sprawdzenie, czy istnieje druga tabela.frq
            if (tables.size() >= 2) {

                for (int i = 1; i < tables.size(); i++) {
                    Element frqTable = tables.get(i);
                    Elements rows = frqTable.select("tr");
                    if (!rows.isEmpty()) {
                        Element dataRow = rows.first();  // Wydobywanie pierwszego wiersza

                        frequence = dataRow.select("td").get(2).text();
                        polarization = dataRow.select("td").get(3).text();
                        txp = dataRow.select("td").get(4).text();
                        beam = dataRow.select("td").get(5).text();
                        standard = dataRow.select("td").get(6).text();
                        modulation = dataRow.select("td").get(7).text();
                        srFec = dataRow.select("td").get(8).text();
                        networkBitrate = dataRow.select("td").get(9).text();

                        // Dodanie zdobytych danych do listy transpondersList
                        transpondersList.add(new String[]{satelliteName, orbitalPosition, frequence, polarization, txp, beam, standard, modulation, srFec, networkBitrate});
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas scrapowania podstrony: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metoda zwracająca listę danych o satelitach.
     *
     * @return Lista danych o satelitach.
     */
    List<String[]> getSatellitesData() {
        //metoda zwracająca listę satelitów
        return satellitesList;
    }

}