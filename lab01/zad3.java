public class zad3 {
    public static double obliczWiekNaPlanecie(double sekundy, String planeta) {
        final double SECONDS_IN_EARTH_YEAR = 31557600.0;
        double lataZiemskie = sekundy / SECONDS_IN_EARTH_YEAR;
        switch (planeta.toLowerCase()) {
            case "ziemia":
                return lataZiemskie;
            case "merkury":
                return lataZiemskie / 0.2408467;
            case "wenus":
                return lataZiemskie / 0.61519726;
            case "mars":
                return lataZiemskie / 1.8808158;
            case "jowisz":
                return lataZiemskie / 11.862615;
            case "saturn":
                return lataZiemskie / 29.447498;
            case "uran":
                return lataZiemskie / 84.016846;
            case "neptun":
                return lataZiemskie / 164.79132;
            default:
                throw new IllegalArgumentException("Nieznana planeta: " + planeta);
        }
    }

    public static void main(String[] args) {
        double sekundy = 1000000000;
        String planeta = "merkury";
        double wiek = obliczWiekNaPlanecie(sekundy, planeta);
        System.out.printf("Wiek na %s: %.2f lat\n", planeta, wiek);
    }
}
