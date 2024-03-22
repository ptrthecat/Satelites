package satelity;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Inicjalizacja obiektu klasy Satelity
        SatBeams satelity = new SatBeams();

        // Inicjalizacja obiektu klasy KingOfSat
        KingOfSat kingOfSat = new KingOfSat();

        // Pobranie danych o satelitach z KingOfSat
        List<String[]> kingOfSatList = kingOfSat.getSatellitesData();

        // Pobranie danych o satelitach z klasy Satelity
        List<String[]> satellitesList = satelity.getSatellitesData();

        // Sprawdzenie, czy dane z KingOfSat zostały pobrane
        if (kingOfSatList.isEmpty()) {
            System.out.println("No data retrieved from KingOfSat.");
        }
        else {
            // Wyświetlenie nagłówka dla danych z KingOfSat
            System.out.println("KingOfSat");

            // Wyświetlenie danych o satelitach z KingOfSat
            for (String[] satellite : kingOfSatList) {
                System.out.println(String.join(" , ", satellite));
            }
        }
        // Sprawdzenie, czy dane z klasy Satelity zostały pobrane
        if (satellitesList.isEmpty()) {
            System.out.println("No data retrieved from Satelity.");
        }
        else {
            // Wyświetlenie nagłówka dla danych z klasy Satelity
            System.out.println("SatBeams");

            // Wyświetlenie danych o satelitach z klasy Satelity
            for (String[] satellite : satellitesList) {
                System.out.println(String.join(" , ", satellite));
            }
        }
    }
}
